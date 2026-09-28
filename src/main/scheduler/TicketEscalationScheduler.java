package com.example.CropAdvisor.scheduler;

import com.example.CropAdvisor.service.TicketService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TicketEscalationScheduler {

    private final TicketService ticketService;

    public TicketEscalationScheduler(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Scheduled(fixedRate = 3600000)
    public void checkForEscalation() {

        ticketService.escalateOldTickets();
    }
}