package com.L2.dto.global_spares;

public class ReplacementCrDTO {
    String item;
    String replacement;
    String comment;
    double old_qty;
    double new_qty;

    public ReplacementCrDTO() {
    }

    public void clear() {
        this.item = "";
        this.replacement = "";
        this.comment = "";
        this.old_qty = 0;
        this.new_qty = 0;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getOld_qty() {
        return old_qty;
    }

    public void setOld_qty(double old_qty) {
        this.old_qty = old_qty;
    }

    public double getNew_qty() {
        return new_qty;
    }

    public void setNew_qty(double new_qty) {
        this.new_qty = new_qty;
    }

    @Override
    public String toString() {
        return "ReplacementCR{" +
                "item='" + item + '\'' +
                ", replacement='" + replacement + '\'' +
                ", comment='" + comment + '\'' +
                ", old_qty=" + old_qty +
                ", new_qty=" + new_qty +
                '}';
    }
}
