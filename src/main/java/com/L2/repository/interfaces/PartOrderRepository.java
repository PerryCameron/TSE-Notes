package com.L2.repository.interfaces;

import com.L2.dto.PartFx;
import com.L2.dto.PartOrderFx;

import java.util.List;

public interface PartOrderRepository {

    int insertPartOrder(PartOrderFx partOrder);

    List<PartOrderFx> findAllPartOrdersByNoteId(int noteId);

    List<PartOrderFx> findAllPartOrders();

    int updatePartOrder(PartOrderFx partOrder);

    int deletePartOrder(PartOrderFx partOrderDTO);

    int insertPart(PartFx partDTO);

    int updatePart(PartFx partDTO);

    int deletePart(PartFx partDTO);

    List<PartFx> getPartsByPartOrder(PartOrderFx partOrderDTO);
}
