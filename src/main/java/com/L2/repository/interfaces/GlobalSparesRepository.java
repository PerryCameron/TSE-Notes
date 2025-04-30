package com.L2.repository.interfaces;

import com.L2.dto.global_spares.ProductToSparesDTO;
import com.L2.dto.global_spares.ReplacementCrDTO;

public interface GlobalSparesRepository {

    int insertProductToSpare(ProductToSparesDTO productToSpares);

    int insertReplacementCr(ReplacementCrDTO replacementCrDTO);
}
