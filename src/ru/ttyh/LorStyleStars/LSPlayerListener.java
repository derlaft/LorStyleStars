package ru.ttyh.LorStyleStars;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class LSPlayerListener extends PlayerListener {

	 public void onPlayerJoin(PlayerJoinEvent event) {
		 String name = event.getPlayer().getName();
		 LSSystem.updScore(name);
	 }
	 public void onPlayerChat(PlayerChatEvent event) {
	     if (LSSystem.getScore(event.getPlayer().getName()) <= 0) {
	     event.getPlayer().sendMessage(ChatColor.RED + "You have no score.");
	      event.setCancelled(true);
	     }
	    }
 
}
