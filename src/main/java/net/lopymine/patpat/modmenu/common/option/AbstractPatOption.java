package net.lopymine.patpat.modmenu.common.option;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.lopymine.patpat.modmenu.common.PatDescription;
import net.lopymine.patpat.modmenu.common.PatElement;
import net.lopymine.patpat.modmenu.common.image.AbstractPatImage;
import net.lopymine.patpat.utils.ModMenuUtils;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

@Getter
@SuperBuilder
public abstract class AbstractPatOption<T> implements PatElement {

    private final Component name;
    private @Nullable PatDescription description;

	@SuppressWarnings("unused")
    public abstract static class AbstractPatOptionBuilder<T,
            C extends AbstractPatOption<T>,
            B extends AbstractPatOptionBuilder<T, C, B>> {

        public B key(String key) {
            this.name = ModMenuUtils.getOptionName(key);
            this.description = PatDescription.of(ModMenuUtils.getOptionDescription(key));
            return self();
        }

        public B addImage(AbstractPatImage image) {
            if(this.description != null) {
                this.description.setImage(image);
            }
            return self();
        }

    }

}
