package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowInformationDto;

public interface ShowTicketService {
    /**
     * Find the show information by id.
     *
     * @param id the id of the show entry
     * @return the show with corresponding Infomration
     */
    ShowInformationDto findOne(Long id);
}
