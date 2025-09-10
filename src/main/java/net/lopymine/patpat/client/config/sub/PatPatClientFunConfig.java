package net.lopymine.patpat.client.config.sub;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Supplier;
import lombok.*;
import net.lopymine.patpat.PatTranslation;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.config.EnumWithText;
import net.lopymine.patpat.utils.CodecUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.*;
import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientFunConfig {

	public static final Codec<PatPatClientFunConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("pvpMode", PvpMode.DISABLED, PvpMode.CODEC, PatPatClientFunConfig::getPvpMode)
	).apply(instance, PatPatClientFunConfig::new));

	private PvpMode pvpMode;

	public static Supplier<PatPatClientFunConfig> getNewInstance() {
		return () -> CodecUtils.parseNewInstanceHacky(CODEC);
	}

	public enum PvpMode implements StringRepresentable, EnumWithText {

		DISABLED,
		EMPTY_HAND,
		NOT_EMPTY_HAND,
		ALWAYS;

		public static final Codec<PvpMode> CODEC = StringRepresentable.fromEnum(PvpMode::values/*? if <=1.18.2 {*//*, PvpMode::byName *//*?}*/);

		@Nullable
		public static PvpMode byName(String name) {
			try {
				return PvpMode.valueOf(name.toUpperCase());
			} catch (Exception e) {
				PatPatClient.LOGGER.error("Failed to get enum by name \"{}\":", name, e);
				return null;
			}
		}

		@Override
		public @NotNull String getSerializedName() {
			return this.name().toLowerCase();
		}

		@Override
		public Component getText() {
			return PatTranslation.text("modmenu.option.pvp_mode." + this.getSerializedName());
		}
	}

}
