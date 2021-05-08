package uk.joshiejack.penguinlib.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public abstract class AbstractDoubleBlock extends AbstractRotatableBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public AbstractDoubleBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public ActionResultType use(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult blockRayTraceResult) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER)
            return super.use(world.getBlockState(pos.below()), world, pos.below(), player, hand, blockRayTraceResult);
        else return super.use(state, world, pos, player, hand, blockRayTraceResult);
    }

    @Nonnull
    @Override
    public BlockState updateShape(@Nonnull BlockState oldState, Direction direction, @Nonnull BlockState newState, @Nonnull IWorld world, @Nonnull BlockPos oldPos, @Nonnull BlockPos newPos) {
        DoubleBlockHalf doubleblockhalf = oldState.getValue(HALF);
        if (direction.getAxis() == Direction.Axis.Y && doubleblockhalf == DoubleBlockHalf.LOWER == (direction == Direction.UP))
            return newState.is(this) && newState.getValue(HALF) != doubleblockhalf ? oldState.setValue(FACING, newState.getValue(FACING)) : Blocks.AIR.defaultBlockState();
        else
            return doubleblockhalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !oldState.canSurvive(world, oldPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(oldState, direction, newState, world, oldPos, newPos);
    }

    @Override
    public void playerWillDestroy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull PlayerEntity player) {
        if (!world.isClientSide && player.isCreative())
            AbstractDoubleBlock.preventCreativeDropFromBottomPart(world, pos, state, player);
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void playerDestroy(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable TileEntity tile, @Nonnull ItemStack stack) {
        super.playerDestroy(world, player, pos, Blocks.AIR.defaultBlockState(), tile, stack);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext ctx) {
        BlockPos blockpos = ctx.getClickedPos();
        if (blockpos.getY() < 255 && ctx.getLevel().getBlockState(blockpos.above()).canBeReplaced(ctx))
            return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()).setValue(HALF, DoubleBlockHalf.LOWER);
        else
            return null;
    }

    @Override
    public void setPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity entity, @Nonnull ItemStack stack) {
        world.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public boolean canSurvive(@Nonnull BlockState state, @Nonnull IWorldReader world, @Nonnull BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = world.getBlockState(blockpos);
        return state.getValue(HALF) == DoubleBlockHalf.LOWER || blockstate.is(this);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public long getSeed(@Nonnull BlockState state, @Nonnull BlockPos pos) {
        return MathHelper.getSeed(pos.getX(), pos.below(state.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HALF, FACING);
    }

    private static void preventCreativeDropFromBottomPart(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull PlayerEntity player) {
        DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
            BlockPos blockpos = pos.below();
            BlockState blockstate = world.getBlockState(blockpos);
            if (blockstate.getBlock() == state.getBlock() && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
                world.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                world.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
            }
        }
    }
}
