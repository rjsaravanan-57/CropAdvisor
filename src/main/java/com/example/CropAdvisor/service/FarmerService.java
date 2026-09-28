package com.example.CropAdvisor.service;

import com.example.CropAdvisor.entity.Farmer;
import com.example.CropAdvisor.entity.Region;
import com.example.CropAdvisor.exception.ResourceNotFoundException;
import com.example.CropAdvisor.repository.FarmerRepository;
import com.example.CropAdvisor.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmerService {

    private final FarmerRepository farmerRepository;
    private final RegionRepository regionRepository;

    public FarmerService(
            FarmerRepository farmerRepository,
            RegionRepository regionRepository) {

        this.farmerRepository = farmerRepository;
        this.regionRepository = regionRepository;
    }

    public Farmer createFarmer(Farmer farmer) {

        Long regionId = farmer.getRegion().getId();

        Region region = regionRepository.findById(regionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Region not found with id: " + regionId));

        farmer.setRegion(region);

        return farmerRepository.save(farmer);
    }

    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }

    public Farmer getFarmerById(Long id) {

        return farmerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Farmer not found with id: " + id));
    }

    public List<Farmer> getFarmersByRegion(Long regionId) {

        return farmerRepository.findByRegionId(regionId);
    }

    public Farmer updateFarmer(
            Long id,
            Farmer updatedFarmer) {

        Farmer existingFarmer = getFarmerById(id);

        existingFarmer.setName(updatedFarmer.getName());
        existingFarmer.setPhone(updatedFarmer.getPhone());

        if (updatedFarmer.getRegion() != null) {

            Long regionId = updatedFarmer.getRegion().getId();

            Region region = regionRepository.findById(regionId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Region not found with id: " + regionId));

            existingFarmer.setRegion(region);
        }

        return farmerRepository.save(existingFarmer);
    }

    public void deleteFarmer(Long id) {

        Farmer farmer = getFarmerById(id);

        farmerRepository.delete(farmer);
    }
}