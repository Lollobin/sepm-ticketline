package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketWithShowInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketWithShowInfoDto.TypeEnum;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface TicketMapper {

    default TicketWithShowInfoDto ticketToTicketWithShowInfoDto(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setTicketId(ticket.getTicketId());
        ticketDto.setRowNumber(ticket.getSeat().getRowNumber());
        ticketDto.setSeatNumber(ticket.getSeat().getSeatNumber());
        ticketDto.setSector(ticket.getSeat().getSector().getSectorId());

        TicketWithShowInfoDto dto = new TicketWithShowInfoDto();
        dto.setType(ticket.getPurchasedBy() == null ? TypeEnum.RESERVED : TypeEnum.PURCHASED);
        dto.setTicket(ticketDto);
        dto.setShowDate(ticket.getShow().getDate());
        dto.setArtists(ticket.getShow().getArtists().stream().map(Artist::getKnownAs).toList());
        dto.setEventName(ticket.getShow().getEvent().getName());
        dto.setCity(
            ticket.getSeat().getSector().getSeatingPlan().getLocation().getAddress().getCity());
        dto.setLocationName(ticket.getSeat().getSector().getSeatingPlan().getLocation().getName());

        return dto;
    }

    List<TicketWithShowInfoDto> ticketToTicketWithShowInfoDto(List<Ticket> tickets);
}
