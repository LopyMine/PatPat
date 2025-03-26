package net.lopymine.patpat.config.server.migrate;

import java.util.*;

public class MigrateServerConfigManager {

	private static final Set<MigrateHandler> HANDLERS = new HashSet<>();

	private MigrateServerConfigManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void onInitialize() {
		HANDLERS.clear();
		addHandlers(new MigrateVersion0());
	}

	public static void migrate() {
		for (MigrateHandler handler : HANDLERS) {
			handler.migrate();
		}
	}

	private static void addHandlers(MigrateHandler... handlers) {
		HANDLERS.addAll(List.of(handlers));
	}


}
