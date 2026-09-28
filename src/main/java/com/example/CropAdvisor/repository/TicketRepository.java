package com.example.CropAdvisor.repository;

import com.example.CropAdvisor.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByFarmerId(Long farmerId);

    List<Ticket> findByOfficerId(Long officerId);

    List<Ticket> findByStatus(Ticket.Status status);

    List<Ticket> findByCreatedAtBeforeAndStatusNot(
            LocalDateTime time,
            Ticket.Status status
    );
}