/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.command.normal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonah Seguin on 12/13/2015.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class GCommandHandler {

    private Map<String, GCmdWrapper> commandMap = new HashMap<>();
    private CommandMap map;
    private Plugin plugin;

    public GCommandHandler(Plugin plugin) {
        this.plugin = plugin;
        if (plugin.getServer().getPluginManager() instanceof SimplePluginManager) {
            SimplePluginManager manager = (SimplePluginManager) plugin.getServer().getPluginManager();
            try {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                map = (CommandMap) field.get(manager);
            }
            catch (IllegalArgumentException | SecurityException | IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerCommands(GCommand command, String... commands) {
        for (Method method : command.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(GCmd.class)) {
                GCmd data = method.getAnnotation(GCmd.class);
                if (method.getParameterTypes().length != 1 || method.getParameterTypes()[0] != GCmdArgs.class) {
                    Bukkit.getLogger().warning("Could not register command '" + data.name() + "'.  Invalid parameters");
                    continue;
                }
                if (commands == null || commands.length == 0) {
                    registerCommand(command, method, data);
                }
                else if (commands.length > 0) {
                    for (String s : commands) {
                        if (data.name().equalsIgnoreCase(s)) {
                            registerCommand(command, method, data);
                        }
                    }
                }
            }
        }
    }

    public void registerCommand(final GCommand command, final Method method, GCmd data) {
        GCommandCaller caller = new GCommandCaller() {
            @Override
            public void call(GCmdArgs args) {
                try {
                    method.invoke(command, args);
                }
                catch (IllegalAccessException | InvocationTargetException ex) {
                    throw new GCommandException("Could not register command caller", ex);
                }
            }
        };

        GCmdWrapper wrapper = new GCmdWrapper(command, caller, data.name(), data.aliases(), data.usage(), data.description(), data.permission(), data.playerOnly(), data.minArgs());
        String commandName = wrapper.getName();
        commandMap.put(commandName, wrapper);
        commandMap.put(plugin.getName() + ":" + commandName, wrapper);//Add support for /<pluginName>:command format
        String bukkitCommandName = commandName.replace(" ", "$split").split("$split")[0].toLowerCase();
        if (map.getCommand(bukkitCommandName) == null) {
            org.bukkit.command.Command cmd = new BukkitCommand(bukkitCommandName) {
                @Override
                public boolean execute(CommandSender commandSender, String s, String[] args) {
                    handleCommand(commandSender, s, args);
                    return true;
                }
            };
            map.register(plugin.getName(), cmd);
        }
        if (!data.description().equals("")) {
            map.getCommand(bukkitCommandName).setDescription(data.description());
        }
        if (!data.usage().equals("")) {
            map.getCommand(bukkitCommandName).setUsage(data.usage());
        }
        if (!data.permission().equals("")) {
            map.getCommand(bukkitCommandName).setPermission(data.permission());
        }
        if (data.aliases().length > 0) {
            List<String> aliases = new ArrayList<>();
            for (String s : data.aliases()) {
                aliases.add(s);
            }
            map.getCommand(bukkitCommandName).setAliases(aliases);
        }
    }

    private void handleCommand(CommandSender sender, String label, String[] args) {
        for (int i = args.length; i >= 0; i--) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < i; x++) {
                buffer.append("." + args[x].toLowerCase());
            }
            String cmdLabel = buffer.toString();
            if (commandMap.containsKey(cmdLabel)) {
                GCmdWrapper wrapper = commandMap.get(cmdLabel);

                if (!wrapper.getPermission().equals("") && !sender.hasPermission(wrapper.getPermission())) {
                    sender.sendMessage(ChatColor.RED + "No permission.");
                    return;
                }

                String[] newArgs = new String[args.length - i];

                for (int x = 0, z = i; x < newArgs.length; x++, z++) {
                    newArgs[x] = args[z];
                }

                if (wrapper.getMinArgs() > newArgs.length) {
                    if (wrapper.getUsage().equals("")) {
                        sender.sendMessage(ChatColor.RED + "Invalid Usage.  Required arguments: " + wrapper.getMinArgs());
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Usage: " + wrapper.getUsage());
                    }
                    return;
                }

                GCmdArgs cmdArgs = new GCmdArgs(new GCommandSender(sender), newArgs);

                wrapper.execute(cmdArgs);
                break;
            }
        }
    }

}
