package net.lopymine.patpat.modmenu.yacl.custom.base;
//? if >=1.20.1 {

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.OptionGroup.Builder;
import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.text.Text;

import net.lopymine.patpat.utils.ModMenuUtils;

@SuppressWarnings("unused")
public class SimpleGroup {

	private final Builder groupBuilder;
	private final OptionDescription.Builder description;

	public SimpleGroup(String groupId) {
		String groupKey = ModMenuUtils.getGroupKey(groupId);
		Text groupName = ModMenuUtils.getName(groupKey);
		Text description = ModMenuUtils.getDescription(groupKey);

		this.groupBuilder = OptionGroup.createBuilder().name(groupName);
		this.description  = OptionDescription.createBuilder().text(description);
	}

	public static SimpleGroup startBuilder(String groupId) {
		return new SimpleGroup(groupId);
	}

	public SimpleGroup options(Option<?>... options) {
		for (Option<?> option : options) {
			if (option == null) {
				continue;
			}
			this.groupBuilder.option(option);
		}
		return this;
	}

	public SimpleGroup withCustomDescription(ImageRenderer renderer) {
		this.description.customImage(renderer);
		return this;
	}

	public OptionGroup build() {
		return this.groupBuilder.description(this.description.build()).build();
	}
}
//?}
