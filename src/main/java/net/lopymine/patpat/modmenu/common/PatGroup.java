package net.lopymine.patpat.modmenu.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import net.lopymine.patpat.modmenu.common.option.AbstractPatOption;
import net.lopymine.patpat.utils.ModMenuUtils;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
@Builder
public class PatGroup implements PatElement {

    @Singular("addOption") private List<AbstractPatOption<?>> options;
    private final Component name;
    private @Nullable PatDescription description;
    private boolean collapsed;

    public static class PatGroupBuilder {

        public PatGroupBuilder key(String key) {
            this.name = ModMenuUtils.getName(ModMenuUtils.getGroupKey(key));
            this.description = PatDescription.builder().text(ModMenuUtils.getDescription(ModMenuUtils.getGroupKey(key))).build();
            return this;
        }

    }

}
