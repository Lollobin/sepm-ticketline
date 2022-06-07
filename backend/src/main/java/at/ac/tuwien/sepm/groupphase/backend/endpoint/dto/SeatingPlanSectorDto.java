package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ElementLocationDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * SeatingPlanSectorDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SeatingPlanSectorDto   {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("color")
  private Long color;

  @JsonProperty("description")
  private String description;

  @JsonProperty("noSeats")
  private Boolean noSeats;

  @JsonProperty("location")
  private ElementLocationDto location;

  public SeatingPlanSectorDto id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  @NotNull 
  @Schema(name = "id", required = true)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public SeatingPlanSectorDto color(Long color) {
    this.color = color;
    return this;
  }

  /**
   * Get color
   * @return color
  */
  @NotNull 
  @Schema(name = "color", required = true)
  public Long getColor() {
    return color;
  }

  public void setColor(Long color) {
    this.color = color;
  }

  public SeatingPlanSectorDto description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  */
  
  @Schema(name = "description", required = false)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public SeatingPlanSectorDto noSeats(Boolean noSeats) {
    this.noSeats = noSeats;
    return this;
  }

  /**
   * Get noSeats
   * @return noSeats
  */
  @NotNull 
  @Schema(name = "noSeats", required = true)
  public Boolean getNoSeats() {
    return noSeats;
  }

  public void setNoSeats(Boolean noSeats) {
    this.noSeats = noSeats;
  }

  public SeatingPlanSectorDto location(ElementLocationDto location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
  */
  @Valid 
  @Schema(name = "location", required = false)
  public ElementLocationDto getLocation() {
    return location;
  }

  public void setLocation(ElementLocationDto location) {
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
    SeatingPlanSectorDto seatingPlanSector = (SeatingPlanSectorDto) o;
    return Objects.equals(this.id, seatingPlanSector.id) &&
        Objects.equals(this.color, seatingPlanSector.color) &&
        Objects.equals(this.description, seatingPlanSector.description) &&
        Objects.equals(this.noSeats, seatingPlanSector.noSeats) &&
        Objects.equals(this.location, seatingPlanSector.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, color, description, noSeats, location);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SeatingPlanSectorDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    color: ").append(toIndentedString(color)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    noSeats: ").append(toIndentedString(noSeats)).append("\n");
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

