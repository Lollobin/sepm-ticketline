package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketWithShowInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketWithShowInfoDto.TypeEnum;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface TicketMapper {

    default TicketDto ticketToTicketDto(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setTicketId(ticket.getTicketId());
        ticketDto.setSector(ticket.getSeat().getSector().getSectorId());
        ticketDto.setSeatNumber(ticket.getSeat().getSeatNumber());
        ticketDto.setRowNumber(ticket.getSeat().getRowNumber());
        return ticketDto;
    }

    List<TicketDto> ticketToTicketDto(List<Ticket> tickets);

    default List<TicketWithShowInfoDto> ticketToTicketWithShowInfoDto(List<Ticket> tickets) {
        Set<TicketWithShowInfoDto> ticketWithShowInfoDtoSet = new HashSet<>();

        for (Ticket ticket : tickets) {
            TicketWithShowInfoDto dto = new TicketWithShowInfoDto();
            dto.setType(ticket.getPurchasedBy() == null ? TypeEnum.RESERVED : TypeEnum.PURCHASED);
            dto.setShowDate(ticket.getShow().getDate());
            dto.setArtists(ticket.getShow().getArtists().stream().map(Artist::getKnownAs).toList());
            dto.setEventName(ticket.getShow().getEvent().getName());
            dto.setCity(
                ticket.getSeat().getSector().getSeatingPlan().getLocation().getAddress().getCity());
            dto.setLocationName(
                ticket.getSeat().getSector().getSeatingPlan().getLocation().getName());

            List<Ticket> includedTickets = new ArrayList<>();
            for (Ticket otherTicket : tickets) {
                if (ticket.getShow().equals(otherTicket.getShow())) {
                    includedTickets.add(otherTicket);
                }
            }
            dto.setTicket(ticketToTicketDto(includedTickets));

            ticketWithShowInfoDtoSet.add(dto);
        }
        return ticketWithShowInfoDtoSet.stream().toList();
    }
}
