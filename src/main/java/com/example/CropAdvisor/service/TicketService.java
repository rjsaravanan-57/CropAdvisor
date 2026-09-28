package com.example.CropAdvisor.service;

import com.example.CropAdvisor.entity.Farmer;
import com.example.CropAdvisor.entity.Officer;
import com.example.CropAdvisor.entity.Ticket;
import com.example.CropAdvisor.exception.InvalidTicketOperationException;
import com.example.CropAdvisor.exception.ResourceNotFoundException;
import com.example.CropAdvisor.repository.FarmerRepository;
import com.example.CropAdvisor.repository.OfficerRepository;
import com.example.CropAdvisor.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final FarmerRepository farmerRepository;
    private final OfficerRepository officerRepository;

    public TicketService(
            TicketRepository ticketRepository,
            FarmerRepository farmerRepository,
            OfficerRepository officerRepository) {

        this.ticketRepository = ticketRepository;
        this.farmerRepository = farmerRepository;
        this.officerRepository = officerRepository;
    }

    public Ticket createTicket(Ticket ticket) {

        Long farmerId = ticket.getFarmer().getId();

        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Farmer not found with id: " + farmerId));

        ticket.setFarmer(farmer);

        Long regionId = farmer.getRegion().getId();

        Officer officer = officerRepository
                .findFirstByRegionId(regionId)
                .orElse(null);

        if (officer != null) {

            ticket.setOfficer(officer);
            ticket.setStatus(Ticket.Status.ASSIGNED);

        } else {

            ticket.setStatus(Ticket.Status.OPEN);
        }

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {

        return ticketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket not found with id: " + id));
    }

    public List<Ticket> getTicketsByFarmer(Long farmerId) {

        return ticketRepository.findByFarmerId(farmerId);
    }

    public List<Ticket> getTicketsByOfficer(Long officerId) {

        return ticketRepository.findByOfficerId(officerId);
    }

    public List<Ticket> getTicketsByStatus(
            Ticket.Status status) {

        return ticketRepository.findByStatus(status);
    }

    public Ticket addRecommendation(
            Long ticketId,
            Long officerId,
            String recommendation) {

        Ticket ticket = getTicketById(ticketId);

        Officer officer = officerRepository.findById(officerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Officer not found with id: " + officerId));

        if (ticket.getOfficer() == null ||
                !ticket.getOfficer().getId().equals(officerId)) {

            throw new InvalidTicketOperationException(
                    "This officer is not assigned to this ticket");
        }

        ticket.setRecommendation(recommendation);

        ticket.setStatus(Ticket.Status.IN_PROGRESS);

        return ticketRepository.save(ticket);
    }

    public Ticket closeTicket(
            Long ticketId,
            Long officerId) {

        Ticket ticket = getTicketById(ticketId);

        if (ticket.getOfficer() == null ||
                !ticket.getOfficer().getId().equals(officerId)) {

            throw new InvalidTicketOperationException(
                    "Only the assigned officer can close this ticket");
        }

        ticket.setStatus(Ticket.Status.CLOSED);

        return ticketRepository.save(ticket);
    }

    public Ticket reopenTicket(
            Long ticketId,
            Long farmerId) {

        Ticket ticket = getTicketById(ticketId);

        if (!ticket.getFarmer().getId().equals(farmerId)) {

            throw new InvalidTicketOperationException(
                    "Only the farmer who created the ticket can reopen it");
        }

        ticket.setStatus(Ticket.Status.REOPENED);

        ticket.setOfficer(null);

        Long regionId =
                ticket.getFarmer().getRegion().getId();

        Officer officer = officerRepository
                .findFirstByRegionId(regionId)
                .orElse(null);

        if (officer != null) {

            ticket.setOfficer(officer);
            ticket.setStatus(Ticket.Status.ASSIGNED);
        }

        return ticketRepository.save(ticket);
    }

    public void escalateOldTickets() {

        LocalDateTime cutoffTime =
                LocalDateTime.now().minusHours(48);

        List<Ticket> oldTickets =
                ticketRepository.findByCreatedAtBeforeAndStatusNot(
                        cutoffTime,
                        Ticket.Status.CLOSED
                );

        for (int i = 0; i < oldTickets.size(); i++) {

            Ticket ticket = oldTickets.get(i);

            ticket.setStatus(Ticket.Status.ESCALATED);

            ticketRepository.save(ticket);
        }
    }
}