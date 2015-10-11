package com.shawckz.myhcf.faction.serial;

import com.shawckz.myhcf.configuration.AbstractSerializer;
import com.shawckz.myhcf.land.Claim;


/**
 * Created by 360 on 22/07/2015.
 */
public class ClaimSerializer extends AbstractSerializer<Claim> {

    @Override
    public String toString(Claim data) {
        return data.toString();
    }

    @Override
    public Claim fromString(Object data) {
        if(data instanceof String){
            String s = (String) data;
            return Claim.fromString(s);
        }
        return null;
    }
}
