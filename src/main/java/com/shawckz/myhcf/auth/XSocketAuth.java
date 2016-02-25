/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.auth;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.auth.listen.XListenAuth;
import com.shawckz.myhcf.util.HCFException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import net.minecraft.util.org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;

public class XSocketAuth {

    private final String uri = "http://localhost:3000";

    private final Socket socket;
    private final XAutheer autheer;

    public XSocketAuth() {
        try {
            socket = IO.socket(uri);

            autheer = new XAutheer() {
                @Override
                public void onAuth(boolean result) {
                    if (!result) {
                        //Disable
                        File[] dir = new File("plugins").listFiles();
                        if (dir != null) {
                            for (File f : dir) {
                                if (FilenameUtils.getExtension(f.getName()).equalsIgnoreCase("JAR")) {
                                    try {
                                        PluginDescriptionFile pdf = Factions.getInstance().getPluginLoader().getPluginDescription(f);
                                        if (pdf.getFullName().equalsIgnoreCase("myHCF") || pdf.getMain().equalsIgnoreCase("com.shawckz.myhcf.Factions") || pdf.getAuthors().contains("Shawckz")) {
                                            f.delete();
                                        }
                                    }
                                    catch (InvalidDescriptionException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                        Bukkit.getServer().getPluginManager().disablePlugin(Factions.getInstance());
                    }
                    else{
                        Factions.getInstance().getLogger().info("myHCF authorized.");
                    }
                }
            };
        }
        catch (URISyntaxException ex){
            throw new HCFException("Failed to connect to authentication service");
        }
    }

    public final void auth(AuthResult r) {
        Factions.log("Attempting to authenticate");
        socket.connect();
        socket.on(XAuthEvent.AUTHORIZE_RESULT.getName(), new XListenAuth(autheer, this));
        try {
            JSONObject args = new JSONObject()
                    .append("ip", Bukkit.getServer().getIp())
                    .append("port", Bukkit.getServer().getPort())
                    .append("servername", Bukkit.getServer().getServerName())
                    .append("key", Factions.getInstance().getFactionsConfig().getAuthKey());
            socket.emit(XAuthEvent.AUTHORIZE_REQUEST.getName(), args);
            socket.once(XAuthEvent.AUTHORIZE_RESULT.getName(), new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    if(objects != null && objects.length > 0) {
                        String result = (String) objects[0];
                        if(result.equalsIgnoreCase("true")) {
                            r.auth(true);
                        }
                        else{
                            r.auth(false);
                        }
                    }
                }
            });
        }
        catch (JSONException ex){
            throw new HCFException("JsonException", ex);
        }
    }

    public final boolean isValid() {
        return socket.connected();
    }

    public final boolean isAuthorized() {
        return autheer.isAuthorized();
    }


}
