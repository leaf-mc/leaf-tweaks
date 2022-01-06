package mc.leaf.modules.tweaks.restore;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ShovelRestoreOptions {

    private final List<Material> supportedMaterials = new ArrayList<>();
    private       boolean        tweakEnabled       = true;
    private       boolean        useItemDurability  = true;

    /**
     * Constructs a new object.
     */
    public ShovelRestoreOptions() {

        this.supportedMaterials.add(Material.WOODEN_SHOVEL);
        this.supportedMaterials.add(Material.STONE_SHOVEL);
        this.supportedMaterials.add(Material.IRON_SHOVEL);
        this.supportedMaterials.add(Material.GOLDEN_SHOVEL);
        this.supportedMaterials.add(Material.DIAMOND_SHOVEL);
        this.supportedMaterials.add(Material.NETHERITE_SHOVEL);
    }

    public boolean isTweakEnabled() {

        return tweakEnabled;
    }

    public void setTweakEnabled(boolean tweakEnabled) {

        this.tweakEnabled = tweakEnabled;
    }

    public void setUseItemDurability(boolean useItemDurability) {

        this.useItemDurability = useItemDurability;
    }

    public boolean isUsingItemDurability() {

        return useItemDurability;
    }

    public List<Material> getSupportedMaterials() {

        return this.supportedMaterials;
    }

}
