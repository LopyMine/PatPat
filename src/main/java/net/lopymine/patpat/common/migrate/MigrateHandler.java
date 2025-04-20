package net.lopymine.patpat.common.migrate;

public interface MigrateHandler {

	String getVersion();

	boolean needMigrate();

	boolean migrate();

}
