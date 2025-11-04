package com.L2.static_tools.bom;   // ← SAME PACKAGE

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;

public class RefDesListAdapter extends XmlAdapter<RefDesListWrapper, RefDesList> {

    @Override
    public RefDesList unmarshal(RefDesListWrapper v) {
        return v == null || v.refdes == null
                ? new RefDesList()
                : new RefDesList(new ArrayList<>(v.refdes));
    }

    @Override
    public RefDesListWrapper marshal(RefDesList v) { return null; }
}