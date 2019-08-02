package org.MachinaEconomy.ThePrometeus.Entities;

import java.util.UUID;
import org.MachinaEconomy.ThePrometeus.Entity;
import org.bukkit.entity.Player;

public class MachinaPlayerEntity extends Entity {
    private final Player player;

    public MachinaPlayerEntity(Player p) {
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
