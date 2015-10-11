package com.shawckz.myhcf.configuration;

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
    }

    private final Map<FactionLang, String> lang = new HashMap<>();

    public String getFormattedLang(FactionLang lang, String... args){
        if(!this.lang.containsKey(lang)){
            this.lang.put(lang, lang.getDefaultValue());
        }
        String val = this.lang.get(lang);
        if(args != null){
            if(args.length > 0){
                for(int i = 0; i < args.length; i++){
                    val = val.replaceAll("{"+i+"}", args[i]);
                }
            }
        }
        return ChatColor.translateAlternateColorCodes('&', val);
    }

}
