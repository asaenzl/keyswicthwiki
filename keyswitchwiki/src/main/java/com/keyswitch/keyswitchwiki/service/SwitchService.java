package com.keyswitch.keyswitchwiki.service;
import com.keyswitch.keyswitchwiki.entity.Switch;
import com.keyswitch.keyswitchwiki.entity.SwitchType;
import com.keyswitch.keyswitchwiki.repository.SwitchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service //marks as a service components
@RequiredArgsConstructor //Loombok generates for final fields (dependency injection)
@Transactional //ensures database operation are wrapped in transactions
public class SwitchService {


    private final SwitchRepository switchRepository;


    // Get all switches
    public List<Switch> getAllSwitches() {
        return switchRepository.findAll();
    }

    // Get switch by ID
    public Optional<Switch> getSwitchById(Long id) {
        return switchRepository.findById(id);
    }

    // Create or update switch
    public Switch saveSwitch(Switch switchEntity) {
        return switchRepository.save(switchEntity);
    }

    // Delete switch
    public void deleteSwitch(Long id) {
        switchRepository.deleteById(id);
    }

    // Search switches by model name
    public List<Switch> searchByModel(String model) {
        return switchRepository.findByModelContainingIgnoreCase(model);
    }

    // Search by brand
    public List<Switch> searchByBrand(String brand) {
        return switchRepository.findByBrandContainingIgnoreCase(brand);
    }

    // Filter by switch type
    public List<Switch> getBySwitchType(SwitchType switchType) {
        return switchRepository.findBySwitchType(switchType);
    }

    // Filter by actuation force range
    public List<Switch> getByForceRange(Integer minForce, Integer maxForce) {
        return switchRepository.findByActuationForceGramsBetween(minForce, maxForce);
    }

    // Get factory lubed switches
    public List<Switch> getFactoryLubedSwitches() {
        return switchRepository.findByIsFactoryLubed(true);
        }
}
