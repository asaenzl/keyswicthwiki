package com.keyswitch.keyswitchwiki.repository;

import com.keyswitch.keyswitchwiki.entity.Switch;
import com.keyswitch.keyswitchwiki.entity.SwitchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SwitchRepository extends JpaRepository<Switch, Long> {

    // Find by brand
    List<Switch> findByBrand(String brand);

    // Find by switch type
    List<Switch> findBySwitchType(SwitchType switchType);

    // Find by brand and model (exact match)
    Switch findByBrandAndModel(String brand, String model);

    // Search by model name containing text (case-insensitive)
    List<Switch> findByModelContainingIgnoreCase(String model);

    // Find by brand containing text (case-insensitive)
    List<Switch> findByBrandContainingIgnoreCase(String brand);

    // Find switches within actuation force range
    List<Switch> findByActuationForceGramsBetween(Integer minForce, Integer maxForce);

    // Find factory lubed switches
    List<Switch> findByIsFactoryLubed(Boolean isLubed);
}
