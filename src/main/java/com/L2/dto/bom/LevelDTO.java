package com.L2.dto.bom;

public class LevelDTO {
    int level;
    int partCount;

    public LevelDTO(int level, int partCount) {
        this.level = level;
        this.partCount = partCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPartCount() {
        return partCount;
    }

    public void setPartCount(int partCount) {
        this.partCount = partCount;
    }
}
