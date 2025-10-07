package com.L2.repository.interfaces;

public interface SettingsRepository {
    boolean isSpellCheckEnabled();

    String getTheme();

    String setTheme(String theme);

    void setSpellCheckEnabled(boolean enabled);
}
