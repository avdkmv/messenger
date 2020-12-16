package com.unn.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.unn.dto.TicketRequest;
import com.unn.model.Chat;
import com.unn.model.Ticket;
import com.unn.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketService {
    private Map<String, Ticket> tickets = new ConcurrentHashMap<>();

    private final UserService userService;

    public void createTicket(final TicketRequest ticketRequest, final User user, final Chat chat) {
        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID().toString());
        ticket.setCreator(user.getUsername());
        ticket.setName(ticketRequest.getTicketName());
        ticket.setDescription(ticketRequest.getDescription());
        ticket.setChat(chat);
        ticket.addUser(user);
        ticket.generateLink();
        if (!this.tickets.containsKey(ticket.getName())) {
            this.tickets.put(ticket.getId(), ticket);
        }
    }

    public List<Ticket> allTickets() {
        return new ArrayList<>(this.tickets.values());
    }

    public Optional<Ticket> findById(String id) {
        return Optional.ofNullable(this.tickets.get(id));
    }

    public Optional<Ticket> findByIdSecured(String id, Authentication auth) {
        Optional<Ticket> ticket = findById(id);
        if (ticket.isPresent() && ticket.get().getUsers().containsKey(auth.getName())) {
            return ticket;
        }

        return Optional.empty();
    }

    public List<Ticket> allTicketsForUser(String userName) {
        return tickets
            .values()
            .parallelStream()
            .filter(ticket -> ticket.getUsers().containsKey(userName))
            .collect(Collectors.toList());
    }

    public void addUser(String id, String userName) {
        Optional<Ticket> ticket = findById(id);

        if (ticket.isPresent()) {
            if (userName != null) {
                Optional<User> optionalUser = userService.getUser(userName);
                optionalUser.ifPresent((user) -> {
                    ticket.get().addUser(user);
                    ticket.get().getChat().addUser(user);
                });
            }
        }
    }
}
