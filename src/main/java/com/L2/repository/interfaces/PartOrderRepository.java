package com.L2.repository.interfaces;

import com.L2.dto.PartOrderDTO;

import java.util.List;

public interface PartOrderRepository {

    int insertPartOrder(PartOrderDTO partOrder);

    List<PartOrderDTO> findAllPartOrdersByNoteId(int noteId);

    List<PartOrderDTO> findAllPartOrders();

    int updatePartOrder(PartOrderDTO partOrder);

    int deletePartOrder(PartOrderDTO partOrderDTO);
}
