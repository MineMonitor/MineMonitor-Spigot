package com.minemonitor.monitor.player.playtime;

import java.util.HashMap;
import java.util.UUID;

public class PlaytimeManager implements IPlaytimeManager {

    private final HashMap<UUID, Long> startTime;

    public PlaytimeManager() {
        this.startTime = new HashMap<>();
    }


    @Override
    public Long getStartTime(UUID uuid) {
        return this.startTime.get(uuid);
    }

    @Override
    public Long getPlayTime(UUID uuid) {
        Long start = this.startTime.get(uuid);
        if (start == null) {
            return null;
        }

        return System.currentTimeMillis() - start;
    }

    @Override
    public void trackPlayer(UUID uuid) {
        this.startTime.put(uuid, System.currentTimeMillis());
    }

    @Override
    public void unTrackPlayer(UUID uuid) {
        this.startTime.remove(uuid);
    }
}
