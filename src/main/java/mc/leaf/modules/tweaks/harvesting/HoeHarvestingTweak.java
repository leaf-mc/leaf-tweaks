package mc.leaf.modules.tweaks.harvesting;

import mc.leaf.modules.tweaks.LeafTweaksModule;
import mc.leaf.core.events.LeafListener;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HoeHarvestingTweak extends LeafListener {

    private final LeafTweaksModule     module;
    private final HoeHarvestingOptions options;
    private final Map<UUID, Integer>   tickTimeout;

    public HoeHarvestingTweak(LeafTweaksModule module) {

        this.module = module;

        this.options     = new HoeHarvestingOptions();
        this.tickTimeout = new HashMap<>();
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

    private void doHarvestFeedback(Player player, Block block) {

        World world = block.getWorld();
        world.playSound(block.getLocation(), Sound.BLOCK_CROP_BREAK, SoundCategory.BLOCKS, 0.9f, 1);
        player.swingMainHand();
        // TODO: Particles
    }

    @Override
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        // Avoid double event fire
        Integer lastUse = this.tickTimeout.getOrDefault(event.getPlayer().getUniqueId(), 0);
        if (lastUse + 1 >= Bukkit.getCurrentTick()) {
            return;
        }
        this.tickTimeout.remove(event.getPlayer().getUniqueId());


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

        Collection<ItemStack> drops   = block.getDrops(tool);
        BlockData             data    = block.getState().getBlockData();
        Ageable               ageable = (Ageable) data;

        if (ageable.getAge() != ageable.getMaximumAge()) {
            return;
        }

        boolean doCrop = false;

        Material replant = this.getOptions().getHarvestMaterialMap().get(block.getType());

        for (ItemStack drop : drops) {
            if (drop.getType() == replant) {
                drop.setAmount(drop.getAmount() - 1);
                doCrop = true;
                break;
            }
        }

        if (!doCrop) {
            return;
        }

        drops.stream().filter(itemStack -> itemStack.getType() != Material.AIR)
                .filter(itemStack -> itemStack.getAmount() > 0)
                .forEach(itemStack -> block.getWorld().dropItemNaturally(block.getLocation(), itemStack));
        ageable.setAge(0);
        block.setBlockData(ageable);

        // Player feedback
        if (this.getOptions().isUsingItemDurability()) {
            this.module.useItemDurability(event.getPlayer(), tool);
        }

        this.doHarvestFeedback(event.getPlayer(), block);
        this.tickTimeout.put(event.getPlayer().getUniqueId(), Bukkit.getCurrentTick());
    }

}
