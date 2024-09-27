package com.L2.repository.interfaces;

import com.L2.dto.EntitlementDTO;

import java.util.List;

public interface EntitlementsRepository {
    List<EntitlementDTO> getAllEntitlements();
}
