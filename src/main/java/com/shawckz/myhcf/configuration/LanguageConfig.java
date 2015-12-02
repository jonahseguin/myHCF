package com.shawckz.myhcf.configuration;

import com.shawckz.myhcf.Factions;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class LanguageConfig extends Configuration {

    public LanguageConfig(Plugin plugin) {
        super(plugin, "lang.yml");
        load();
        save();

        for(FactionLang lang : FactionLang.values()){//Load default values into local memory hashmap cache
            if (!this.lang.containsKey(lang)) {
                this.lang.put(lang, lang.getDefaultValue());
            }
        }

        for(FactionLang key : lang.keySet()){//Save values into config that don't exist already
            if(!getConfig().contains(key.toString())){
                getConfig().set(key.toString(), lang.get(key));
            }
        }

        for(String key : getConfig().getKeys(false)){//Load lang values from config into local memory hashmap cache
            FactionLang lang = FactionLang.fromString(key);
            if(lang != null){
                this.lang.put(lang, getConfig().getString(key));
            }
        }
        save();

        if(Factions.isDebug()){
            Factions.log("Loaded Language File.");
            Factions.log("Language Values:");
            for(FactionLang key : lang.keySet()){
                Factions.log(key.toString()+": " + lang.get(key));
            }
        }

    }


    private final Map<FactionLang, String> lang = new HashMap<>();

    public String getFormattedLang(FactionLang lang, String... args) {
        if (!this.lang.containsKey(lang)) {
            this.lang.put(lang, lang.getDefaultValue());
        }
        String val = this.lang.get(lang);
        if (args != null) {
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    if(val.contains("{"+i+"}")){
                        val = val.replace("{" + i + "}", args[i]);
                    }
                }
            }
        }
        return ChatColor.translateAlternateColorCodes('&', val);
    }

}
