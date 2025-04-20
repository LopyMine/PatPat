package net.lopymine.patpat.modmenu.yacl.custom.base;

//? if >=1.20.1 {

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.image.ImageRenderer;
import lombok.Getter;
import lombok.experimental.ExtensionMethod;

import net.lopymine.patpat.utils.ModMenuUtils;
import net.lopymine.patpat.modmenu.yacl.custom.extension.YACLAPIExtension;
import net.lopymine.patpat.modmenu.yacl.custom.utils.SimpleContent;

import java.util.List;
import java.util.function.*;

@SuppressWarnings("unused")
@ExtensionMethod(YACLAPIExtension.class)
public class SimpleOption {

	public static <T> Builder<T> startBuilder(String optionId) {
		return new Builder<>(optionId);
	}

	public static ButtonBuilder startButtonBuilder(String optionId, BiConsumer<YACLScreen, ButtonOption> biConsumer) {
		return new ButtonBuilder(optionId, biConsumer);
	}

	public static <T> ListOptionBuilder<T> startListBuilder(String optionId) {
		return new ListOptionBuilder<>(optionId);
	}

	public static class Builder<T> {

		private final String optionId;
		private final String optionKey;
		@Getter
		private final Option.Builder<T> optionBuilder;

		public Builder(String optionId) {
			this.optionId      = optionId;
			this.optionKey     = ModMenuUtils.getOptionKey(optionId);
			this.optionBuilder = Option.<T>createBuilder()
					.name(ModMenuUtils.getName(this.optionKey));
		}

		public Builder<T> withCustomDescription(ImageRenderer renderer) {
			OptionDescription.Builder builder = OptionDescription.createBuilder().customImage(renderer);
			this.optionBuilder.description(builder.build());
			return this;
		}

		public Builder<T> withDescription(SimpleContent content) {
			OptionDescription.Builder builder = OptionDescription.createBuilder().text(ModMenuUtils.getDescription(this.optionKey));
			if (content == SimpleContent.IMAGE) {
				builder.image(ModMenuUtils.getContentId(content, this.optionId), 500, 500);
			}
			if (content == SimpleContent.WEBP) {
				builder.webpImage(ModMenuUtils.getContentId(content, this.optionId));
			}
			this.optionBuilder.description(builder.build());
			return this;
		}

		public Builder<T> withBinding(Binding<T> binding, boolean instant) {
			this.optionBuilder.bindingE(binding, instant);
			return this;
		}

		public Builder<T> withBinding(T def, Supplier<T> getter, Consumer<T> setter, boolean instant) {
			this.optionBuilder.bindingE(Binding.generic(def, getter, setter), instant);
			return this;
		}

		public Option<T> build() {
			return this.optionBuilder.build();
		}
	}

	public static class ButtonBuilder {

		private final String optionId;
		private final String optionKey;
		@Getter
		private final ButtonOption.Builder optionBuilder;

		public ButtonBuilder(String optionId, BiConsumer<YACLScreen, ButtonOption> biConsumer) {
			this.optionId      = optionId;
			this.optionKey     = ModMenuUtils.getOptionKey(optionId);
			this.optionBuilder = ButtonOption.createBuilder()
					.name(ModMenuUtils.getName(this.optionKey))
					.action(biConsumer);
		}

		public ButtonBuilder withCustomDescription(ImageRenderer renderer) {
			OptionDescription.Builder builder = OptionDescription.createBuilder().customImage(renderer);
			this.optionBuilder.description(builder.build());
			return this;
		}

		public ButtonBuilder withDescription(SimpleContent content) {
			OptionDescription.Builder builder = OptionDescription.createBuilder().text(ModMenuUtils.getDescription(this.optionKey));
			if (content == SimpleContent.IMAGE) {
				builder.image(ModMenuUtils.getContentId(content, this.optionId), 500, 500);
			}
			if (content == SimpleContent.WEBP) {
				builder.webpImage(ModMenuUtils.getContentId(content, this.optionId));
			}
			this.optionBuilder.description(builder.build());
			return this;
		}

		public ButtonOption build() {
			return this.optionBuilder.build();
		}
	}

	public static class ListOptionBuilder<T> {

		private final String optionId;
		private final String optionKey;
		@Getter
		private final ListOption.Builder<T> optionBuilder;

		public ListOptionBuilder(String optionId) {
			this.optionId      = optionId;
			this.optionKey     = ModMenuUtils.getGroupKey(optionId);
			this.optionBuilder = ListOption.<T>createBuilder()
					.name(ModMenuUtils.getName(this.optionKey));
		}

		public ListOptionBuilder<T> withCustomDescription(ImageRenderer renderer) {
			OptionDescription.Builder builder = OptionDescription.createBuilder().customImage(renderer);
			this.optionBuilder.description(builder.build());
			return this;
		}

		public ListOptionBuilder<T> withDescription(SimpleContent content) {
			OptionDescription.Builder builder = OptionDescription.createBuilder().text(ModMenuUtils.getDescription(this.optionKey));
			if (content == SimpleContent.IMAGE) {
				builder.image(ModMenuUtils.getContentId(content, this.optionId), 500, 500);
			}
			if (content == SimpleContent.WEBP) {
				builder.webpImage(ModMenuUtils.getContentId(content, this.optionId));
			}
			this.optionBuilder.description(builder.build());
			return this;
		}

		public ListOptionBuilder<T> withBinding(Binding<List<T>> binding, boolean instant) {
			this.optionBuilder.bindingE(binding, instant);
			return this;
		}

		public ListOptionBuilder<T> withBinding(List<T> def, Supplier<List<T>> getter, Consumer<List<T>> setter, boolean instant) {
			this.optionBuilder.bindingE(Binding.generic(def, getter, setter), instant);
			return this;
		}
	}
}
//?}
