package ru.ttyh.LorStyleStars;

import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public class LorStyleStarsEntity extends EntityListener {

	public void onEntityRegainHealth(EntityRegainHealthEvent event) {
		if (event.getRegainReason() == RegainReason.REGEN )
			event.setCancelled(true);
		}
	
}
