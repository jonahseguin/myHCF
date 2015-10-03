package com.shawckz.myhcf.faction;

import lombok.Data;

@Data
public class FactionMember {

    private final String factionId;
    private final String memberUuid;
    private final String memberName;
}