package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long eventId;

    @Column(nullable = false)
    private String name;

    @Column
    private long duration;

    private String category;

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
        return eventId == event.eventId && duration == event.duration && Objects.equals(name,
            event.name) && Objects.equals(category, event.category) && Objects.equals(
            content, event.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, name, duration, category, content);
    }

    @Override
    public String toString() {
        return "Event{" +
            "eventId=" + eventId +
            ", name='" + name + '\'' +
            ", duration=" + duration +
            ", category='" + category + '\'' +
            ", content='" + content + '\'' +
            '}';
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
