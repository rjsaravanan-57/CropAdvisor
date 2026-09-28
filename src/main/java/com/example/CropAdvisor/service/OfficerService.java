package com.example.CropAdvisor.service;

import com.example.CropAdvisor.entity.Officer;
import com.example.CropAdvisor.entity.Region;
import com.example.CropAdvisor.exception.ResourceNotFoundException;
import com.example.CropAdvisor.repository.OfficerRepository;
import com.example.CropAdvisor.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfficerService {

    private final OfficerRepository officerRepository;
    private final RegionRepository regionRepository;

    public OfficerService(
            OfficerRepository officerRepository,
            RegionRepository regionRepository) {

        this.officerRepository = officerRepository;
        this.regionRepository = regionRepository;
    }

    public Officer createOfficer(Officer officer) {

        Long regionId = officer.getRegion().getId();

        Region region = regionRepository.findById(regionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Region not found with id: " + regionId));

        officer.setRegion(region);

        return officerRepository.save(officer);
    }

    public List<Officer> getAllOfficers() {
        return officerRepository.findAll();
    }

    public Officer getOfficerById(Long id) {

        return officerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Officer not found with id: " + id));
    }

    public List<Officer> getOfficersByRegion(Long regionId) {

        return officerRepository.findByRegionId(regionId);
    }

    public Officer updateOfficer(
            Long id,
            Officer updatedOfficer) {

        Officer existingOfficer = getOfficerById(id);

        existingOfficer.setName(updatedOfficer.getName());

        if (updatedOfficer.getRegion() != null) {

            Long regionId = updatedOfficer.getRegion().getId();

            Region region = regionRepository.findById(regionId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Region not found with id: " + regionId));

            existingOfficer.setRegion(region);
        }

        return officerRepository.save(existingOfficer);
    }

    public void deleteOfficer(Long id) {

        Officer officer = getOfficerById(id);

        officerRepository.delete(officer);
    }
}