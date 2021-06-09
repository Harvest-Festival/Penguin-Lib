package uk.joshiejack.penguinlib.item.base;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import uk.joshiejack.penguinlib.events.UseWateringCanEvent;
import uk.joshiejack.penguinlib.util.handlers.SingleFluidHandler;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FluidHelper;

import javax.annotation.Nonnull;
import java.util.Set;

public abstract class AbstractWateringCanItem extends Item {
    private final int maxWater;

    public AbstractWateringCanItem(Item.Properties properties, int maxWater) {
        super(properties);
        this.maxWater = maxWater;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new SingleFluidHandler(stack, Fluids.WATER, maxWater);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return (double) (maxWater - FluidHelper.getFluidCapacityFromStack(stack)) / (double) maxWater;
    }

    @Override
    public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
        return FluidHelper.getFluidCapacityFromStack(stack) > 0 ? 0x006DD9 : 0x555555;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (attemptToFill(world, player, stack)) return ActionResult.success(stack);
        else return ActionResult.pass(stack);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean attemptToFill(World world, PlayerEntity player, ItemStack stack) {
        RayTraceResult rayTraceResult = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
        if (rayTraceResult != null && rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
            BlockState state = world.getBlockState(new BlockPos(rayTraceResult.getLocation()));
            if (state.getMaterial() == Material.WATER) {
                return FluidHelper.fillContainer(stack, maxWater);
            }
        }

        return false;
    }

    protected void applyBonemealEffect(World world, BlockPos pos, PlayerEntity player, ItemStack itemstack, Hand hand) {
    }

    private Event.Result onWateringCanUse(ItemStack stack, PlayerEntity player, World worldIn, BlockPos pos) {
        UseWateringCanEvent event = new UseWateringCanEvent(player, stack, worldIn, pos);
        if (MinecraftForge.EVENT_BUS.post(event)) return Event.Result.DENY;
        if (event.getResult() == Event.Result.ALLOW) {
            if (!player.isCreative()) {
                FluidHelper.drainContainer(stack, 1);
            }

            return Event.Result.ALLOW;
        }

        return Event.Result.DEFAULT;
    }

    public Set<BlockPos> getPositions(PlayerEntity player, World world, BlockPos pos) {
        return Sets.newHashSet(pos, pos.below());
    }

    public boolean water(PlayerEntity player, World world, BlockPos pos, ItemStack stack, Hand hand) {
        applyBonemealEffect(world, pos, player, stack, hand); //Do this stuff
        Event.Result hook = onWateringCanUse(stack, player, world, pos);
        if (hook != Event.Result.DEFAULT) return hook == Event.Result.ALLOW;

        BlockState state = world.getBlockState(pos);
        if (state.getProperties().contains(BlockStateProperties.MOISTURE) &&
                state.getValue(BlockStateProperties.MOISTURE) != 7) {
            world.setBlock(pos, state.setValue(BlockStateProperties.MOISTURE, 7), 2);
            if (!player.isCreative() && !stack.isEmpty()) {
                FluidHelper.drainContainer(stack, 1);
            }

            return true;
        } else return false;
    }

    private void onWatered(PlayerEntity player, World world, BlockPos pos, ItemStack itemstack) {
        MinecraftForge.EVENT_BUS.post(new UseWateringCanEvent.Post(player, itemstack, world, pos));
    }

    @Nonnull
    @Override
    public ActionResultType useOn(ItemUseContext ctx) {
        PlayerEntity player = ctx.getPlayer();
        Hand hand = ctx.getHand();
        Direction direction = ctx.getClickedFace();
        BlockPos pos = ctx.getClickedPos();
        World world = ctx.getLevel();
        ItemStack itemstack = player.getItemInHand(hand);
        if (FluidHelper.getFluidCapacityFromStack(itemstack) > 0) {
            if (!player.mayUseItemAt(pos.offset(direction.getNormal()), direction, itemstack)) {
                return ActionResultType.FAIL;
            } else if (world.dimensionType().ultraWarm()){
                int x = pos.getX();
                int y = pos.getY();
                int z = pos.getZ();
                world.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

                for(int l = 0; l < 8; ++l) {
                    world.addParticle(ParticleTypes.LARGE_SMOKE, (double)x + Math.random(), (double)y + Math.random(), (double)z + Math.random(), 0.0D, 0.0D, 0.0D);
                }

                ItemStack stack = player.getItemInHand(hand);
                if (!player.isCreative() && !stack.isEmpty()) {
                    FluidHelper.drainContainer(stack, 1);
                }

                return ActionResultType.SUCCESS;
            } else {
                boolean used = false;
                for (BlockPos target : getPositions(player, world, pos)) {
                    if (FluidHelper.getFluidCapacityFromStack(itemstack) <= 0) break;
                    if (water(player, world, target, itemstack, hand)) {
                        world.playSound(null, target, SoundEvents.GENERIC_SWIM, SoundCategory.NEUTRAL,
                                player.getRandom().nextFloat() * 0.25F + 0.7F, player.getRandom().nextFloat() + 0.5F);
                        for (int i = 0; i < 60; i++) {
                            double x = pos.getX() + player.getRandom().nextFloat();
                            double z = pos.getZ() + player.getRandom().nextFloat();
                            world.addAlwaysVisibleParticle(ParticleTypes.SPLASH, x, pos.getY() + 1D, z, 0, 0, 0);
                        }

                        onWatered(player, world, target, itemstack);
                        used = true;
                    }
                }

                return used ? ActionResultType.SUCCESS : ActionResultType.PASS;
            }
        } else return ActionResultType.FAIL;
    }

    private ItemStack createFilledWateringCan(ItemStack stack) {
        FluidHelper.fillContainer(stack, maxWater);
        return stack;
    }

    @Override
    public void fillItemCategory(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null)
                items.add(createFilledWateringCan(new ItemStack(this)));
            else
                items.add(new ItemStack(this));
        }
    }
}
