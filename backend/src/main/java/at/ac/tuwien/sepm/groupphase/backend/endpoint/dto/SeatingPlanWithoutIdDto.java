package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * SeatingPlanWithoutIdDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-26T12:09:28.088881827+02:00[Europe/Vienna]")
public class SeatingPlanWithoutIdDto   {

  @JsonProperty("name")
  private String name;

  @JsonProperty("seatingPlanLayoutId")
  private BigDecimal seatingPlanLayoutId;

  @JsonProperty("locationId")
  private Integer locationId;

  public SeatingPlanWithoutIdDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  @NotNull 
  @Schema(name = "name", required = true)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SeatingPlanWithoutIdDto seatingPlanLayoutId(BigDecimal seatingPlanLayoutId) {
    this.seatingPlanLayoutId = seatingPlanLayoutId;
    return this;
  }

  /**
   * Get seatingPlanLayoutId
   * @return seatingPlanLayoutId
  */
  @NotNull @Valid 
  @Schema(name = "seatingPlanLayoutId", required = true)
  public BigDecimal getSeatingPlanLayoutId() {
    return seatingPlanLayoutId;
  }

  public void setSeatingPlanLayoutId(BigDecimal seatingPlanLayoutId) {
    this.seatingPlanLayoutId = seatingPlanLayoutId;
  }

  public SeatingPlanWithoutIdDto locationId(Integer locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * Get locationId
   * @return locationId
  */
  @NotNull 
  @Schema(name = "locationId", required = true)
  public Integer getLocationId() {
    return locationId;
  }

  public void setLocationId(Integer locationId) {
    this.locationId = locationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SeatingPlanWithoutIdDto seatingPlanWithoutId = (SeatingPlanWithoutIdDto) o;
    return Objects.equals(this.name, seatingPlanWithoutId.name) &&
        Objects.equals(this.seatingPlanLayoutId, seatingPlanWithoutId.seatingPlanLayoutId) &&
        Objects.equals(this.locationId, seatingPlanWithoutId.locationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, seatingPlanLayoutId, locationId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SeatingPlanWithoutIdDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    seatingPlanLayoutId: ").append(toIndentedString(seatingPlanLayoutId)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
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

