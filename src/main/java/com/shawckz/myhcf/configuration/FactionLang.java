package com.shawckz.myhcf.configuration;

public enum FactionLang {

    FACTION_INVITE_LOCAL("&9{0}&e has invited &a{1} &eto the faction."),
    FACTION_INVITE_PLAYER("&eYou have been invited to join &9{0} &eby &a{1}&e."),
    FACTION_JOIN_LOCAL("&9{0}&e has joined the faction."),
    FACTION_JOIN_PLAYER("&eYou have joined &9{0}&e."),
    DEATHBAN_KICK("&cYou have been deathbanned.\\n&cYou have {0} lives, login again to use one.\\n&cYour deathban expires in {1}."),
    DEATHBAN_LIFE_USE("&aYou used one of your lives to revive yourself.  You now have {0} lives."),
    BORDER_BLOCK_DENY("&cYou cannot modify blocks beyond the world border."),
    BORDER_PORTAL("&cThat portal's location is past the border. You have been moved inwards."),
    BORDER_TELEPORT("&cThat location is past the border."),
    CHAT_FORMAT("&7[{0}&7] &a{1}&7: &f{2}")//[Faction] player: hi
    ;

    private final String defaultValue;

    FactionLang(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
