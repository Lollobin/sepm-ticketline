package at.ac.tuwien.sepm.groupphase.backend.repository.result;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Category;

public class EventWithTicketsSold {

    private Long eventId;
    private String name;
    private long duration;
    private Category category;
    private String content;
    private long ticketsSold;

    public EventWithTicketsSold(Long eventId, String name, long duration, Category category, String content, long ticketsSold) {
        this.eventId = eventId;
        this.name = name;
        this.duration = duration;
        this.category = category;
        this.content = content;
        this.ticketsSold = ticketsSold;
    }

    public EventWithTicketsSold() {
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(long ticketsSold) {
        this.ticketsSold = ticketsSold;
    }
}
