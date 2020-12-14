package com.unn.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.unn.dto.TicketRequest;
import com.unn.model.Chat;
import com.unn.model.Ticket;
import com.unn.model.User;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketService {
    private Map<String, Ticket> tickets = new HashMap<>();

    private final UserService userService;

    public void createTicket(final TicketRequest ticketRequest, final User user, final Chat chat) {
        Ticket ticket = new Ticket();
        ticket.setCreator(ticketRequest.getUsername());
        ticket.setName(ticketRequest.getTicketName());
        ticket.setDescription(ticketRequest.getDesription());
        ticket.setChat(chat);
        ticket.addUser(user);
        if (!this.tickets.containsKey(ticket.getName())) {
            this.tickets.put(ticket.getName(), ticket);
        }
    }

    public List<Ticket> allTickets() {
        return new ArrayList<>(this.tickets.values());
    }

    public Optional<Ticket> findTicket(String name) {
        return Optional.ofNullable(this.tickets.get(name));
    }

    public List<Ticket> allTicketsForUser(String userName) {
        return tickets
            .values()
            .parallelStream()
            .filter(ticket -> ticket.getUsers().containsKey(userName))
            .collect(Collectors.toList());
    }

    public Collection<User> addUser(String chatName, String userName) {
        Optional<Ticket> ticket = findTicket(chatName);

        if (ticket.isPresent()) {
            if (userName != null) {
                Optional<User> optionalUser = userService.getUser(userName);
                optionalUser.ifPresent((user) -> {
                    ticket.get().addUser(user);
                });
            }
            return ticket.get().getUsers().values();
        }
        return List.of();
    }
}
