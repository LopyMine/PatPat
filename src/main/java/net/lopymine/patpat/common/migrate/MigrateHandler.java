package net.lopymine.patpat.common.migrate;

public interface MigrateHandler {

	String getMigrateVersion();

	boolean needMigrate();

	boolean migrate();


}
