package net.lopymine.patpat.modmenu.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import net.lopymine.patpat.utils.ModMenuUtils;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
@Builder
public class PatCategory {

    @Singular("addElement") private List<PatElement> elements;
    private final Component name;
    private @Nullable Component description;

    public static class PatCategoryBuilder {

        public PatCategoryBuilder key(String key) {
            this.name = ModMenuUtils.getName(ModMenuUtils.getCategoryKey(key));
            this.description = ModMenuUtils.getDescription(ModMenuUtils.getCategoryKey(key));
            return this;
        }

    }

}
