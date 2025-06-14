package net.lopymine.patpat.modmenu.yacl.custom.base;
//? if >=1.20.1 {

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.OptionGroup.Builder;
import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.lopymine.patpat.utils.ModMenuUtils;

import net.minecraft.network.chat.*;

@SuppressWarnings("unused")
public class SimpleGroup {

	public static final Style NAME_STYLE = Style.EMPTY.withBold(true);

	private final Builder groupBuilder;
	private final OptionDescription.Builder description;

	public SimpleGroup(String groupId) {
		String groupKey = ModMenuUtils.getGroupKey(groupId);
		Component groupName = ModMenuUtils.getName(groupKey).withStyle(NAME_STYLE);
		Component description = ModMenuUtils.getDescription(groupKey);

		this.groupBuilder = OptionGroup.createBuilder().name(groupName);
		this.description  = OptionDescription.createBuilder().text(description);
	}

	public static MutableComponent getGroupName(String groupId) {
		return ModMenuUtils.getName(ModMenuUtils.getGroupKey(groupId)).withStyle(NAME_STYLE);
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
