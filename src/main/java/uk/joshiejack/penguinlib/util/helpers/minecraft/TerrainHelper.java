package uk.joshiejack.penguinlib.util.helpers.minecraft;

import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class TerrainHelper {
    public static boolean isWater(IWorld world, BlockPos... positions) {
        for (BlockPos pos : positions) {
            if (world.getBlockState(pos).getMaterial() != Material.WATER) return false;
        }

        return true;
    }

    public static boolean isWaterSource(IWorld world, BlockPos... positions) {
        for (BlockPos pos : positions) {
            if (world.getFluidState(pos).getType() != Fluids.WATER) return false;
        }

        return true;
    }
}

    /*

    private static final Cache<Integer, List<BlockPos>> radius = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();

    private static List<BlockPos> getOffsetForRadius(int radius) {
        List<BlockPos> integers = Lists.newArrayList();
        try {
            integers = TerrainHelper.radius.get(radius, () -> {
                List<BlockPos> list = Lists.newArrayList();
                for (int i = -radius; i <= radius; i++) {
                    for (int l = -radius; l <= radius; l++) {
                        if (i * i + l * l >= (radius + 0.50f) * (radius + 0.50f)) {
                            continue;
                        }

                        list.add(new BlockPos(i, 0, l));
                    }
                }

                return list;
            });
        } catch (ExecutionException ignored) {
        }

        return integers;
    }

    @Nullable
    public static BlockPos getRandomBlockInRadius(World world, BlockPos pos, int radius, BiPredicate<World, BlockPos> predicate) {
        List<BlockPos> integers = getOffsetForRadius(radius);
        Collections.shuffle(integers);
        for (BlockPos pair : integers) {
            BlockPos target = pos.add(pair);
            if (predicate.test(world, target)) {
                return target;
            }
        }

        return null;
    }

    public static int getXDistance(BlockPos master, BlockPos slave) {


        return master.getX() - slave.getX();

        //388 MASTER
        //390 SLAVE  -2

        //-355 MASTER
        //-357 SLAVE

        //1 MASTER   1 - 0 = WANT RESULT OF +1
        //0 SLAVE

        //0, 42, 43 MASTER
        //1, 43, 43 SLAVE       (WANT RESULT OF -1)

        // -1 for the slave which would be correct
    }

    private static final Cache<Integer, List<BlockPos>> reach = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();

    private static List<BlockPos> getOffsetForReach(int reach) {
        List<BlockPos> positions = Lists.newArrayList();
        try {
            positions = TerrainHelper.reach.get(reach, () -> {
                List<BlockPos> list = Lists.newArrayList();
                for (int x = -reach; x <= reach; x++) {
                    for (int z = -reach; z <= reach; z++) {
                        for (int y = -1; y <= 1; y++) {
                            list.add(new BlockPos(x, y, z));
                        }
                    }
                }

                return list;
            });
        } catch (ExecutionException ignored) {
        }
        return positions;
    }

    @Nullable
    public static BlockPos getFirstBlockWithinReach(PlayerEntity player, BiPredicate<World, BlockPos> predicate) {
        BlockPos pos = new BlockPos(player);
        int reach = (int) player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        List<BlockPos> positions = getOffsetForReach(reach);
        for (BlockPos target : positions) {
            BlockPos check = pos.add(target);
            if (predicate.test(player.level, check)) {
                return check;
            }
        }

        return null;
    }

    public static <V> List<V> getAllBlocksWithinReach(PlayerEntity player, BiPredicate<BlockPos, List<V>> predicate) {
        BlockPos pos = new BlockPos(player);
        int reach = (int) player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        List<V> list = Lists.newArrayList();
        for (int x = -reach; x <= reach; x++) {
            for (int z = -reach; z <= reach; z++) {
                for (int y = -1; y <= 1; y++) {
                    predicate.test(pos.add(x, y, z), list);
                }
            }
        }

        return list;
    }

    public static void collectDrops(World world, BlockPos pos, BlockState state, PlayerEntity player, NonNullList<ItemStack> drops, boolean silkTouch) {
        Block block = state.getBlock();
        if (silkTouch && block.canSilkHarvest(world, pos, state, player)) {
            try {
                Method method = ReflectionHelper.findMethod(Block.class, "getSilkTouchDrop", "func_180643_i", BlockState.class);
                ItemStack stack = (ItemStack) method.invoke(block, state);
                if (stack != null) {
                    drops.add(stack);
                    ForgeEventFactory.fireBlockHarvesting(drops, world, pos, state, 0, 1F, true, player);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {}
        } else {
            block.getDrops(drops, world, pos, state, 0);
            ForgeEventFactory.fireBlockHarvesting(drops, world, pos, state, 0, 1F, false, player);
       }
    }

    public static boolean destroyBlock(World world, BlockPos pos, EntityPlayerMP player) {
        int exp = ForgeHooks.onBlockBreakEvent(world, player.interactionManager.getGameType(), player, pos);
        if (exp == -1) {
            return false;
        } else {
            BlockState iblockstate = world.getBlockState(pos);
            TileEntity tileentity = world.getTileEntity(pos);
            Block block = iblockstate.getBlock();

            if ((block instanceof BlockCommandBlock || block instanceof BlockStructure) && !player.canUseCommandBlock()) {
                world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
                return false;
            } else {
                ItemStack stack = player.getHeldItemMainhand();
                if (!stack.isEmpty() && stack.getItem().onBlockStartBreak(stack, pos, player)) return false;

                world.playEvent(player, 2001, pos, Block.getStateId(iblockstate));
                boolean flag1;
                if (player.capabilities.isCreativeMode) {
                    flag1 = iblockstate.getBlock().removedByPlayer(iblockstate, world, pos, player, false);
                    player.connection.sendPacket(new SPacketBlockChange(world, pos));
                } else {
                    ItemStack itemstack1 = player.getHeldItemMainhand();
                    ItemStack itemstack2 = itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1.copy();
                    boolean flag = iblockstate.getBlock().canHarvestBlock(world, pos, player);

                    if (!itemstack1.isEmpty()) {
                        if (itemstack1.isEmpty())
                            ForgeEventFactory.onPlayerDestroyItem(player, itemstack2, EnumHand.MAIN_HAND);
                    }

                    flag1 = iblockstate.getBlock().removedByPlayer(iblockstate, world, pos, player, true);
                    iblockstate.getBlock().onPlayerDestroy(world, pos, iblockstate);
                    if (flag1 && flag) {
                        iblockstate.getBlock().harvestBlock(world, player, pos, iblockstate, tileentity, itemstack2);
                    }
                }

                // Drop experience
                if (!player.capabilities.isCreativeMode && flag1 && exp > 0) {
                    iblockstate.getBlock().dropXpOnBlockBreak(world, pos, exp);
                }
                return flag1;
            }
        }
    } */
