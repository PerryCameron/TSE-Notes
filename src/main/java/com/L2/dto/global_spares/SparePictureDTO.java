package com.L2.dto.global_spares;

public class SparePictureDTO {
    private long id;
    private String spareName;
    private byte[] picture;

    public SparePictureDTO(long id, String spareName, byte[] picture) {
        this.id = id;
        this.spareName = spareName;
        this.picture = picture;
    }

    public SparePictureDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSpareName() {
        return spareName;
    }

    public void setSpareName(String spareName) {
        this.spareName = spareName;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}