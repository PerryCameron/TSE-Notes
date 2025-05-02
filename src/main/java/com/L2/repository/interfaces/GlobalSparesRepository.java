package com.L2.repository.interfaces;

import com.L2.dto.PartDTO;
import com.L2.dto.global_spares.ProductToSparesDTO;
import com.L2.dto.global_spares.PropertiesDTO;
import com.L2.dto.global_spares.ReplacementCrDTO;

import java.util.List;
import java.util.Map;

public interface GlobalSparesRepository {

    int insertProductToSpare(ProductToSparesDTO productToSpares);

    int insertReplacementCr(ReplacementCrDTO replacementCrDTO);

    int insertWorkbookProperties(PropertiesDTO propertiesDTO);

    List<ProductToSparesDTO> searchSpares(String searchTerm, int partOrderId);
}
