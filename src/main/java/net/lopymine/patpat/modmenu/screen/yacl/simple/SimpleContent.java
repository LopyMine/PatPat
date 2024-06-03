package net.lopymine.patpat.modmenu.screen.yacl.simple;

import lombok.Getter;

@Getter
public enum SimpleContent {
	NONE("none", "none"),
	IMAGE("image", "png"),
	WEBP("webp", "webp");

	private final String folder;
	private final String fileExtension;

	SimpleContent(String folder, String fileExtension) {
		this.folder = folder;
		this.fileExtension = fileExtension;
	}
}
