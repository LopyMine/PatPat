package net.lopymine.patpat.modmenu.yacl.custom.base;

//? if >=1.20.1 {

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.ConfigCategory.Builder;
import net.minecraft.text.Text;

import net.lopymine.patpat.utils.ModMenuUtils;

@SuppressWarnings("unused")
public class SimpleCategory {

	private final Builder builder;

	private SimpleCategory(String categoryId) {
		String categoryKey = ModMenuUtils.getCategoryKey(categoryId);
		Text categoryName = ModMenuUtils.getName(categoryKey);
		this.builder = ConfigCategory.createBuilder().name(categoryName);
	}

	public static SimpleCategory startBuilder(String categoryId) {
		return new SimpleCategory(categoryId);
	}

	public SimpleCategory groups(OptionGroup... groups) {
		for (OptionGroup group : groups) {
			if (group == null) {
				continue;
			}
			this.builder.group(group);
		}
		return this;
	}

	public SimpleCategory options(Option<?>... options) {
		for (Option<?> option : options) {
			if (option == null) {
				continue;
			}
			this.builder.option(option);
		}
		return this;
	}

	public ConfigCategory build() {
		return this.builder.build();
	}
}
//?}
