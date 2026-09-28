package com.example.CropAdvisor.repository;

import com.example.CropAdvisor.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    List<Farmer> findByRegionId(Long regionId);
}