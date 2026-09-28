package com.example.CropAdvisor.repository;

import com.example.CropAdvisor.entity.Officer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfficerRepository extends JpaRepository<Officer, Long> {

    List<Officer> findByRegionId(Long regionId);

    Optional<Officer> findFirstByRegionId(Long regionId);
}