package com.L2.repository.interfaces;

import com.L2.dto.global_spares.SparePictureDTO;
import com.L2.dto.global_spares.SparesDTO;

public interface ChangeSetRepository {
    int insertSpare(SparesDTO sparesDTO);

    void insertSparePicture(SparePictureDTO sparePicture);
}
