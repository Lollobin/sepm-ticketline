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
 * ShowSearchDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-25T14:38:18.865520970+02:00[Europe/Vienna]")
public class ShowSearchDto   {

  @JsonProperty("event")
  private Integer event;

  @JsonProperty("date")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime date;

  @JsonProperty("price")
  private BigDecimal price;

  @JsonProperty("seatingPlan")
  private Integer seatingPlan;

  @JsonProperty("location")
  private Integer location;

  @JsonProperty("artist")
  private Integer artist;

  public ShowSearchDto event(Integer event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
  */
  
  @Schema(name = "event", required = false)
  public Integer getEvent() {
    return event;
  }

  public void setEvent(Integer event) {
    this.event = event;
  }

  public ShowSearchDto date(OffsetDateTime date) {
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

  public ShowSearchDto price(BigDecimal price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * @return price
  */
  @Valid 
  @Schema(name = "price", required = false)
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public ShowSearchDto seatingPlan(Integer seatingPlan) {
    this.seatingPlan = seatingPlan;
    return this;
  }

  /**
   * Get seatingPlan
   * @return seatingPlan
  */
  
  @Schema(name = "seatingPlan", required = false)
  public Integer getSeatingPlan() {
    return seatingPlan;
  }

  public void setSeatingPlan(Integer seatingPlan) {
    this.seatingPlan = seatingPlan;
  }

  public ShowSearchDto location(Integer location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
  */
  
  @Schema(name = "location", required = false)
  public Integer getLocation() {
    return location;
  }

  public void setLocation(Integer location) {
    this.location = location;
  }

  public ShowSearchDto artist(Integer artist) {
    this.artist = artist;
    return this;
  }

  /**
   * Get artist
   * @return artist
  */
  
  @Schema(name = "artist", required = false)
  public Integer getArtist() {
    return artist;
  }

  public void setArtist(Integer artist) {
    this.artist = artist;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShowSearchDto showSearch = (ShowSearchDto) o;
    return Objects.equals(this.event, showSearch.event) &&
        Objects.equals(this.date, showSearch.date) &&
        Objects.equals(this.price, showSearch.price) &&
        Objects.equals(this.seatingPlan, showSearch.seatingPlan) &&
        Objects.equals(this.location, showSearch.location) &&
        Objects.equals(this.artist, showSearch.artist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(event, date, price, seatingPlan, location, artist);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShowSearchDto {\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    seatingPlan: ").append(toIndentedString(seatingPlan)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    artist: ").append(toIndentedString(artist)).append("\n");
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

