package com.example.CropAdvisor.controller;

import com.example.CropAdvisor.entity.Ticket;
import com.example.CropAdvisor.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Create ticket
    @PostMapping
    public ResponseEntity<Ticket> createTicket(
            @RequestBody Ticket ticket) {

        return ResponseEntity.ok(
                ticketService.createTicket(ticket)
        );
    }

    // Get all tickets
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {

        return ResponseEntity.ok(
                ticketService.getAllTickets()
        );
    }

    // Get ticket by ID
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ticketService.getTicketById(id)
        );
    }

    // Get tickets by farmer
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<Ticket>> getTicketsByFarmer(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                ticketService.getTicketsByFarmer(farmerId)
        );
    }

    // Get tickets by officer
    @GetMapping("/officer/{officerId}")
    public ResponseEntity<List<Ticket>> getTicketsByOfficer(
            @PathVariable Long officerId) {

        return ResponseEntity.ok(
                ticketService.getTicketsByOfficer(officerId)
        );
    }

    // Get tickets by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Ticket>> getTicketsByStatus(
            @PathVariable Ticket.Status status) {

        return ResponseEntity.ok(
                ticketService.getTicketsByStatus(status)
        );
    }

    // Add recommendation
    @PutMapping("/{ticketId}/recommendation")
    public ResponseEntity<Ticket> addRecommendation(
            @PathVariable Long ticketId,
            @RequestParam Long officerId,
            @RequestBody String recommendation) {

        return ResponseEntity.ok(
                ticketService.addRecommendation(
                        ticketId,
                        officerId,
                        recommendation
                )
        );
    }

    // Close ticket
    @PutMapping("/{ticketId}/close")
    public ResponseEntity<Ticket> closeTicket(
            @PathVariable Long ticketId,
            @RequestParam Long officerId) {

        return ResponseEntity.ok(
                ticketService.closeTicket(
                        ticketId,
                        officerId
                )
        );
    }

    // Reopen ticket
    @PutMapping("/{ticketId}/reopen")
    public ResponseEntity<Ticket> reopenTicket(
            @PathVariable Long ticketId,
            @RequestParam Long farmerId) {

        return ResponseEntity.ok(
                ticketService.reopenTicket(
                        ticketId,
                        farmerId
                )
        );
    }

    // Manually trigger escalation check
    @PutMapping("/escalate")
    public ResponseEntity<String> escalateTickets() {

        ticketService.escalateOldTickets();

        return ResponseEntity.ok(
                "Old tickets checked and escalated"
        );
    }
}