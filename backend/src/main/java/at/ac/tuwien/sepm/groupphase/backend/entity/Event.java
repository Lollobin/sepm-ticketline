package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Category;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(nullable = false)
    private String name;

    @Column
    private long duration;

    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(columnDefinition = "CLOB")
    private String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return eventId == event.eventId
            && duration == event.duration
            && Objects.equals(name, event.name)
            && Objects.equals(category, event.category)
            && Objects.equals(content, event.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, name, duration, category, content);
    }

    @Override
    public String toString() {
        return "Event{"
            + "eventId="
            + eventId
            + ", name='"
            + name
            + '\''
            + ", duration="
            + duration
            + ", category='"
            + category
            + '\''
            + ", content='"
            + content
            + '\''
            + '}';
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
}
