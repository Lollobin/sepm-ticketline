package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * TicketWithShowInfoDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class TicketWithShowInfoDto   {

  @JsonProperty("ticket")
  @Valid
  private List<TicketDto> ticket = new ArrayList<>();

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    PURCHASED("purchased"),
    
    RESERVED("reserved");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @JsonProperty("type")
  private TypeEnum type;

  @JsonProperty("showDate")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime showDate;

  @JsonProperty("artists")
  @Valid
  private List<String> artists = new ArrayList<>();

  @JsonProperty("eventName")
  private String eventName;

  @JsonProperty("city")
  private String city;

  @JsonProperty("locationName")
  private String locationName;

  public TicketWithShowInfoDto ticket(List<TicketDto> ticket) {
    this.ticket = ticket;
    return this;
  }

  public TicketWithShowInfoDto addTicketItem(TicketDto ticketItem) {
    if (this.ticket == null) {
      this.ticket = new ArrayList<>();
    }
    this.ticket.add(ticketItem);
    return this;
  }

  /**
   * Get ticket
   * @return ticket
  */
  @NotNull @Valid 
  @Schema(name = "ticket", required = true)
  public List<TicketDto> getTicket() {
    return ticket;
  }

  public void setTicket(List<TicketDto> ticket) {
    this.ticket = ticket;
  }

  public TicketWithShowInfoDto type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  */
  @NotNull 
  @Schema(name = "type", required = true)
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public TicketWithShowInfoDto showDate(OffsetDateTime showDate) {
    this.showDate = showDate;
    return this;
  }

  /**
   * Get showDate
   * @return showDate
  */
  @NotNull @Valid 
  @Schema(name = "showDate", required = true)
  public OffsetDateTime getShowDate() {
    return showDate;
  }

  public void setShowDate(OffsetDateTime showDate) {
    this.showDate = showDate;
  }

  public TicketWithShowInfoDto artists(List<String> artists) {
    this.artists = artists;
    return this;
  }

  public TicketWithShowInfoDto addArtistsItem(String artistsItem) {
    if (this.artists == null) {
      this.artists = new ArrayList<>();
    }
    this.artists.add(artistsItem);
    return this;
  }

  /**
   * Get artists
   * @return artists
  */
  @NotNull 
  @Schema(name = "artists", required = true)
  public List<String> getArtists() {
    return artists;
  }

  public void setArtists(List<String> artists) {
    this.artists = artists;
  }

  public TicketWithShowInfoDto eventName(String eventName) {
    this.eventName = eventName;
    return this;
  }

  /**
   * Get eventName
   * @return eventName
  */
  @NotNull 
  @Schema(name = "eventName", required = true)
  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public TicketWithShowInfoDto city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Get city
   * @return city
  */
  @NotNull 
  @Schema(name = "city", required = true)
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public TicketWithShowInfoDto locationName(String locationName) {
    this.locationName = locationName;
    return this;
  }

  /**
   * Get locationName
   * @return locationName
  */
  @NotNull 
  @Schema(name = "locationName", required = true)
  public String getLocationName() {
    return locationName;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TicketWithShowInfoDto ticketWithShowInfo = (TicketWithShowInfoDto) o;
    return Objects.equals(this.ticket, ticketWithShowInfo.ticket) &&
        Objects.equals(this.type, ticketWithShowInfo.type) &&
        Objects.equals(this.showDate, ticketWithShowInfo.showDate) &&
        Objects.equals(this.artists, ticketWithShowInfo.artists) &&
        Objects.equals(this.eventName, ticketWithShowInfo.eventName) &&
        Objects.equals(this.city, ticketWithShowInfo.city) &&
        Objects.equals(this.locationName, ticketWithShowInfo.locationName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticket, type, showDate, artists, eventName, city, locationName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TicketWithShowInfoDto {\n");
    sb.append("    ticket: ").append(toIndentedString(ticket)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    showDate: ").append(toIndentedString(showDate)).append("\n");
    sb.append("    artists: ").append(toIndentedString(artists)).append("\n");
    sb.append("    eventName: ").append(toIndentedString(eventName)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    locationName: ").append(toIndentedString(locationName)).append("\n");
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

