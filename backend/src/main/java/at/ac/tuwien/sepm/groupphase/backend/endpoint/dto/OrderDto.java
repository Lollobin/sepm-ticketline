package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingTypeDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * OrderDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class OrderDto   {

  @JsonProperty("type")
  private BookingTypeDto type;

  @JsonProperty("transactionDate")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime transactionDate;

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

  @JsonProperty("transactionId")
  private Long transactionId;

  @JsonProperty("ticketIds")
  @Valid
  private List<Long> ticketIds = new ArrayList<>();

  public OrderDto type(BookingTypeDto type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  */
  @NotNull @Valid 
  @Schema(name = "type", required = true)
  public BookingTypeDto getType() {
    return type;
  }

  public void setType(BookingTypeDto type) {
    this.type = type;
  }

  public OrderDto transactionDate(OffsetDateTime transactionDate) {
    this.transactionDate = transactionDate;
    return this;
  }

  /**
   * Get transactionDate
   * @return transactionDate
  */
  @Valid 
  @Schema(name = "transactionDate", required = false)
  public OffsetDateTime getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(OffsetDateTime transactionDate) {
    this.transactionDate = transactionDate;
  }

  public OrderDto showDate(OffsetDateTime showDate) {
    this.showDate = showDate;
    return this;
  }

  /**
   * Get showDate
   * @return showDate
  */
  @Valid 
  @Schema(name = "showDate", required = false)
  public OffsetDateTime getShowDate() {
    return showDate;
  }

  public void setShowDate(OffsetDateTime showDate) {
    this.showDate = showDate;
  }

  public OrderDto artists(List<String> artists) {
    this.artists = artists;
    return this;
  }

  public OrderDto addArtistsItem(String artistsItem) {
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

  public OrderDto eventName(String eventName) {
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

  public OrderDto city(String city) {
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

  public OrderDto locationName(String locationName) {
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

  public OrderDto transactionId(Long transactionId) {
    this.transactionId = transactionId;
    return this;
  }

  /**
   * Get transactionId
   * @return transactionId
  */
  @NotNull 
  @Schema(name = "transactionId", required = true)
  public Long getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(Long transactionId) {
    this.transactionId = transactionId;
  }

  public OrderDto ticketIds(List<Long> ticketIds) {
    this.ticketIds = ticketIds;
    return this;
  }

  public OrderDto addTicketIdsItem(Long ticketIdsItem) {
    if (this.ticketIds == null) {
      this.ticketIds = new ArrayList<>();
    }
    this.ticketIds.add(ticketIdsItem);
    return this;
  }

  /**
   * Get ticketIds
   * @return ticketIds
  */
  @NotNull 
  @Schema(name = "ticketIds", required = true)
  public List<Long> getTicketIds() {
    return ticketIds;
  }

  public void setTicketIds(List<Long> ticketIds) {
    this.ticketIds = ticketIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderDto order = (OrderDto) o;
    return Objects.equals(this.type, order.type) &&
        Objects.equals(this.transactionDate, order.transactionDate) &&
        Objects.equals(this.showDate, order.showDate) &&
        Objects.equals(this.artists, order.artists) &&
        Objects.equals(this.eventName, order.eventName) &&
        Objects.equals(this.city, order.city) &&
        Objects.equals(this.locationName, order.locationName) &&
        Objects.equals(this.transactionId, order.transactionId) &&
        Objects.equals(this.ticketIds, order.ticketIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, transactionDate, showDate, artists, eventName, city, locationName, transactionId, ticketIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderDto {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    transactionDate: ").append(toIndentedString(transactionDate)).append("\n");
    sb.append("    showDate: ").append(toIndentedString(showDate)).append("\n");
    sb.append("    artists: ").append(toIndentedString(artists)).append("\n");
    sb.append("    eventName: ").append(toIndentedString(eventName)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    locationName: ").append(toIndentedString(locationName)).append("\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    ticketIds: ").append(toIndentedString(ticketIds)).append("\n");
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

