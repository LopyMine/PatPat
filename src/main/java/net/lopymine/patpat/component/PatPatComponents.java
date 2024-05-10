package net.lopymine.patpat.component;

import dev.onyxstudios.cca.api.v3.component.*;
import dev.onyxstudios.cca.api.v3.entity.*;
import net.minecraft.entity.LivingEntity;

import net.lopymine.patpat.PatPat;

import org.jetbrains.annotations.NotNull;

public class PatPatComponents implements EntityComponentInitializer {
    public static final ComponentKey<PatPatComponent> PATPAT_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(PatPat.i(PatPatComponent.PATPAT_ID), PatPatComponent.class);

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.registerFor(LivingEntity.class, PATPAT_COMPONENT, PatPatComponent::new);
    }
}