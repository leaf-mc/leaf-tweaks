package mc.leaf.modules.tweaks;

import mc.leaf.modules.tweaks.commands.TweakCommand;
import mc.leaf.core.interfaces.ILeafCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public final class LeafTweaks extends JavaPlugin {

    public static final String           PREFIX = "§l[§aLeaf§bTweaks§r§l]§r";
    private             LeafTweaksModule module = null;

    @Override
    public void onEnable() {

        Plugin plugin = Bukkit.getPluginManager().getPlugin("LeafCore");
        if (plugin instanceof ILeafCore core) {
            this.module = new LeafTweaksModule(this, core);
        } else {
            this.getLogger().severe("Unable to find LeafCore instance.");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.module = null;
    }

    public void registerDisabledCommand() {
        Optional.ofNullable(Bukkit.getPluginCommand("tweaks")).ifPresent(pluginCommand -> pluginCommand.setExecutor(new TweakCommand()));
    }

}
