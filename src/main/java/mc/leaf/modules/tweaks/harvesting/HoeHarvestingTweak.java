package mc.leaf.modules.tweaks.harvesting;

import mc.leaf.core.events.LeafListener;
import mc.leaf.modules.tweaks.LeafTweaksModule;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class HoeHarvestingTweak extends LeafListener {

    private final LeafTweaksModule     module;
    private final HoeHarvestingOptions options;

    public HoeHarvestingTweak(LeafTweaksModule module) {

        this.module = module;

        this.options = new HoeHarvestingOptions();
    }

    public HoeHarvestingOptions getOptions() {

        return this.options;
    }

    private boolean isHarvestable(Material crop) {

        return this.getOptions().getHarvestMaterialMap().containsKey(crop);
    }

    private boolean canHarvest(Material tool) {

        return this.getOptions().getHarvestRadiusMap().containsKey(tool);
    }

    @Override
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        // Tweak start
        if (!this.getOptions().isTweakEnabled() || !this.module.isRightClickOnBlockWithItem(event)) {
            return;
        }

        // Safe usage of requireNonNull here, as it's checked within isInteractionOnBlockWithItem
        Block     block = Objects.requireNonNull(event.getClickedBlock());
        ItemStack tool  = Objects.requireNonNull(event.getItem());

        if (!this.isHarvestable(block.getType()) || !this.canHarvest(tool.getType())) {
            return;
        }

        if (event.getPlayer().hasCooldown(tool.getType())) {
            event.setCancelled(true);
            return;
        }

        BlockData data    = block.getState().getBlockData();
        Ageable   ageable = (Ageable) data;

        if (ageable.getAge() != ageable.getMaximumAge()) {
            return;
        }

        ageable.setAge(0);
        event.getPlayer().swingMainHand();
        block.breakNaturally(tool, true);
        block.setBlockData(ageable);

        // Durability
        if (this.shouldDepleteDurability(event.getPlayer())) {
            this.module.useItemDurability(event.getPlayer(), tool);
        }

        event.getPlayer().setCooldown(tool.getType(), this.getOptions().getHarvestTimeoutMap().get(tool.getType()));
    }

    private boolean shouldDepleteDurability(HumanEntity player) {

        return this.getOptions().isUsingItemDurability() &&
                (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE);
    }

}
