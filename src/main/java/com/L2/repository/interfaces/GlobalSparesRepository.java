package com.L2.repository.interfaces;

import com.L2.dto.global_spares.ProductToSparesDTO;

public interface GlobalSparesRepository {

    int insertProductToSpare(ProductToSparesDTO productToSpares);
}
