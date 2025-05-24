package com.L2.repository.interfaces;

import com.L2.dto.EntitlementFx;

import java.util.List;

public interface EntitlementsRepository {
    List<EntitlementFx> getAllEntitlements();

    // Insert a new entitlement row into the database
    int insertEntitlement(EntitlementFx entitlement);

    // Update an existing entitlement row based on its id
    int updateEntitlement(EntitlementFx entitlement);

    // Delete an entitlement row based on its id
    int deleteEntitlement(EntitlementFx entitlement);
}
