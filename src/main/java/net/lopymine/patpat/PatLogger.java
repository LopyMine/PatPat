package net.lopymine.patpat;

//? >=1.17 {
import org.slf4j.*;
//?}

public class PatLogger {

	//? >=1.17 {
	private final Logger logger;
	//?} else {
	/*private final String name;
	*///?}

	public PatLogger(String name){
		//? >=1.17 {
		logger = LoggerFactory.getLogger(name);
		//?} else {
		/*this.name = name;
		*///?}
	}

	public void info(String text, Object... args) {
		//? >=1.17 {
		logger.info(text, args);
		//?} else {
		/*System.out.println("[%s/INFO] ".formatted(name) + text.replace("{}", "%s").formatted(args));
		 *///?}
	}

	public void warn(String text, Object... args) {
		//? >=1.17 {
		logger.warn(text, args);
		//?} else {
		/*System.out.println("[%s/WARN] ".formatted(name) + text.replace("{}", "%s").formatted(args));
		 *///?}
	}

	public void error(String text, Object... args) {
		//? >=1.17 {
		logger.error(text, args);
		//?} else {
		/*System.out.println("[%s/ERROR] ".formatted(name) + text.replace("{}", "%s").formatted(args));
		 *///?}
	}
}