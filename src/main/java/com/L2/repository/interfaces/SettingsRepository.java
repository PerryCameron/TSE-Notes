package com.L2.repository.interfaces;

public interface SettingsRepository {
    boolean isSpellCheckEnabled();

    void setSpellCheckEnabled(boolean enabled);
}
