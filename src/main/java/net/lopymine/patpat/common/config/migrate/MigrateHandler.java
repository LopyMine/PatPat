package net.lopymine.patpat.common.config.migrate;

public interface MigrateHandler {

	String getMigrateVersion();

	boolean needMigrate();

	boolean migrate();

}
