package com.minemonitor.monitor.player.playtime;

import java.util.UUID;

public interface IPlaytimeManager {

     Long getStartTime(UUID uuid);
     Long getPlayTime(UUID uuid);

     void trackPlayer(UUID uuid);
     void unTrackPlayer(UUID uuid);

}
