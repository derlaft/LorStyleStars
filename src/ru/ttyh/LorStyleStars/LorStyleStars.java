package ru.ttyh.LorStyleStars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LorStyleStars extends JavaPlugin {
	
	  
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		LorStyleStarsSystem.disable();
	}
	
	public void onEnable() {
		LorStyleStarsSystem.setup();
		PluginManager pm = this.getServer().getPluginManager();

		pm.registerEvent(Type.PLAYER_LOGIN, new LorStyleStarsPlayerListener(), Priority.Normal, this);

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
						LorStyleStarsSystem.getScore(p.getName()) );
				return true;
			case 1:
				if (args[0].equalsIgnoreCase("stars") ) {
					p.sendMessage(ChatColor.YELLOW + "Stars mark: " +
							LorStyleStarsSystem.scoreToStars(p.getName()) );
					return true;
				} else if (args[0].equalsIgnoreCase("reload") ) {
					LorStyleStarsSystem.reload();
					p.sendMessage(ChatColor.YELLOW + "Config reloaded" );
					return true;
				} else return false;
			case 2:
				if (args[0].equalsIgnoreCase("get") && !args[1].equals(null) ) {
					p.sendMessage(ChatColor.YELLOW + args[1] + "'s score is " +
							LorStyleStarsSystem.getScore(args[1]) );
					return true;
				} else return false;
			case 3:
				try {
					Integer.parseInt(args[2]);
				} catch (NumberFormatException e){
					p.sendMessage(ChatColor.RED + "arg[2] is not a nuber!");
					return true;
				}
				if (! p.isOp() ) {
					p.sendMessage(ChatColor.RED + "You can't do it, you are now OP");
					return true;
				}
				if ( args[0].equalsIgnoreCase("set") ) {
					LorStyleStarsSystem.setScore(args[1], args[2]);
					Bukkit.getServer().broadcastMessage(
							ChatColor.GRAY + p.getName() + " sets " + 
							args[1] + "'s score to " + args[2]);
					return true;
				} else if ( args[0].equalsIgnoreCase("add")) {
					LorStyleStarsSystem.addScore(args[1], args[2]);
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
}