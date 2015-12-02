package com.shawckz.myhcf.faction.serial;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.AbstractSerializer;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.util.HCFException;

public class HCFPlayerIdSerializer extends AbstractSerializer<HCFPlayer> {

    @Override
    public String toString(HCFPlayer data) {
        return data.getUniqueId();
    }

    @Override
    public HCFPlayer fromString(Object data) {
        if (data instanceof String) {
            return Factions.getInstance().getCache().getHCFPlayerByUUID(((String) data));
        } else {
            throw new HCFException("HCFPlayerIdSerializer: data != String");
        }
    }
}
