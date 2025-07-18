package net.lopymine.mossy.multi;

import java.util.Objects;

public record MultiVersion(String projectVersion, String minVersion, String maxVersion) {

	@Override
	public String toString() {
		if (!this.minVersion().equals(this.maxVersion())) {
			return "%s[%s-%s]".formatted(this.projectVersion(), this.minVersion(), this.maxVersion());
		}
		return "%s[%s]".formatted(this.projectVersion(), this.maxVersion());
	}

	public String toVersionRange() {
		if (Objects.equals(this.minVersion(), this.maxVersion())) {
			return this.maxVersion();
		}

		int aMin = this.minVersion().indexOf(".");
		int bMin = this.minVersion().lastIndexOf(".");
		if (aMin == bMin) {
			bMin = this.minVersion().length();
		}
		String minMain = this.minVersion().substring(0, bMin);

		int aMax = this.maxVersion().indexOf(".");
		int bMax = this.maxVersion().lastIndexOf(".");
		if (aMax == bMax) {
			bMax = this.maxVersion().length();
		}

		String maxMain = this.maxVersion().substring(0, bMax);
		String maxMinor = this.maxVersion().substring(bMax+1);
		if (minMain.equals(maxMain)) {
			return "%s-%s".formatted(this.minVersion(), maxMinor);
		}
		return "%s-%s".formatted(this.minVersion(), this.maxVersion());
	}

	public boolean minIsMax() {
		return this.minVersion().equals(this.maxVersion());
	}

}
