package com.example.CropAdvisor.controller;

import com.example.CropAdvisor.entity.Farmer;
import com.example.CropAdvisor.service.FarmerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmers")
public class FarmerController {

    private final FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    // Create farmer
    @PostMapping
    public ResponseEntity<Farmer> createFarmer(
            @RequestBody Farmer farmer) {

        return ResponseEntity.ok(
                farmerService.createFarmer(farmer)
        );
    }

    // Get all farmers
    @GetMapping
    public ResponseEntity<List<Farmer>> getAllFarmers() {

        return ResponseEntity.ok(
                farmerService.getAllFarmers()
        );
    }

    // Get farmer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Farmer> getFarmerById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                farmerService.getFarmerById(id)
        );
    }

    // Get farmers by region
    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<Farmer>> getFarmersByRegion(
            @PathVariable Long regionId) {

        return ResponseEntity.ok(
                farmerService.getFarmersByRegion(regionId)
        );
    }

    // Update farmer
    @PutMapping("/{id}")
    public ResponseEntity<Farmer> updateFarmer(
            @PathVariable Long id,
            @RequestBody Farmer farmer) {

        return ResponseEntity.ok(
                farmerService.updateFarmer(id, farmer)
        );
    }

    // Delete farmer
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFarmer(
            @PathVariable Long id) {

        farmerService.deleteFarmer(id);

        return ResponseEntity.ok(
                "Farmer deleted successfully"
        );
    }
}