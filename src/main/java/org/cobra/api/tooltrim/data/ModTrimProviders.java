package org.cobra.api.tooltrim.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.item.Items;
import org.cobra.api.tooltrim.item.trim.ToolTrimMaterials;
import org.cobra.api.tooltrim.item.trim.ToolTrimPatterns;

public class ModTrimProviders extends TrimProvider {
    public ModTrimProviders(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generate() {
        this.createTrimMaterial("amethyst", Items.AMETHYST_SHARD, ToolTrimMaterials.AMETHYST, "tooltrim.minecraft.amethyst", "minecraft");
        this.createTrimPattern("amethyst", Items.GOLD_NUGGET, "test", false, ToolTrimPatterns.GUARDIAN, "minecraft");
    }
}
