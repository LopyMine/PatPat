package net.lopymine.patpat;

import lombok.*;

//? >=1.17 {
import org.slf4j.*;
//?} else {
/*import org.apache.logging.log4j.*;
 *///?}

public class PatLogger {

	@Getter
	private final Logger logger;
	private final String name;

	@Setter
	private boolean debugMode = false;

	public PatLogger(String name) {
		this.name = name;
		this.logger    = /*? if >=1.17 {*/LoggerFactory/*?} else {*//*LogManager*//*?}*/.getLogger(this.name);
	}

	public void debug(String text, Object... args) {
		if (this.debugMode) {
			text = "[%s/DEBUG]: %s".formatted(this.name, text);
			this.logger.info(text, args);
		}
	}

	public void info(String text, Object... args) {
		text = prepare(text);
		this.logger.info(text, args);
	}

	public void warn(String text, Object... args) {
		text = prepare(text);
		this.logger.warn(text, args);
	}

	public void error(String text, Object... args) {
		text = prepare(text);
		this.logger.error(text, args);
	}

	private String prepare(String text) {
		return "[%s]: %s".formatted(this.name, text);
	}
}