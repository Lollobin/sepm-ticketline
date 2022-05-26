package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * EventSearchDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class EventSearchDto   {

  @JsonProperty("name")
  private String name;

  @JsonProperty("category")
  private String category;

  @JsonProperty("duration")
  private Integer duration;

  @JsonProperty("location")
  private Integer location;

  @JsonProperty("artist")
  private Integer artist;

  public EventSearchDto name(String name) {
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

  public EventSearchDto category(String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
  */
  
  @Schema(name = "category", required = false)
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public EventSearchDto duration(Integer duration) {
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

  public EventSearchDto location(Integer location) {
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

  public EventSearchDto artist(Integer artist) {
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
    EventSearchDto eventSearch = (EventSearchDto) o;
    return Objects.equals(this.name, eventSearch.name) &&
        Objects.equals(this.category, eventSearch.category) &&
        Objects.equals(this.duration, eventSearch.duration) &&
        Objects.equals(this.location, eventSearch.location) &&
        Objects.equals(this.artist, eventSearch.artist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, category, duration, location, artist);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventSearchDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
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

