package net.lopymine.patpat;

//? >=1.17 {
import org.slf4j.*;
 //?} else {
/*import org.apache.logging.log4j.*;
*///?}

public class PatLogger {

	private final Logger logger;

	public PatLogger(String name) {
		logger = /*? if >=1.17 {*/LoggerFactory/*?} else {*//*LogManager*//*?}*/.getLogger(name);
	}

	public void info(String text, Object... args) {
		logger.info(text, args);
	}

	public void warn(String text, Object... args) {
		logger.warn(text, args);
	}

	public void error(String text, Object... args) {
		logger.error(text, args);
	}
}