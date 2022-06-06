package at.ac.tuwien.sepm.groupphase.backend.repository.result;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Category;

public interface EventWithTickets {

    int getEventId();

    String getName();

    int getDuration();

    Category getCategory();

    int getTicketsSold();
}
