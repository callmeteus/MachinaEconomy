package org.MachinaEconomy.ThePrometeus.Entities;

import java.util.UUID;
import org.MachinaEconomy.ThePrometeus.Entity;
import org.bukkit.OfflinePlayer;

public class MachinaOfflinePlayerEntity extends Entity {
    private final OfflinePlayer player;

    public MachinaOfflinePlayerEntity(OfflinePlayer p) {
        player  = p;
    }

    @Override
    public UUID getUUID() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getName();
    }
}
