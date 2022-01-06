package mc.leaf.modules.tweaks.commands;

import mc.leaf.modules.tweaks.LeafTweaks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TweakCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        sender.sendMessage(LeafTweaks.PREFIX + " Please enable this module before using any commands.");
        return true;
    }

}
