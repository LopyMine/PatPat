package net.lopymine.patpat.server.ratelimit;

import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.server.config.sub.PatPatServerRateLimitConfig;

import java.util.TimerTask;

public class PatPatServerRateLimitTask extends TimerTask {

	private final PatPatServerRateLimitConfig config;

	public PatPatServerRateLimitTask() {
		this.config = PatPatServerConfig.getInstance().getRateLimitConfig();
	}

	@Override
	public void run() {
		PatPatServerRateLimitManager.addPats(this.config.getTokenIncrement());
	}
}
