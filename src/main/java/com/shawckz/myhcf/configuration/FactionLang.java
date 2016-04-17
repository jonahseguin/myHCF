package com.shawckz.myhcf.configuration;

public enum FactionLang {

    FACTION_INVITE_LOCAL("&9{0}&e has invited &a{1} &eto the faction."),
    FACTION_INVITE_PLAYER("&eYou have been invited to join &9{0} &eby &a{1}&e."),
    FACTION_JOIN_LOCAL("&9{0}&e joined the faction."),
    FACTION_JOIN_PLAYER("&eYou joined the faction &9{0}&e."),
    FACTION_LEAVE_LOCAL("&9{0}&e left the faction."),
    FACTION_LEAVE_PLAYER("&eYou left your faction &9{0}&e."),
    FACTION_CREATE_BROADCAST("&9{0}&e has been &acreated&e by &f{1}"),
    FACTION_CREATE_FINISH("&aYou created a faction.  For help, type /f help"),
    FACTION_TAG_BROADCAST("&9{0}&e is now known as &9{1}&e."),
    FACTION_TAG_LOCAL("&9{0}&e has renamed the faction to '&9{1}&e'."),
    FACTION_DISBAND_BROADCAST("&9{0}&e has been &cdisbanded by &f{1}"),
    FACTION_DISBAND_FINISH("&eYou disbanded the faction '{0}'."),
    FACTION_NONE("&cYou are not in a faction."),
    FACTION_SETHOME_LOCAL("&9{0}&e has updated the faction's home."),
    FACTION_SETHOME_CLAIM("&cYou can only set your faction's home in your own claimed land."),
    FACTION_HOME_NONE("&cYour faction does not have a home."),
    FACTION_RALLY_NONE("&cYour faction does not have a rally."),
    FACTION_SETRALLY_LOCAL("&9{0}&e has updated the faction's rally."),
    FACTION_NO_KICK_LEADER("&cYou cannot kick the leader of your faction."),
    FACTION_NO_KICK_SELF("&cYou cannot kick yourself from your faction."),
    FACTION_NO_KICK_MOD("&cOnly the faction leader can kick moderators."),
    FACTION_ALREADY_IN("&cYou are already in a faction."),
    FACTION_ALREADY_IN_OTHER("&cPlayer '{0}' is already in a faction."),
    FACTION_INVITED_ALREADY("&cPlayer '{0}' has already been invited to the faction."),
    FACTION_NOT_SAME("&cPlayer '{0}' is not in your faction."),
    FACTION_NONE_OTHER("&cThat player is not in a faction."),
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
    PLAYER_NOT_IN_FACTION("&cPlayer '{0}' is not in a faction."),
    FACTION_JOIN_NOT_INVITED("&cYou are not invited to the faction '{0}'."),
    FACTION_JOIN_FULL("&cThe faction '{0}' is full."),
    FACTION_KICK_LOCAL("&9{0}&e has been kicked from the faction by &9{1)&e."),
    FACTION_KICK_PLAYER("&eYou were kicked from your faction (&9{0}&e) by &9{1}&e."),
    FACTION_LEADER_LEAVE("&cYou cannot leave the faction because you are the leader, use /f disband or make someone else the leader instead."),
    FACTION_CMD_MOD_ONLY("&cOnly faction moderators can do this."),
    FACTION_DESC_LENGTH("&cFaction description cannot be longer than 32 characters."),
    FACTION_DESC_LOCAL("&9{0}&e has changed the faction's description to '&9{1}&e'"),
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
    PLAYER_NOT_FOUND("&cPlayer '{0}' not found."),
    PLAYER_FACTION_NOT_FOUND("&cPlayer or faction '{0}' not found."),
    ARMOR_CLASS_EQUIP("&aEquipped &eArmor Class &7--> &9{0}"),
    ARMOR_CLASS_REMOVE("&cRemoved &eArmor Class &7--> &9{0}"),
    ENDER_CHEST_DISABLED("&cEnder Chests are disabled."),
    BLAZE_SPAWNER_DISABLED("&cDestroying Blaze Spawners is disabled in this world."),
    DEATH_MESSAGE("&9{0}&e died."),
    DEATH_MESSAGE_PLAYER("&9{0} &ewas killed by &9{1}&e."),
    PLAYER_TELEPORT_MOVED("&cYou moved. &7Teleport cancelled."),
    PLAYER_TELEPORT("&eYou will teleport in &9{0} seconds&e.  &cDon't move."),
    FACTION_LEADER_ONLY("&cOnly the faction leader can do this."),
    FACTION_PROMOTE_NOT_MEMBER("&cPlayer '{0}' is already a faction moderator."),
    FACTION_DEMOTE_NOT_MOD("&cPlayer '{0}' is not a faction moderator."),
    FACTION_PROMOTE_LOCAL("&9{0}&e has been promoted to faction moderator by &9{1}&e."),
    FACTION_DEMOTE_LOCAL("&9{0}&e has been demoted by &9{1}&e."),
    FACTION_LEADER_LOCAL("&9{0}&e has been promoted to faction leader by &9{1}&e."),
    FACTION_STUCK_TELEPORT("&eDon't move more than 5 blocks.  You will be unstuck in &c&l{0}&e."),
    FACTION_STUCK_TELEPORT_UPDATE("&eYou will be unstuck in &c&l{0}&e."),
    FACTION_STUCK_COMBAT("&cYou cannot use /f stuck while in combat."),
    FACTION_STUCK_NOT_STUCK("&cYou are not in enemy land."),
    FACTION_ALLY_ACCEPT("&eYour faction is now allies with &9{0}&e."),
    FACTION_ALLY_REQUEST_TARGET("&eThe faction '&9{0}&e' has requested to &aally&e.  To accept, type &b/f ally {0}&e."),
    FACTION_ALLY_REQUEST_LOCAL("&eYour faction has requested to ally the faction '&9{0}&e'."),
    FACTION_ENEMY_LOCAL("&9{0}&e has set your faction to &cenemy &ewith the faction &e'&9{1}&e'."),
    FACTION_ENEMY_TARGET("&eThe faction '&9{0}&e' has &cenemied&e your faction."),
    FACTION_ENEMY_ALREADY("&cYour faction is already enemy to the faction '{0}'."),
    FACTION_ALLY_ALREADY("&cYour faction is already allied to the faction '{0}'."),
    FACTION_LIST("&7{0}. &9{1} &7- &e(&a{2}&e/{3}&e) &7- &bDTR: &a{4}"),
    FACTION_UPDATE_DTR("&eYour faction's DTR was updated to &b{0}&e by &9{1}&e."),
    FACTION_UPDATE_DTR_SENDER("&eYou updated the dtr of faction '&9{0}&e' to &b{1}&e."),
    FACTION_UNFREEZE("&eYour faction's DTR was unfrozen by &9{0}&e."),
    FACTION_UNFREEZE_SENDER("&eYou unfroze the DTR of the faction '&9{0}&e'."),
    FACTION_CLAIM_CANCEL("&eLand claiming cancelled."),
    FACTION_CLAIM_NOT_ENOUGH_MONEY("&cYour faction does not have enough money to purchase this claim (${0})."),
    FACTION_CLAIM_TOO_SMALL("&cThat claim is too small.  Must be at least {0} by {0}."),
    FACTION_CLAIM_PURCHASE("&9{0}&e purchased &a${0}&e worth of land for your faction."),
    FACTION_DEPOSIT("&9{0}&e has deposited &a${1}&e into the faction balance."),
    FACTION_DEPOSIT_INSUFFICIENT_FUNDS("&cYou do not have enough money."),
    FACTION_WITHDRAW_INSUFFICIENT_FUNDS("&cYour faction does not have enough money."),
    FACTION_WITHDRAW("&9{0}&e has withdrawn &a${1}&e from the faction balance."),
    FACTION_CLAIM_ID("&eThe ID of this claim is &9{0}"),
    FACTION_UNCLAIM_ALL("&9{0}&e unclaimed all of the faction's land."),
    FACTION_UNCLAIM_NOT_FOUND("&cA claim with that ID owned by your faction was not found."),
    FACTION_UNCLAIM("&9{0}&e has unclaimed claim &a#{1}&e."),
    SPAWN_TAG("&7You are now &cspawn-tagged&7.  Do not logout."),
    LIMITER_REMOVED_ENCHANTS("&7We detected you had some illegal enchantments and have removed them."),
    LIMITER_POTION("&7That potion is not allowed."),
    PVP_TIMER_NOT_ALLOWED("&cYou cannot do this with an active PvPTimer.  Type /pvp enable to remove your PvPTimer.")
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
