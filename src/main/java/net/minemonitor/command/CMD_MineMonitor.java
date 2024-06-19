package net.minemonitor.command;

import net.minemonitor.Main;
import net.minemonitor.MineMonitorApi;
import net.minemonitor.plugin.Guis;
import net.minemonitor.plugin.Permissions;
import net.minemonitor.message.MessageKey;
import mcapi.davidout.manager.command.ICommand;
import mcapi.davidout.manager.language.MessageManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CMD_MineMonitor implements ICommand {


    @Override
    public String getName() {
        return "minemonitor";
    }

    @Override
    public List<String> getAliasList() {
        return Arrays.asList("mm", "mmonitor");
    }

    @Override
    public boolean executeCommand(CommandSender commandSender, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(MessageManager.getInstance().getMessage(MessageKey.ONLY_PLAYER));
            return false;
        }

        if(args.length == 0) {
            commandSender.sendMessage(MessageManager.getInstance().getMessage(MessageKey.USE_COMMAND).replace("%command", "/minemonitor [settings, setup, auth]"));
            return true;
        }

        Player p = (Player) commandSender;
        String cmd = args[0];

        if(!cmd.equalsIgnoreCase("setup") && MineMonitorApi.getInstance().getSetupManager().isInSetup()) {
            commandSender.sendMessage(MessageManager.getInstance().getMessage(MessageKey.SETUP_REQUIRED));
            return true;
        }

        if(cmd.equalsIgnoreCase("setup")) {
            if(!commandSender.hasPermission(Permissions.SETUP.getPermission())) {
                commandSender.sendMessage(MessageManager.getInstance().getMessage(MessageKey.NO_PERMISSION));
                return false;
            }

            MineMonitorApi.getInstance().getSetupManager().runSetup(Main.getInstance().getDataFolder());
            Guis.SETUP_GUI.openInventory(p);
            return true;
        }



        if(cmd.equalsIgnoreCase("auth")) {
            if(!commandSender.hasPermission(Permissions.AUTHENTICATE.getPermission())) {
                commandSender.sendMessage(MessageManager.getInstance().getMessage(MessageKey.NO_PERMISSION));
                return false;
            }


            p.spigot().sendMessage(
                    getAuthenticateMessage()
            );
            return true;
        }

        if(!commandSender.hasPermission(Permissions.SEE_CONFIG.getPermission())) {
            commandSender.sendMessage(MessageManager.getInstance().getMessage(MessageKey.NO_PERMISSION));
            return false;
        }

        Guis.SETTINGS_GUI.openInventory((Player) commandSender);
        return true;
    }

    @Override
    public List<String> autoComplete(CommandSender commandSender, String s, String[] args) {
        List<String> possible = Arrays.asList("settings", "setup", "auth");

        if(args.length == 0 || args[0] == null || args[0].isEmpty()) {
            return possible;
        }

        String arg = args[0];
        return possible.stream().filter(ca -> ca.toLowerCase().startsWith(arg.toLowerCase())).collect(Collectors.toList());
    }

    private BaseComponent[] getAuthenticateMessage() {
        String websiteUrl = "https://" + Main.getInstance().getDescription().getWebsite();
        String serverId = UUID.randomUUID().toString();
        String token = Main.getInstance().getConfigManager().getConnectionConfig().token;
        return getCombinedTextComponent(websiteUrl, serverId, token);
    }

    private BaseComponent[] getCombinedTextComponent(String websiteUrl, String serverId, String token) {
        TextComponent website = new TextComponent("Website");
        website.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, websiteUrl));
        website.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                MessageManager.getInstance().getMessage(MessageKey.WEBSITE_GO_TO)
        ).create()));
        website.setColor(ChatColor.YELLOW);

        TextComponent serverIdText = new TextComponent("ServerId");
        serverIdText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, serverId));
        serverIdText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                MessageManager.getInstance().getMessage(MessageKey.CLICK_COPY)
        ).create()));
        serverIdText.setColor(ChatColor.YELLOW);

        TextComponent tokenText = new TextComponent("Token");
        tokenText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, token));
        tokenText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                MessageManager.getInstance().getMessage(MessageKey.CLICK_COPY)
        ).create()));
        tokenText.setColor(ChatColor.YELLOW);

        return new BaseComponent[] {
                new TextComponent(MessageManager.getInstance().getMessage(MessageKey.AUTHENTICATE)),
                new TextComponent(" "),
                website,
                new TextComponent(" | "),
                serverIdText,
                new TextComponent(" | "),
                tokenText
        };

    }
}
