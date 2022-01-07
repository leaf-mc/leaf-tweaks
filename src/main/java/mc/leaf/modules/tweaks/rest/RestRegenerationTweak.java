package mc.leaf.modules.tweaks.rest;

import io.papermc.paper.event.player.PlayerDeepSleepEvent;
import mc.leaf.core.events.LeafListener;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

public class RestRegenerationTweak extends LeafListener {

    private final RestRegenerationOptions options;

    public RestRegenerationTweak() {

        this.options = new RestRegenerationOptions();
    }

    public RestRegenerationOptions getOptions() {

        return options;
    }

    @Override
    public void onPlayerDeepSleep(PlayerDeepSleepEvent event) {

        if (!this.getOptions().isTweakEnabled()) {
            return;
        }

        AttributeInstance attribute = event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (attribute != null) {
            event.getPlayer().setHealth(attribute.getValue());
        }
    }

}
