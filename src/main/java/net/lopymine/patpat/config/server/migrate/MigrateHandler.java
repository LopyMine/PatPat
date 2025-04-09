package net.lopymine.patpat.config.server.migrate;

public interface MigrateHandler {

	String getVersion();

	boolean needMigrate();

	boolean migrate();

}
