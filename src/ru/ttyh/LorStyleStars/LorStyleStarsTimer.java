package ru.ttyh.LorStyleStars;

import java.util.TimerTask;

public class LorStyleStarsTimer extends TimerTask {
	private long time = 0;
	LorStyleStarsSystem system;
	LorStyleStarsTimer(LorStyleStarsSystem sys) {
		system = sys;
	}
	@Override
	public void run() {
		time++;
		if (time == 1000000)
			time =0; 
		system.heal(time);
	}

}