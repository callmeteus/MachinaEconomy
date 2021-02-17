package org.MachinaEconomy.ThePrometeus;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

public class Entity {
    private Main plugin;

    public String getName() {
        return null;
    }
    
    public UUID getUUID() {
        return null;
    }
    
    public byte[] getHexUUID() {
        byte[] uuidBytes = new byte[16];

        ByteBuffer.wrap(uuidBytes)
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(getUUID().getMostSignificantBits())
                .putLong(getUUID().getLeastSignificantBits());
        
        return uuidBytes;
    }
}