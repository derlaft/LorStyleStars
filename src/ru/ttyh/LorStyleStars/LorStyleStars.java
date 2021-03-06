package ru.ttyh.LorStyleStars;

import java.util.Timer;


import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.plugin.Plugin;

// TODO sqlite
// TODO Take Chat Control
// TODO Multiworld support

public class LorStyleStars extends JavaPlugin {
	static LorStyleStarsSystem system;
	
	private Timer timer = new Timer(true);
	
	public static PermissionHandler perms;
	@Override
	public void onDisable() {
		system.disable();
	}
	
	public void onEnable() {
		system = new LorStyleStarsSystem();
		system.setup();
		setupPermissions();
		PluginManager pm = this.getServer().getPluginManager();
		LorStyleStarsPlayerListener listener = new LorStyleStarsPlayerListener(system);
		pm.registerEvent(Type.PLAYER_JOIN, listener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_CHAT, listener, Priority.Normal, this);
		pm.registerEvent(Type.ENTITY_REGAIN_HEALTH, new LorStyleStarsEntity(), Priority.Normal, this);
		timer.schedule(new LorStyleStarsTimer(system), 0, (long) (100));
	}

	@Override 
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (! (sender instanceof Player) ) {
			return false;
		}
		Player p = (Player) sender;

		if (command.getName().equalsIgnoreCase("score") ) {
			switch (args.length) {
			case 0:
				p.sendMessage(ChatColor.YELLOW + "Your score is " +
						system.getScore(p.getName()) );
				return true;
			case 1:
				if (args[0].equalsIgnoreCase("stars") ) {
					p.sendMessage(ChatColor.YELLOW + "Stars mark: " +
							system.scoreToStars(p.getName()) );
					return true;
				} else if (args[0].equalsIgnoreCase("reload") ) {
					if (notHave(p, "manage")) 
						return false;
					system.reload();
					p.sendMessage(ChatColor.YELLOW + "Config reloaded" );
					return true;
				} else  if (args[0].equalsIgnoreCase("save") ) {
					if (notHave(p, "manage")) 
						return false;
					system.save();
					p.sendMessage(ChatColor.YELLOW + "Config saved" );
					return true;
				} else if (args[0].equalsIgnoreCase("help") ) {
					return help(p);
				} else return false;
			case 2:
				if (args[0].equalsIgnoreCase("get")  ) {
					if (p.getName() == args[1] || !notHave(p, "eye") )
							p.sendMessage(ChatColor.YELLOW + args[1] + "'s score is " +
							system.getScore(args[1]) );
					return true;
				} else return false;
			case 3:
				if (notHave(p, "manage")) 
					return false;
				try {
					Integer.parseInt(args[2]);
				} catch (NumberFormatException e){
					p.sendMessage(ChatColor.RED + "arg[2] is not a nuber!");
					return true;
				}
				if ( args[0].equalsIgnoreCase("set") ) {
					system.setScore(args[1], args[2]);
					Bukkit.getServer().broadcastMessage(
							ChatColor.GRAY + p.getName() + " sets " + 
							args[1] + "'s score to " + args[2]);
					return true;
				} else if ( args[0].equalsIgnoreCase("add")) {
					system.addScore(args[1], args[2]);
					if ( Integer.parseInt(args[2]) > 0 )
						Bukkit.getServer().broadcastMessage(
								ChatColor.GREEN + p.getName() + " gives " + 
								args[1] + " " + args[2] + " score" );
					else
						Bukkit.getServer().broadcastMessage(
								ChatColor.RED + p.getName() + " takes from " + 
								args[1] + " " + Math.abs(Integer.parseInt
										(args[2])) + " score");
					return true;
				} else return false;
			}
		}
		return false;
	}
	private void setupPermissions() {
	    if (perms != null) {
	        return;
	    }
	    
	    Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
	    
	    if (permissionsPlugin == null) {
	        System.out.println("LSS: Permission system not detected, defaulting to OP");
	        return;
	    }
	    
	    perms = ((Permissions) permissionsPlugin).getHandler();
	    System.out.println("LSS: Found and will use plugin "+((Permissions)permissionsPlugin).getDescription().getFullName());
	}
	private boolean help(Player p) {
		p.sendMessage(ChatColor.YELLOW + "LorStyleScore commands:" );
		p.sendMessage("/score - show your score" );
		p.sendMessage("/score stars - show your stars" );
		p.sendMessage("/score help - show this page" );
		if (!notHave(p, "eye")) {
			p.sendMessage(ChatColor.YELLOW+ "LorStyleScore watchers commands:" );
			p.sendMessage("/score get <username> - get score of username" );
		}
		if (!notHave(p, "manage")) {
			p.sendMessage(ChatColor.YELLOW+ "LorStyleScore managers commands:" );
			p.sendMessage("/score save - force save config" );
			p.sendMessage("/score reload - reload Fconfig (without save)" );
			p.sendMessage("/score set <username> <count> - set score of username" );
			p.sendMessage("/score add <username> <+/-><count> - add or remove score" );
			
		}
		return true;
	}
	private boolean notHave(Player p, String node) {
		  if (!LorStyleStars.perms.has(p, "score." + node)) {
		      return true;
		  }
		return false;
	}
	
	}