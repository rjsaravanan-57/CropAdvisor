package com.example.CropAdvisor.service;

import com.example.CropAdvisor.entity.Region;
import com.example.CropAdvisor.exception.ResourceNotFoundException;
import com.example.CropAdvisor.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public Region createRegion(Region region) {
        return regionRepository.save(region);
    }

    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    public Region getRegionById(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Region not found with id: " + id));
    }

    public Region updateRegion(Long id, Region updatedRegion) {
        Region existingRegion = getRegionById(id);

        existingRegion.setName(updatedRegion.getName());

        return regionRepository.save(existingRegion);
    }

    public void deleteRegion(Long id) {
        Region region = getRegionById(id);

        regionRepository.delete(region);
    }
}