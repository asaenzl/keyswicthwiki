package com.keyswitch.keyswitchwiki.controller;

import com.keyswitch.keyswitchwiki.entity.Switch;
import com.keyswitch.keyswitchwiki.entity.SwitchType;
import com.keyswitch.keyswitchwiki.service.FileStorageService;
import com.keyswitch.keyswitchwiki.service.SwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.BeanUtils;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

@RestController
@RequestMapping("/api/switches")
@CrossOrigin(origins = "*")
public class SwitchController {

    private final SwitchService switchService;
    private final FileStorageService fileStorageService;

    @Autowired
    public SwitchController(SwitchService switchService, FileStorageService fileStorageService) {
        this.switchService = switchService;
        this.fileStorageService = fileStorageService;
    }

    // GET all switches
    @GetMapping
    public ResponseEntity<List<Switch>> getAllSwitches() {
        List<Switch> switches = switchService.getAllSwitches();
        return ResponseEntity.ok(switches);
    }

    // GET switch by ID
    @GetMapping("/{id}")
    public ResponseEntity<Switch> getSwitchById(@PathVariable Long id) {
        return switchService.getSwitchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Create new switch
    @PostMapping
    public ResponseEntity<Switch> createSwitch(@RequestBody Switch switchEntity) {
        Switch saved = switchService.saveSwitch(switchEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT - Update existing switch
    @PutMapping("/{id}")
    public ResponseEntity<Switch> updateSwitch(@PathVariable Long id, @RequestBody Switch switchEntity) {
        return switchService.getSwitchById(id)
                .map(existing -> {
                    // copy incoming properties onto the existing entity but keep the existing id
                    BeanUtils.copyProperties(switchEntity, existing, "id");
                    Switch updated = switchService.saveSwitch(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE switch
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSwitch(@PathVariable Long id) {
        if (switchService.getSwitchById(id).isPresent()) {
            switchService.deleteSwitch(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Search by model
    @GetMapping("/search/model")
    public ResponseEntity<List<Switch>> searchByModel(@RequestParam String query) {
        List<Switch> switches = switchService.searchByModel(query);
        return ResponseEntity.ok(switches);
    }

    // Search by brand
    @GetMapping("/search/brand")
    public ResponseEntity<List<Switch>> searchByBrand(@RequestParam String query) {
        List<Switch> switches = switchService.searchByBrand(query);
        return ResponseEntity.ok(switches);
    }

    // Filter by type
    @GetMapping("/filter/type/{type}")
    public ResponseEntity<List<Switch>> filterByType(@PathVariable SwitchType type) {
        List<Switch> switches = switchService.getBySwitchType(type);
        return ResponseEntity.ok(switches);
    }

    // Filter by force range
    @GetMapping("/filter/force")
    public ResponseEntity<List<Switch>> filterByForce(
            @RequestParam Integer min,
            @RequestParam Integer max) {
        List<Switch> switches = switchService.getByForceRange(min, max);
        return ResponseEntity.ok(switches);
    }

    // Upload image for a switch
    @PostMapping("/{id}/image")
    public ResponseEntity<Switch> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        return switchService.getSwitchById(id)
                .map(switchEntity -> {
                    try {
                        // Delete old image if exists
                        if (switchEntity.getImagePath() != null) {
                            fileStorageService.deleteImage(switchEntity.getImagePath());
                        }

                        // Store new image
                        String filename = fileStorageService.storeImage(file);
                        switchEntity.setImagePath(filename);
                        Switch updated = switchService.saveSwitch(switchEntity);

                        return ResponseEntity.ok(updated);
                    } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Switch>build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Get image for a switch
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        return switchService.getSwitchById(id)
                .filter(s -> s.getImagePath() != null)
                .map(switchEntity -> {
                    try {
                        Path imagePath = fileStorageService.loadImage(switchEntity.getImagePath());
                        byte[] imageBytes = Files.readAllBytes(imagePath);

                        return ResponseEntity.ok()
                                .contentType(org.springframework.http.MediaType.IMAGE_JPEG)
                                .body(imageBytes);
                    } catch (IOException e) {
                        return ResponseEntity.notFound().<byte[]>build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
