package net.lopymine.patpat;

import org.jetbrains.annotations.Nullable;
import lombok.*;

//? if >=1.17 {
import org.slf4j.*;
//?} else {
/*import org.apache.logging.log4j.*;
*///?}

public class PatLogger {

	@Getter
	private final Logger logger;
	private final String name;

	@Nullable
	private final PatLogger parent;

	@Setter
	private boolean debugMode = false;

	public PatLogger(String name) {
		this.name   = name;
		this.logger = /*? if >=1.17 {*/LoggerFactory/*?} else {*//*LogManager*//*?}*/.getLogger(this.name);
		this.parent = null;
	}

	public PatLogger(String name, @Nullable PatLogger parent) {
		this.name   = name;
		this.logger = /*? if >=1.17 {*/LoggerFactory/*?} else {*//*LogManager*//*?}*/.getLogger(this.name);
		this.parent = parent;
	}

	public void debug(String text, Object... args) {
		if (isDebugModeEnabled()) {
			text = "[%s/DEBUG]: %s".formatted(this.name, text);
			this.logger.info(text, args);
		}
	}

	private boolean isDebugModeEnabled() {
		return this.debugMode || (this.parent != null && this.parent.isDebugModeEnabled());
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

	public PatLogger extend(String name) {
		return new PatLogger("%s/%s".formatted(this.name, name), this);
	}

	private String prepare(String text) {
		return "[%s]: %s".formatted(this.name, text);
	}
}