package mc.leaf.modules.tweaks;

import mc.leaf.core.api.command.PluginCommandImpl;
import mc.leaf.core.interfaces.impl.LeafModule;
import mc.leaf.modules.tweaks.commands.TweakModuleCommand;
import mc.leaf.modules.tweaks.harvesting.HoeHarvestingTweak;
import mc.leaf.modules.tweaks.restore.ShovelRestoreTweak;
import mc.leaf.core.interfaces.ILeafCore;
import mc.leaf.core.interfaces.ILeafModule;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class LeafTweaksModule extends LeafModule {

    private       boolean            enabled = false;
    private       HoeHarvestingTweak hoeHarvestingTweak;
    private       ShovelRestoreTweak shovelRestoreTweak;

    public LeafTweaksModule(LeafTweaks plugin, ILeafCore core) {

        super(core, plugin);
        this.getCore().registerModule(this);
    }

    @Override
    public void onEnable() {

        this.getPlugin().getLogger().info("Loading tweaks...");
        this.hoeHarvestingTweak = new HoeHarvestingTweak(this);
        this.shovelRestoreTweak = new ShovelRestoreTweak(this);
    }

    @Override
    public List<Listener> getListeners() {

        return Arrays.asList(this.hoeHarvestingTweak, this.shovelRestoreTweak);
    }

    @Override
    public Map<String, PluginCommandImpl> getCommands() {

        return new HashMap<>() {{
            this.put("tweaks", new TweakModuleCommand(LeafTweaksModule.this));
        }};
    }

    @Override
    public void onDisable() {

        this.getPlugin().getLogger().info("Cleaning up...");
        this.hoeHarvestingTweak = null;
        this.shovelRestoreTweak = null;
    }

    @Override
    public String getName() {

        return "Tweaks";
    }

    public HoeHarvestingTweak getHoeHarvestingTweak() {

        return this.hoeHarvestingTweak;
    }

    public ShovelRestoreTweak getShovelRestoreTweak() {

        return this.shovelRestoreTweak;
    }

    // Global helper methods
    private void doItemBreakFeedback(Player player) {

        player.getInventory().setItemInMainHand(null);
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1, 0.9f);
        // TODO: Particles
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isRightClickOnBlockWithItem(PlayerInteractEvent event) {

        return event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                event.getClickedBlock() != null &&
                event.getHand() == EquipmentSlot.HAND &&
                event.getItem() != null;
    }

    public void useItemDurability(Player owner, ItemStack item) {

        if (item.getItemMeta() instanceof Damageable damageable) {
            damageable.setDamage(damageable.getDamage() + 1);

            if (damageable.getDamage() < item.getType().getMaxDurability()) {
                item.setItemMeta(damageable);
            } else {
                // No more use, let's break it.
                this.doItemBreakFeedback(owner);
            }
        }
    }

}
