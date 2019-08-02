package org.MachinaEconomy.ThePrometeus;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;
import org.apache.commons.lang.NotImplementedException;

public class Entity {
    private Main plugin;

    public String getName() {
        throw new NotImplementedException("This method is not implemented for entity " + this.getName());
    }
    
    public UUID getUUID() {
        throw new NotImplementedException("This method is not implemented for entity " + this.getName());
    }
    
    public byte[] getHexUUID() {
        byte[] uuidBytes    = new byte[16];

        ByteBuffer.wrap(uuidBytes)
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(getUUID().getMostSignificantBits())
                .putLong(getUUID().getLeastSignificantBits());
        
        return uuidBytes;
    }
}