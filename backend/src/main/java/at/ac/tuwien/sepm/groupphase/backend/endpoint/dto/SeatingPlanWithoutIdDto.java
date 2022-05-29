package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ProvisionalSectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * SeatingPlanWithoutIdDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SeatingPlanWithoutIdDto   {

  @JsonProperty("name")
  private String name;

  @JsonProperty("seatingPlanLayoutId")
  private Long seatingPlanLayoutId;

  @JsonProperty("locationId")
  private Long locationId;

  @JsonProperty("sectors")
  @Valid
  private List<ProvisionalSectorDto> sectors = new ArrayList<>();

  @JsonProperty("seats")
  @Valid
  private List<SeatDto> seats = new ArrayList<>();

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

  public SeatingPlanWithoutIdDto seatingPlanLayoutId(Long seatingPlanLayoutId) {
    this.seatingPlanLayoutId = seatingPlanLayoutId;
    return this;
  }

  /**
   * Get seatingPlanLayoutId
   * @return seatingPlanLayoutId
  */
  @NotNull 
  @Schema(name = "seatingPlanLayoutId", required = true)
  public Long getSeatingPlanLayoutId() {
    return seatingPlanLayoutId;
  }

  public void setSeatingPlanLayoutId(Long seatingPlanLayoutId) {
    this.seatingPlanLayoutId = seatingPlanLayoutId;
  }

  public SeatingPlanWithoutIdDto locationId(Long locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * Get locationId
   * @return locationId
  */
  @NotNull 
  @Schema(name = "locationId", required = true)
  public Long getLocationId() {
    return locationId;
  }

  public void setLocationId(Long locationId) {
    this.locationId = locationId;
  }

  public SeatingPlanWithoutIdDto sectors(List<ProvisionalSectorDto> sectors) {
    this.sectors = sectors;
    return this;
  }

  public SeatingPlanWithoutIdDto addSectorsItem(ProvisionalSectorDto sectorsItem) {
    if (this.sectors == null) {
      this.sectors = new ArrayList<>();
    }
    this.sectors.add(sectorsItem);
    return this;
  }

  /**
   * Get sectors
   * @return sectors
  */
  @NotNull @Valid 
  @Schema(name = "sectors", required = true)
  public List<ProvisionalSectorDto> getSectors() {
    return sectors;
  }

  public void setSectors(List<ProvisionalSectorDto> sectors) {
    this.sectors = sectors;
  }

  public SeatingPlanWithoutIdDto seats(List<SeatDto> seats) {
    this.seats = seats;
    return this;
  }

  public SeatingPlanWithoutIdDto addSeatsItem(SeatDto seatsItem) {
    if (this.seats == null) {
      this.seats = new ArrayList<>();
    }
    this.seats.add(seatsItem);
    return this;
  }

  /**
   * Get seats
   * @return seats
  */
  @NotNull @Valid 
  @Schema(name = "seats", required = true)
  public List<SeatDto> getSeats() {
    return seats;
  }

  public void setSeats(List<SeatDto> seats) {
    this.seats = seats;
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
        Objects.equals(this.locationId, seatingPlanWithoutId.locationId) &&
        Objects.equals(this.sectors, seatingPlanWithoutId.sectors) &&
        Objects.equals(this.seats, seatingPlanWithoutId.seats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, seatingPlanLayoutId, locationId, sectors, seats);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SeatingPlanWithoutIdDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    seatingPlanLayoutId: ").append(toIndentedString(seatingPlanLayoutId)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    sectors: ").append(toIndentedString(sectors)).append("\n");
    sb.append("    seats: ").append(toIndentedString(seats)).append("\n");
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

