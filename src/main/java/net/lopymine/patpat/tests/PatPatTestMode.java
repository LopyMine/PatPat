package net.lopymine.patpat.tests;

public class PatPatTestMode {

	public static final float FROZEN_PROGRESS = 0.5F;

	private static boolean syntheticInput;

	private PatPatTestMode() {
		throw new IllegalStateException("Mode class");
	}

	public static boolean isEnabled() {
		return PatPatTestAgent.isEnabled();
	}

	public static boolean isRealInputBlocked() {
		return isEnabled() && !syntheticInput;
	}

	public static void runSyntheticInput(Runnable runnable) {
		syntheticInput = true;
		try {
			runnable.run();
		} finally {
			syntheticInput = false;
		}
	}

	public static boolean isFrozen(float progress, int duration) {
		return progress >= (duration * FROZEN_PROGRESS);
	}
}
