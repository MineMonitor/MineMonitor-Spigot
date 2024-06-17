package net.minemonitor.event;

import net.minemonitor.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
//        Main.getInstance().getStatsManager().getPlayTimeManager().getStartTime(e.getPlayer().getUniqueId());
    }
}
