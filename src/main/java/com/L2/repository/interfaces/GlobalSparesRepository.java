package com.L2.repository.interfaces;

import com.L2.dto.PartDTO;
import com.L2.dto.global_spares.*;

import java.util.List;
import java.util.Map;

public interface GlobalSparesRepository {

    int insertProductToSpare(ProductToSparesDTO productToSpares);

    int insertConsolidatedProductToSpare(ProductToSparesDTO productToSpares);

    int insertReplacementCr(ReplacementCrDTO replacementCrDTO);

    int insertWorkbookProperties(PropertiesDTO propertiesDTO);

    List<String> getDistinctSpareItems(boolean isArchived);

    List<SparesDTO> searchSpares(String searchTerm, int partOrderId);

    List<String> getRangesFromSpareItem(String spare, boolean isArchived);

    List<String> getProductsFromRange(String spare, String range, boolean isArchived);

    ProductToSparesDTO getProductToSpares(String spare, boolean isArchived);

    List<RangesDTO> findAllRanges();

    List<SparesDTO> searchSparesScoring(String[] keywords);

    List<SparesDTO> searchSparesScoringDual(String[] keywords1, String[] keywords2);
}
