package com.example.CropAdvisor.controller;

import com.example.CropAdvisor.entity.Officer;
import com.example.CropAdvisor.service.OfficerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/officers")
public class OfficerController {

    private final OfficerService officerService;

    public OfficerController(OfficerService officerService) {
        this.officerService = officerService;
    }

    // Create officer
    @PostMapping
    public ResponseEntity<Officer> createOfficer(
            @RequestBody Officer officer) {

        return ResponseEntity.ok(
                officerService.createOfficer(officer)
        );
    }

    // Get all officers
    @GetMapping
    public ResponseEntity<List<Officer>> getAllOfficers() {

        return ResponseEntity.ok(
                officerService.getAllOfficers()
        );
    }

    // Get officer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Officer> getOfficerById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                officerService.getOfficerById(id)
        );
    }

    // Get officers by region
    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<Officer>> getOfficersByRegion(
            @PathVariable Long regionId) {

        return ResponseEntity.ok(
                officerService.getOfficersByRegion(regionId)
        );
    }

    // Update officer
    @PutMapping("/{id}")
    public ResponseEntity<Officer> updateOfficer(
            @PathVariable Long id,
            @RequestBody Officer officer) {

        return ResponseEntity.ok(
                officerService.updateOfficer(id, officer)
        );
    }

    // Delete officer
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOfficer(
            @PathVariable Long id) {

        officerService.deleteOfficer(id);

        return ResponseEntity.ok(
                "Officer deleted successfully"
        );
    }
}