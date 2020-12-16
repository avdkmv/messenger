package com.unn.controller;

import java.util.Optional;
import com.unn.model.Ticket;
import com.unn.service.TicketService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TicketPageController {
    private final TicketService ticketService;

    @GetMapping("/ticket/{id}")
    public String openTicket(@PathVariable("id") String id, Authentication auth, Model model) {
        Optional<Ticket> ticket = ticketService.findByIdSecured(id, auth);
        if (ticket.isPresent()) {
            model.addAttribute("creator", ticket.get().getCreator());

            model.addAttribute("ticketId", ticket.get().getId());
            model.addAttribute("chatId", ticket.get().getChat().getId());

            model.addAttribute("ticketName", ticket.get().getName());
            model.addAttribute("chatName", ticket.get().getChat().getName());

            model.addAttribute("description", ticket.get().getDescription());
            return "ticket";
        }

        return "redirect:/dashboard";
    }
}
