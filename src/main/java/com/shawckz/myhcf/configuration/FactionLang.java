package com.shawckz.myhcf.configuration;

public enum FactionLang {

    FACTION_INVITE_LOCAL("&9{0}&e has invited &a{1} &eto the faction."),
    FACTION_INVITE_PLAYER("&eYou have been invited to join &9{0} &eby &a{1}&e."),
    FACTION_JOIN_LOCAL("&9{0}&e has joined the faction."),
    FACTION_JOIN_PLAYER("&eYou have joined &9{0}&e."),
    FACTION_CREATE_BROADCAST("&9{0}&e has been &acreated&e by &f{1}"),
    FACTION_CREATE_FINISH("&aYou created a faction.  For help, type /f help"),
    FACTION_DISBAND_BROADCAST("&9{0}&e has been &cdisbanded by &f{1}"),
    FACTION_NONE("&cYou are not in a faction."),
    FACTION_NONE_OTHER("&That player is not in a faction."),
    FACTION_INFO_HEADER_FOOTER("&7&m------------------------"),
    FACTION_INFO_DESCRIPTION("&9{0}&7 - &6${1} &7- \"&e{2}&7\""),
    FACTION_INFO_DTR("&eDTR&7: {0} &7[{1}&7] - &7[{2}&7]"),
    FACTION_INFO_HOME("&eHome&7: &9{0}"),
    FACTION_INFO_MEMBERS("&eMembers&7: {0}"),
    FACTION_INFO_ALLIES("&eAllies&7: {0}"),
    FACTION_INFO_KOTH_NAME("&9{0} &7- &6KOTH"),
    FACTION_INFO_KOTH_LOCATION("&eLocation&7: &9{0}"),
    FACTION_INFO_ROAD_NAME("&9{0} &7- &5ROAD"),
    FACTION_INFO_SPECIAL_NAME("&9{0} &7- &3Permanent"),
    FACTION_INFO_SPECIAL_LOCATION("&eLocation&7: &9{0}"),
    FACTION_INFO_SPAWN_NAME("&a{0}"),
    FACTION_INFO_SPAWN_LOCATION("&eLocation&7: &9{0}"),
    DEATHBAN_KICK("&cYou have been deathbanned.\\n&cYou have {0} lives, login again to use one.\\n&cYour deathban expires in {1}."),
    DEATHBAN_LIFE_USE("&aYou used one of your lives to revive yourself.  You now have {0} lives."),
    BORDER_BLOCK_DENY("&cYou cannot modify blocks beyond the world border."),
    BORDER_PORTAL("&cThat portal's location is past the border. You have been moved inwards."),
    BORDER_TELEPORT("&cThat location is past the border."),
    CHAT_FORMAT("&7[{0}&7] &a{1}&7: &f{2}"),//[Faction] player: hi
    CHAT_FORMAT_ALLY("&7(&9&lALLY&7) &8[&a{0}&8] &f{1}&7: &e{2}"),//(ALLY) [Faction] player: Hello
    CHAT_FORMAT_FACTION("&7(&a&lFACTION&7) &f{0}&7: &e{1}"),//(FACTION) player: hello
    ENDERPEARL_COOLDOWN("&cEnderPearl Cooldown: &c&l{0}s"),
    PVP_TIMER_START("&eYou are protected from pvp for {0}.  Type /pvp enable to remove your protection."),
    PVP_TIMER_NO_DAMAGE("&9{0} is protected from pvp."),
    PVP_TIMER_NO_ATTACK("&9You cannot attack while protected from pvp.  Type &e/pvp enable&9 to remove your protection."),
    PVP_TIMER_NOT_ACTIVE("&cYou do not have an active PvPTimer."),
    PVP_TIMER_NOT_ACTIVE_OTHER("&cPlayer '{0}' does not have an active PvPTimer."),
    PVP_TIMER_TIME_REMAINING("&eYour PvPTimer has &9{0}&e time remaining."),
    PVP_TIMER_REMOVE("&eYou are no longer protected from PvP."),
    PVP_TIMER_FORCE_ENABLE("&cYou forcefully enabled {0}'s PvPTimer."),
    FOUND_DIAMONDS_BROADCAST("[FD] &b{0} found {1}"),
    PLAYER_NOT_FOUND("&cPlayer '{0}' not found.")
    ;

    private final String defaultValue;

    FactionLang(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public static FactionLang fromString(String s){
        for(FactionLang lang : values()){
            if(lang.toString().equalsIgnoreCase(s)){
                return lang;
            }
        }
        return null;
    }

}
