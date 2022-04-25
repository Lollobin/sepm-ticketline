package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * ShowWithTicketsSoldDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-25T14:38:18.865520970+02:00[Europe/Vienna]")
public class ShowWithTicketsSoldDto   {

  @JsonProperty("showId")
  private Integer showId;

  @JsonProperty("date")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime date;

  @JsonProperty("event")
  private BigDecimal event;

  @JsonProperty("ticketsSold")
  private Integer ticketsSold;

  public ShowWithTicketsSoldDto showId(Integer showId) {
    this.showId = showId;
    return this;
  }

  /**
   * Get showId
   * @return showId
  */
  @NotNull 
  @Schema(name = "showId", required = true)
  public Integer getShowId() {
    return showId;
  }

  public void setShowId(Integer showId) {
    this.showId = showId;
  }

  public ShowWithTicketsSoldDto date(OffsetDateTime date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
  */
  @Valid 
  @Schema(name = "date", required = false)
  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  public ShowWithTicketsSoldDto event(BigDecimal event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
  */
  @Valid 
  @Schema(name = "event", required = false)
  public BigDecimal getEvent() {
    return event;
  }

  public void setEvent(BigDecimal event) {
    this.event = event;
  }

  public ShowWithTicketsSoldDto ticketsSold(Integer ticketsSold) {
    this.ticketsSold = ticketsSold;
    return this;
  }

  /**
   * Get ticketsSold
   * @return ticketsSold
  */
  
  @Schema(name = "ticketsSold", required = false)
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
    ShowWithTicketsSoldDto showWithTicketsSold = (ShowWithTicketsSoldDto) o;
    return Objects.equals(this.showId, showWithTicketsSold.showId) &&
        Objects.equals(this.date, showWithTicketsSold.date) &&
        Objects.equals(this.event, showWithTicketsSold.event) &&
        Objects.equals(this.ticketsSold, showWithTicketsSold.ticketsSold);
  }

  @Override
  public int hashCode() {
    return Objects.hash(showId, date, event, ticketsSold);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShowWithTicketsSoldDto {\n");
    sb.append("    showId: ").append(toIndentedString(showId)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
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

