package mc.leaf.modules.tweaks;

import mc.leaf.modules.tweaks.commands.TweakModuleCommand;
import mc.leaf.modules.tweaks.harvesting.HoeHarvestingTweak;
import mc.leaf.modules.tweaks.restore.ShovelRestoreTweak;
import mc.leaf.core.interfaces.ILeafCore;
import mc.leaf.core.interfaces.ILeafModule;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class LeafTweaksModule implements ILeafModule {

    private final LeafTweaks         plugin;
    private final ILeafCore          core;
    private       boolean            enabled = false;
    private       HoeHarvestingTweak hoeHarvestingTweak;
    private       ShovelRestoreTweak shovelRestoreTweak;

    public LeafTweaksModule(LeafTweaks plugin, ILeafCore core) {

        this.plugin = plugin;

        this.core = core;
        this.core.registerModule(this);
    }

    @Override
    public void onEnable() {

        this.plugin.getLogger().info("Loading tweaks...");
        this.hoeHarvestingTweak = new HoeHarvestingTweak(this);
        this.shovelRestoreTweak = new ShovelRestoreTweak(this);

        this.plugin.getLogger().info("Registering tweaks...");
        this.core.getEventBridge().register(this, this.hoeHarvestingTweak);
        this.core.getEventBridge().register(this, this.shovelRestoreTweak);

        this.plugin.getLogger().info("Registering command");
        Optional.ofNullable(Bukkit.getPluginCommand("tweaks")).ifPresent(pluginCommand -> pluginCommand.setExecutor(new TweakModuleCommand(this)));

        this.enabled = true;
    }

    @Override
    public void onDisable() {

        this.plugin.getLogger().info("Unregistering tweaks listeners...");
        this.core.getEventBridge().unregister(this);

        this.plugin.getLogger().info("Cleaning up...");
        this.hoeHarvestingTweak = null;
        this.shovelRestoreTweak = null;
        this.plugin.registerDisabledCommand();

        this.enabled = false;
    }

    @Override
    public ILeafCore getCore() {

        return this.core;
    }

    @Override
    public String getName() {

        return "Tweaks";
    }

    @Override
    public boolean isEnabled() {

        return this.enabled;
    }

    @Override
    public JavaPlugin getPlugin() {

        return this.plugin;
    }

    public HoeHarvestingTweak getHoeHarvestingTweak() {

        return hoeHarvestingTweak;
    }

    public ShovelRestoreTweak getShovelRestoreTweak() {

        return shovelRestoreTweak;
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
