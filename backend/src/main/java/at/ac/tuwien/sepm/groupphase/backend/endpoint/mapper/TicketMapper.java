package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketWithShowInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketWithShowInfoDto.TypeEnum;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface TicketMapper {

    class SortTicketByDate implements Comparator<TicketWithShowInfoDto> {

        @Override
        public int compare(TicketWithShowInfoDto o1, TicketWithShowInfoDto o2) {
            return o1.getShowDate().compareTo(o2.getShowDate());
        }
    }

    default TicketDto ticketToTicketDto(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setTicketId(ticket.getTicketId());
        ticketDto.setSector(ticket.getSeat().getSector().getSectorId());
        ticketDto.setSeatNumber(ticket.getSeat().getSeatNumber());
        ticketDto.setRowNumber(ticket.getSeat().getRowNumber());
        return ticketDto;
    }

    List<TicketDto> ticketToTicketDto(List<Ticket> tickets);

    default List<TicketWithShowInfoDto> ticketToTicketWithShowInfoDto(List<Ticket> tickets, ArtistMapper artistMapper) {
        Set<TicketWithShowInfoDto> ticketWithShowInfoDtoSet = new HashSet<>();

        for (Ticket ticket : tickets) {
            TicketWithShowInfoDto dto = new TicketWithShowInfoDto();
            dto.setType(ticket.getPurchasedBy() == null ? TypeEnum.RESERVED : TypeEnum.PURCHASED);
            dto.setShowDate(ticket.getShow().getDate());
            dto.setArtists(ticket.getShow().getArtists().stream().map(artistMapper::artistToArtistDto).toList());
            dto.setEventName(ticket.getShow().getEvent().getName());
            dto.setCity(
                ticket.getSeat().getSector().getSeatingPlan().getLocation().getAddress().getCity());
            dto.setLocationName(
                ticket.getSeat().getSector().getSeatingPlan().getLocation().getName());

            List<Ticket> includedTickets = new ArrayList<>();
            for (Ticket otherTicket : tickets) {
                if (ticket.getShow().equals(otherTicket.getShow())
                    && ticket.getReservedBy() == otherTicket.getReservedBy()
                    && ticket.getPurchasedBy() == otherTicket.getPurchasedBy()) {
                    includedTickets.add(otherTicket);
                }
            }
            dto.setTicket(ticketToTicketDto(includedTickets));

            ticketWithShowInfoDtoSet.add(dto);
        }

        List<TicketWithShowInfoDto> ticketWithShowInfoDtos = new ArrayList<>(
            ticketWithShowInfoDtoSet.stream().toList());
        ticketWithShowInfoDtos.sort(new SortTicketByDate());

        return ticketWithShowInfoDtos;
    }
}
