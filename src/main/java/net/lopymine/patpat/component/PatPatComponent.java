package net.lopymine.patpat.component;

import dev.onyxstudios.cca.api.v3.component.sync.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Util;

import org.jetbrains.annotations.NotNull;

public class PatPatComponent implements AutoSyncedComponent {
    public static final String PATPAT_ID = "patpat";
    private final LivingEntity patEntity;
    private boolean shouldPat;
    private int duration;
    private long timeOfStart;
    private int frame;

    public PatPatComponent(LivingEntity patEntity) {
        this.patEntity = patEntity;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        this.duration = tag.getInt(PATPAT_ID);
        this.timeOfStart = Util.getMeasuringTimeMs();
        this.shouldPat = tag.getBoolean(PATPAT_ID);
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        tag.putInt(PATPAT_ID, this.duration);
        tag.putBoolean(PATPAT_ID, this.shouldPat);
    }

    public void pat() {
        System.out.println("pat");
        this.shouldPat = true;
        this.duration = 320;
        this.timeOfStart = Util.getMeasuringTimeMs();

        this.patEntity.syncComponent(PatPatComponents.PATPAT_COMPONENT, (this), PlayerSyncPredicate.all());
    }

    public void unPat() {
        System.out.println("unPat");
        this.shouldPat = false;
        this.duration = 0;
        this.timeOfStart = 0L;
    }

    public boolean shouldPat() {
        return this.shouldPat;
    }

    public int getDuration() {
        return this.duration;
    }

    public long getTimeOfStart() {
        return this.timeOfStart;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public int getFrame() {
        return this.frame;
    }
}
