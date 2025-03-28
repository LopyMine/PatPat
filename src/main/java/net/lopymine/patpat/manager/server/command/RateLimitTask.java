package net.lopymine.patpat.manager.server.command;

import net.lopymine.patpat.config.server.PatPatServerConfig;
import net.lopymine.patpat.config.server.RateLimitConfig;

import java.util.TimerTask;

public class RateLimitTask extends TimerTask {

	private final RateLimitConfig config;

	public RateLimitTask() {
		this.config = PatPatServerConfig.getInstance().getRateLimitConfig();

	}

	@Override
	public void run() {
		RateLimitManager.addPats(this.config.getTokenIncrement());
	}
}
