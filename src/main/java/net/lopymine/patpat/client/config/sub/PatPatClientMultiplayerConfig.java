package net.lopymine.patpat.client.config.sub;

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

	public static final PatPatClientMultiplayerConfig DEFAULT = new PatPatClientMultiplayerConfig(
			false,
			true,
			ListMode.DISABLED
	);

	public static final Codec<PatPatClientMultiplayerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("bypassServerResourcePackPriorityEnabled", DEFAULT.bypassServerResourcePackPriorityEnabled, Codec.BOOL, PatPatClientMultiplayerConfig::isBypassServerResourcePackPriorityEnabled),
			option("patMeEnabled", DEFAULT.patMeEnabled, Codec.BOOL, PatPatClientMultiplayerConfig::isPatMeEnabled),
			option("listMode", DEFAULT.listMode, ListMode.CODEC, PatPatClientMultiplayerConfig::getListMode)
	).apply(instance, PatPatClientMultiplayerConfig::new));

	@Setter(value = AccessLevel.PRIVATE)
	private boolean bypassServerResourcePackPriorityEnabled;
	private boolean patMeEnabled;
	private ListMode listMode;

	public static PatPatClientMultiplayerConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

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

	public PatPatClientMultiplayerConfig copy() {
		return new PatPatClientMultiplayerConfig(
				this.bypassServerResourcePackPriorityEnabled,
				this.patMeEnabled,
				this.listMode
		);
	}
}
