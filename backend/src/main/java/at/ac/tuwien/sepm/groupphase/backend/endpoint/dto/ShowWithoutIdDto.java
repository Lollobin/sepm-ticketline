package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
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
 * ShowWithoutIdDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ShowWithoutIdDto   {

  @JsonProperty("date")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime date;

  @JsonProperty("event")
  private Integer event;

  @JsonProperty("artists")
  @Valid
  private List<Integer> artists = new ArrayList<>();

  @JsonProperty("seatingPlan")
  private Integer seatingPlan;

  public ShowWithoutIdDto date(OffsetDateTime date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
  */
  @NotNull @Valid 
  @Schema(name = "date", required = true)
  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  public ShowWithoutIdDto event(Integer event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
  */
  @NotNull 
  @Schema(name = "event", required = true)
  public Integer getEvent() {
    return event;
  }

  public void setEvent(Integer event) {
    this.event = event;
  }

  public ShowWithoutIdDto artists(List<Integer> artists) {
    this.artists = artists;
    return this;
  }

  public ShowWithoutIdDto addArtistsItem(Integer artistsItem) {
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

  public ShowWithoutIdDto seatingPlan(Integer seatingPlan) {
    this.seatingPlan = seatingPlan;
    return this;
  }

  /**
   * Get seatingPlan
   * @return seatingPlan
  */
  @NotNull 
  @Schema(name = "seatingPlan", required = true)
  public Integer getSeatingPlan() {
    return seatingPlan;
  }

  public void setSeatingPlan(Integer seatingPlan) {
    this.seatingPlan = seatingPlan;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShowWithoutIdDto showWithoutId = (ShowWithoutIdDto) o;
    return Objects.equals(this.date, showWithoutId.date) &&
        Objects.equals(this.event, showWithoutId.event) &&
        Objects.equals(this.artists, showWithoutId.artists) &&
        Objects.equals(this.seatingPlan, showWithoutId.seatingPlan);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, event, artists, seatingPlan);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShowWithoutIdDto {\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    artists: ").append(toIndentedString(artists)).append("\n");
    sb.append("    seatingPlan: ").append(toIndentedString(seatingPlan)).append("\n");
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

