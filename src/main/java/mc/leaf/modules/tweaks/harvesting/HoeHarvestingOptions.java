package mc.leaf.modules.tweaks.harvesting;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class HoeHarvestingOptions {

    private final Map<Material, Material> harvestMaterialMap = new HashMap<>();
    private final Map<Material, Integer>  harvestRadiusMap   = new HashMap<>();
    private       boolean                 tweakEnabled       = true;
    private       boolean                 useItemDurability  = true;

    /**
     * Constructs a new object.
     */
    public HoeHarvestingOptions() {

        this.harvestMaterialMap.put(Material.WHEAT, Material.WHEAT_SEEDS);
        this.harvestMaterialMap.put(Material.BEETROOTS, Material.BEETROOT_SEEDS);
        this.harvestMaterialMap.put(Material.CARROTS, Material.CARROT);
        this.harvestMaterialMap.put(Material.POTATOES, Material.POTATO);

        this.harvestRadiusMap.put(Material.WOODEN_HOE, 1);
        this.harvestRadiusMap.put(Material.STONE_HOE, 1);
        this.harvestRadiusMap.put(Material.IRON_HOE, 1);
        this.harvestRadiusMap.put(Material.GOLDEN_HOE, 1);
        this.harvestRadiusMap.put(Material.DIAMOND_HOE, 1);
        this.harvestRadiusMap.put(Material.NETHERITE_HOE, 1);
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

    public Map<Material, Integer> getHarvestRadiusMap() {

        return harvestRadiusMap;
    }

    public Map<Material, Material> getHarvestMaterialMap() {

        return harvestMaterialMap;
    }

}
