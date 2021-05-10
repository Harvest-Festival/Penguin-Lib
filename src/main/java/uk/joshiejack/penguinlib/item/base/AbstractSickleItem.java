package uk.joshiejack.penguinlib.item.base;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSickleItem extends ToolItem {
    public static final ToolType SICKLE = ToolType.get("sickle");
    protected static final Int2IntMap areaByHarvestLevel = new Int2IntOpenHashMap();
    private static final Set<Material> EFFECTIVE_MATERIALS = ImmutableSet.of(Material.PLANT, Material.WATER_PLANT, Material.REPLACEABLE_PLANT,
            Material.REPLACEABLE_FIREPROOF_PLANT, Material.REPLACEABLE_WATER_PLANT, Material.LEAVES, Material.BAMBOO_SAPLING, Material.BAMBOO,
            Material.CACTUS);
    private static final Set<Block> EFFECTIVE_BLOCKS = new HashSet<>();

    public AbstractSickleItem(IItemTier tier, Item.Properties properties) {
        super(2, -2.6F, tier, EFFECTIVE_BLOCKS, properties.addToolType(SICKLE, tier.getLevel()));
        areaByHarvestLevel.put(0, 0);
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, @Nonnull BlockState state) {
        float ret = super.getDestroySpeed(stack, state);
        return ret > 1 ? ret : EFFECTIVE_MATERIALS.contains(state.getMaterial()) ? speed : 1F;
    }
}
