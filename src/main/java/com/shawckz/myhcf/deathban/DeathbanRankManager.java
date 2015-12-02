package com.shawckz.myhcf.deathban;

import java.util.HashMap;
import java.util.Map;

public class DeathbanRankManager {

    private final Map<String, DeathbanRank> ranks = new HashMap<>();

    public DeathbanRank getRank(String rankName) {
        return ranks.get(rankName);
    }

    public void registerRank(DeathbanRank rank) {
        ranks.put(rank.getRank(), rank);
    }

    public int getSeconds(String rankName) {
        return getRank(rankName).getSeconds();
    }

}
