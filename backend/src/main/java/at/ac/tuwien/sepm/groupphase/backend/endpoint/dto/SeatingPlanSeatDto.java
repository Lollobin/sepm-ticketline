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
 * SeatingPlanSeatDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SeatingPlanSeatDto   {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("sectorId")
  private Long sectorId;

  @JsonProperty("location")
  private ElementLocationDto location;

  public SeatingPlanSeatDto id(Long id) {
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

  public SeatingPlanSeatDto sectorId(Long sectorId) {
    this.sectorId = sectorId;
    return this;
  }

  /**
   * Get sectorId
   * @return sectorId
  */
  @NotNull 
  @Schema(name = "sectorId", required = true)
  public Long getSectorId() {
    return sectorId;
  }

  public void setSectorId(Long sectorId) {
    this.sectorId = sectorId;
  }

  public SeatingPlanSeatDto location(ElementLocationDto location) {
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
    SeatingPlanSeatDto seatingPlanSeat = (SeatingPlanSeatDto) o;
    return Objects.equals(this.id, seatingPlanSeat.id) &&
        Objects.equals(this.sectorId, seatingPlanSeat.sectorId) &&
        Objects.equals(this.location, seatingPlanSeat.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sectorId, location);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SeatingPlanSeatDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    sectorId: ").append(toIndentedString(sectorId)).append("\n");
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

