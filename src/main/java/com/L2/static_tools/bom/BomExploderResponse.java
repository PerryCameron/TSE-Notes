package com.L2.static_tools.bom;

import com.L2.dto.bom.ComponentDTO;
import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "bomexploder_response")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "")  // This handles xmlns=""
public class BomExploderResponse {

    @XmlElement(name = "component")
    private List<ComponentDTO> rootComponents;

    public List<ComponentDTO> getRootComponents() { return rootComponents; }
}