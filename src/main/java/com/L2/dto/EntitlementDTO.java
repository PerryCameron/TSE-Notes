package com.L2.dto;

import java.io.Serializable;

public class EntitlementDTO implements Serializable {
    int id;
    String name;
    String includes;
    String notIncludes;

    public EntitlementDTO(int id, String name, String includes, String notIncludes) {
        this.id = id;
        this.name = name;
        this.includes = includes;
        this.notIncludes = notIncludes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIncludes() {
        return includes;
    }

    public void setIncludes(String includes) {
        this.includes = includes;
    }

    public String getNotIncludes() {
        return notIncludes;
    }

    public void setNotIncludes(String notIncludes) {
        this.notIncludes = notIncludes;
    }

    @Override
    public String toString() {
        return "EntitlementDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", includes='" + includes + '\'' +
                ", notIncludes='" + notIncludes + '\'' +
                '}';
    }
}
