package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CategoryDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * EventWithTicketsSoldDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class EventWithTicketsSoldDto   {

  @JsonProperty("eventId")
  private Integer eventId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("duration")
  private Integer duration;

  @JsonProperty("category")
  private CategoryDto category;

  @JsonProperty("ticketsSold")
  private Integer ticketsSold;

  public EventWithTicketsSoldDto eventId(Integer eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
  */
  
  @Schema(name = "eventId", required = false)
  public Integer getEventId() {
    return eventId;
  }

  public void setEventId(Integer eventId) {
    this.eventId = eventId;
  }

  public EventWithTicketsSoldDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  
  @Schema(name = "name", required = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public EventWithTicketsSoldDto duration(Integer duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Get duration
   * @return duration
  */
  
  @Schema(name = "duration", required = false)
  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public EventWithTicketsSoldDto category(CategoryDto category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
  */
  @Valid 
  @Schema(name = "category", required = false)
  public CategoryDto getCategory() {
    return category;
  }

  public void setCategory(CategoryDto category) {
    this.category = category;
  }

  public EventWithTicketsSoldDto ticketsSold(Integer ticketsSold) {
    this.ticketsSold = ticketsSold;
    return this;
  }

  /**
   * Get ticketsSold
   * @return ticketsSold
  */
  @NotNull 
  @Schema(name = "ticketsSold", required = true)
  public Integer getTicketsSold() {
    return ticketsSold;
  }

  public void setTicketsSold(Integer ticketsSold) {
    this.ticketsSold = ticketsSold;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventWithTicketsSoldDto eventWithTicketsSold = (EventWithTicketsSoldDto) o;
    return Objects.equals(this.eventId, eventWithTicketsSold.eventId) &&
        Objects.equals(this.name, eventWithTicketsSold.name) &&
        Objects.equals(this.duration, eventWithTicketsSold.duration) &&
        Objects.equals(this.category, eventWithTicketsSold.category) &&
        Objects.equals(this.ticketsSold, eventWithTicketsSold.ticketsSold);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, name, duration, category, ticketsSold);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventWithTicketsSoldDto {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    ticketsSold: ").append(toIndentedString(ticketsSold)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

