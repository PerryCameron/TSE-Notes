package com.L2.repository.interfaces;

import com.L2.dto.global_spares.*;
import java.util.List;

public interface GlobalSparesRepository {

    List<SparesDTO> searchSparesByPartNumber(String searchTerm, int partOrderId);
    List<RangesFx> findAllRanges();
    List<SparesDTO> searchSparesScoring(String[] keywords);
    List<SparesDTO> searchSparesScoringSingleKeyword(String keyword);
    List<SparesDTO> searchSparesWithRange(String[] keywords1, String[] keywords2);
    int countSparesByRanges(String[] ranges);
    int updateSpare(SparesDTO sparesDTO);
    int deleteRange(RangesFx range);
    byte[] getImage(String spareItem);
    SparesDTO findBySpareItem(String spareItem);

    int insertSpare(SparesDTO sparesDTO);

    List<SparesDTO> findSparesUpdatedWithinDays(int days);
}
