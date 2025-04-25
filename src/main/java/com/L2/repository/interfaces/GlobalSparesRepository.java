package com.L2.repository.interfaces;

import com.L2.dto.global_spares.ProductToSpares;

public interface GlobalSparesRepository {

    int insertProductToSpare(ProductToSpares productToSpares);
}
