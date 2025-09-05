package net.lopymine.patpat.client.config.sub;

import java.util.function.Supplier;
import lombok.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.utils.CodecUtils;
import net.minecraft.client.Minecraft;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientMultiplayerConfig {

	public static final Codec<PatPatClientMultiplayerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("bypassServerResourcePackPriorityEnabled", false, Codec.BOOL, PatPatClientMultiplayerConfig::isBypassServerResourcePackPriorityEnabled),
			option("patMeEnabled", true, Codec.BOOL, PatPatClientMultiplayerConfig::isPatMeEnabled),
			option("listMode", ListMode.DISABLED, ListMode.CODEC, PatPatClientMultiplayerConfig::getListMode)
	).apply(instance, PatPatClientMultiplayerConfig::new));

	@Setter(value = AccessLevel.PRIVATE)
	private boolean bypassServerResourcePackPriorityEnabled;
	private boolean patMeEnabled;
	private ListMode listMode;

	public static Supplier<PatPatClientMultiplayerConfig> getNewInstance() {
		return () -> CodecUtils.parseNewInstanceHacky(CODEC);
	}

	@SuppressWarnings("ConstantConditions")
	public void setBypassServerResourcePackEnabled(boolean value) {
		this.bypassServerResourcePackPriorityEnabled = value;
		Minecraft client = Minecraft.getInstance();
		if (client == null) {
			return;
		}
		boolean bl = client.getResourcePackRepository()
				.getSelectedIds()
				.stream()
				.anyMatch(s -> s.startsWith("server/"));

		if (bl) {
			client.reloadResourcePacks();
		}
	}

}
