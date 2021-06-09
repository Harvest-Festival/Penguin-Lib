package uk.joshiejack.penguinlib.data.generators.builders;

import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.penguinlib.data.generators.AbstractDatabaseProvider;

public class TradeBuilder {
    private final VillagerProfession profession;
    private IItemProvider input = Items.EMERALD;
    private final int tier;
    private int inputAmount = 1;
    private IItemProvider output = Items.EMERALD;
    private int outputAmount = 1;
    private int maxTrades = 32;
    private int xp = 16;
    private float priceMultiplier = 2F;

    public TradeBuilder(VillagerProfession profession, int tier, IItemProvider output) {
        this.profession = profession;
        this.tier = tier;
        this.output = output;
    }

    public TradeBuilder setInput(IItemProvider item) {
        this.input = item;
        return this;
    }

    public TradeBuilder setInputAmount(int i) {
        this.inputAmount = i;
        return this;
    }

    public TradeBuilder setOutputAmount(int i) {
        this.outputAmount = i;
        return this;
    }

    public TradeBuilder setMaxTrades(int i) {
        this.maxTrades = i;
        return this;
    }

    public TradeBuilder setXP(int i) {
        this.xp = i;
        return this;
    }

    public TradeBuilder setPriceMultiplier(float mp) {
        this.priceMultiplier = mp;
        return this;
    }

    public void build(AbstractDatabaseProvider gen) {
        gen.addEntry("villager_trades", "Profession,Tier,Input Item,Input Amount,Output Item,Output Amount,Max Trades,Experience,Price Multiplier",
                CSVUtils.join(profession.getRegistryName().toString(), tier, input.asItem().getRegistryName().toString(), inputAmount,
                        output.asItem().getRegistryName(), outputAmount,
                        maxTrades, xp, priceMultiplier));
    }
}
