package org.MachinaEconomy.ThePrometeus.Entities;

import java.util.UUID;
import org.MachinaEconomy.ThePrometeus.Entity;

public class MachinaConsoleEntity extends Entity {
    public MachinaConsoleEntity() {

    }
    
    @Override
    public UUID getUUID() {
        return UUID.fromString("[SYSTEM]");
    }
    
    @Override
    public String getName() {
        return "[SYSTEM]";
    }
}
