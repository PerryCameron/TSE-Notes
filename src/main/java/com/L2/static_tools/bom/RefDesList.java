package com.L2.static_tools.bom;

import java.util.List;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/* --------------------------------------------------------------
   Helper adapter – turns a <refdeslist> into a single CSV string
   -------------------------------------------------------------- */
@XmlJavaTypeAdapter(RefDesListAdapter.class)
public class RefDesList {
    public final List<String> refdes;

    public RefDesList() { this.refdes = null; }
    public RefDesList(List<String> refdes) { this.refdes = refdes; }

    @Override public String toString() {
        return refdes == null ? "" : String.join(",", refdes);
    }
}
