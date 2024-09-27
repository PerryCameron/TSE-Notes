package com.L2.repository.interfaces;

import com.L2.dto.EntitlementDTO;

import java.util.List;

public interface EntitlementsRepository {
    List<EntitlementDTO> getAllEntitlements();

    // Insert a new entitlement row into the database
    int insertEntitlement(EntitlementDTO entitlement);

    // Update an existing entitlement row based on its id
    int updateEntitlement(EntitlementDTO entitlement);

    // Delete an entitlement row based on its id
    int deleteEntitlement(EntitlementDTO entitlement);
}
