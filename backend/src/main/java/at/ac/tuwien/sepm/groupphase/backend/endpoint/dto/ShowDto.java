package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
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
 * ShowDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ShowDto   {

  @JsonProperty("showId")
  private Long showId;

  @JsonProperty("date")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime date;

  @JsonProperty("event")
  private EventDto event;

  @JsonProperty("artists")
  @Valid
  private List<Integer> artists = new ArrayList<>();

  @JsonProperty("location")
  private LocationDto location;

  public ShowDto showId(Long showId) {
    this.showId = showId;
    return this;
  }

  /**
   * Get showId
   * @return showId
  */
  @NotNull 
  @Schema(name = "showId", required = true)
  public Long getShowId() {
    return showId;
  }

  public void setShowId(Long showId) {
    this.showId = showId;
  }

  public ShowDto date(OffsetDateTime date) {
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

  public ShowDto event(EventDto event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
  */
  @NotNull @Valid 
  @Schema(name = "event", required = true)
  public EventDto getEvent() {
    return event;
  }

  public void setEvent(EventDto event) {
    this.event = event;
  }

  public ShowDto artists(List<Integer> artists) {
    this.artists = artists;
    return this;
  }

  public ShowDto addArtistsItem(Integer artistsItem) {
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
  public List<Integer> getArtists() {
    return artists;
  }

  public void setArtists(List<Integer> artists) {
    this.artists = artists;
  }

  public ShowDto location(LocationDto location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
  */
  @NotNull @Valid 
  @Schema(name = "location", required = true)
  public LocationDto getLocation() {
    return location;
  }

  public void setLocation(LocationDto location) {
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShowDto show = (ShowDto) o;
    return Objects.equals(this.showId, show.showId) &&
        Objects.equals(this.date, show.date) &&
        Objects.equals(this.event, show.event) &&
        Objects.equals(this.artists, show.artists) &&
        Objects.equals(this.location, show.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(showId, date, event, artists, location);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShowDto {\n");
    sb.append("    showId: ").append(toIndentedString(showId)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    artists: ").append(toIndentedString(artists)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
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

