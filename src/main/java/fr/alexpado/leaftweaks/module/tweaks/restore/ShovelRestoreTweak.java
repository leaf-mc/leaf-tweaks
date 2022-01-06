package fr.alexpado.leaftweaks.module.tweaks.restore;

import fr.alexpado.leaftweaks.module.LeafTweaksModule;
import mc.leaf.core.events.LeafListener;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ShovelRestoreTweak extends LeafListener {

    private final LeafTweaksModule     module;
    private final ShovelRestoreOptions options;

    public ShovelRestoreTweak(LeafTweaksModule module) {

        this.module  = module;
        this.options = new ShovelRestoreOptions();
    }

    public ShovelRestoreOptions getOptions() {

        return options;
    }

    private boolean canRestore(Material tool) {

        return this.getOptions().getSupportedMaterials().contains(tool);
    }

    private void doRestoreFeedback(Player player, Block block) {

        World world = block.getWorld();
        world.playSound(block.getLocation(), Sound.BLOCK_GRAVEL_BREAK, SoundCategory.BLOCKS, 1, 0.8f);
        player.swingMainHand();
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {

        // Tweak start
        if (!this.getOptions().isTweakEnabled() || !this.module.isRightClickOnBlockWithItem(event)) {
            return;
        }

        // Safe usage of requireNonNull here, as it's checked within isInteractionOnBlockWithItem
        Block     block = Objects.requireNonNull(event.getClickedBlock());
        ItemStack tool  = Objects.requireNonNull(event.getItem());

        if (block.getType() != Material.FARMLAND || !this.canRestore(tool.getType())) {
            return;
        }

        block.setType(Material.DIRT);

        if (this.getOptions().isUsingItemDurability()) {
            this.module.useItemDurability(event.getPlayer(), tool);
        }

        this.doRestoreFeedback(event.getPlayer(), block);
        event.setCancelled(true);
    }

}
