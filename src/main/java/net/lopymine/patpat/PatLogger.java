package net.lopymine.patpat;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;

import lombok.Getter;

//? >=1.17 {
import org.slf4j.*;
//?} else {
/*import org.apache.logging.log4j.*;
 *///?}

public class PatLogger {

	@Getter
	private final Logger logger;
	private final String name;

	public PatLogger(String name) {
		this.name = name;
		logger    = /*? if >=1.17 {*/LoggerFactory/*?} else {*//*LogManager*//*?}*/.getLogger(this.name);
	}

	public void debug(String text, Object... args) {
		PatPatClientConfig config = PatPatClient.getConfig();
		if (config != null && config.isDebugLogEnabled()) {
			text = "[%s/DEBUG]: %s".formatted(this.name, text);
			logger.info(text, args);
		}
	}

	public void info(String text, Object... args) {
		text = prepare(text);
		logger.info(text, args);

	}

	public void warn(String text, Object... args) {
		text = prepare(text);
		logger.warn(text, args);
	}

	public void error(String text, Object... args) {
		text = prepare(text);
		logger.error(text, args);
	}

	private String prepare(String text) {
		return "[%s]: %s".formatted(this.name, text);
	}
}