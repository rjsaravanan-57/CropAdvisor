package com.example.CropAdvisor.controller;

import com.example.CropAdvisor.entity.Region;
import com.example.CropAdvisor.service.RegionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    // Create region
    @PostMapping
    public ResponseEntity<Region> createRegion(
            @RequestBody Region region) {

        return ResponseEntity.ok(
                regionService.createRegion(region)
        );
    }

    // Get all regions
    @GetMapping
    public ResponseEntity<List<Region>> getAllRegions() {

        return ResponseEntity.ok(
                regionService.getAllRegions()
        );
    }

    // Get region by ID
    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                regionService.getRegionById(id)
        );
    }

    // Update region
    @PutMapping("/{id}")
    public ResponseEntity<Region> updateRegion(
            @PathVariable Long id,
            @RequestBody Region region) {

        return ResponseEntity.ok(
                regionService.updateRegion(id, region)
        );
    }

    // Delete region
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRegion(
            @PathVariable Long id) {

        regionService.deleteRegion(id);

        return ResponseEntity.ok(
                "Region deleted successfully"
        );
    }
}