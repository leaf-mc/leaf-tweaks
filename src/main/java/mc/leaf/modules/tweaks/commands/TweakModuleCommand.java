package mc.leaf.modules.tweaks.commands;

import mc.leaf.core.api.command.PluginCommandImpl;
import mc.leaf.core.api.command.annotations.Param;
import mc.leaf.core.api.command.annotations.Runnable;
import mc.leaf.core.api.command.annotations.Sender;
import mc.leaf.modules.tweaks.LeafTweaks;
import mc.leaf.modules.tweaks.LeafTweaksModule;
import mc.leaf.modules.tweaks.harvesting.HoeHarvestingOptions;
import mc.leaf.modules.tweaks.restore.ShovelRestoreOptions;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TweakModuleCommand extends PluginCommandImpl {

    private final LeafTweaksModule module;

    public TweakModuleCommand(LeafTweaksModule module) {

        super(module.getCore());
        this.module = module;
    }

    private void setTweakValue(CommandSender sender, String valueName, boolean newState, Supplier<Boolean> stateGetter, Consumer<Boolean> stateSetter) {

        String stateName = newState ? "enabled" : "disabled";

        if (stateGetter.get() == newState) {
            sender.sendMessage(LeafTweaks.PREFIX + " This " + valueName + " is already " + stateName + ".");
            return;
        }

        stateSetter.accept(newState);
        sender.sendMessage(LeafTweaks.PREFIX + " This " + valueName + " is now " + stateName + ".");
    }

    private void setTweakState(CommandSender sender, boolean newState, Supplier<Boolean> stateGetter, Consumer<Boolean> stateSetter) {

        this.setTweakValue(sender, "tweak", newState, stateGetter, stateSetter);
    }

    private void setTweakOption(CommandSender sender, boolean newState, Supplier<Boolean> stateGetter, Consumer<Boolean> stateSetter) {

        this.setTweakValue(sender, "option", newState, stateGetter, stateSetter);
    }

    @Runnable("harvesting options enabled {booleanState}")
    public void toggleHarvestingTweak(@Sender CommandSender sender, @Param String booleanState) {

        boolean              state   = booleanState.equals("true");
        HoeHarvestingOptions options = this.module.getHoeHarvestingTweak().getOptions();
        this.setTweakState(sender, state, options::isTweakEnabled, options::setTweakEnabled);
    }

    @Runnable("harvesting options useDurability {booleanState}")
    public void toggleHarvestingDurability(@Sender CommandSender sender, @Param String booleanState) {

        boolean              state   = booleanState.equals("true");
        HoeHarvestingOptions options = this.module.getHoeHarvestingTweak().getOptions();
        this.setTweakOption(sender, state, options::isUsingItemDurability, options::setUseItemDurability);
    }

    @Runnable("farmRestore options enabled {booleanState}")
    public void toggleFarmRestoreTweak(@Sender CommandSender sender, @Param String booleanState) {

        boolean              state   = booleanState.equals("true");
        ShovelRestoreOptions options = this.module.getShovelRestoreTweak().getOptions();
        this.setTweakState(sender, state, options::isTweakEnabled, options::setTweakEnabled);
    }

    @Runnable("farmRestore options useDurability {booleanState}")
    public void toggleFarmRestoreDurability(@Sender CommandSender sender, @Param String booleanState) {

        boolean              state   = booleanState.equals("true");
        ShovelRestoreOptions options = this.module.getShovelRestoreTweak().getOptions();
        this.setTweakOption(sender, state, options::isUsingItemDurability, options::setUseItemDurability);
    }

}
