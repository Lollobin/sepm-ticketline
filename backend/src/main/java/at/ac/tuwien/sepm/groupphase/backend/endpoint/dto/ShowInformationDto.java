package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatWithBookingStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorDto;
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
 * ShowInformationDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-26T12:20:21.425982882+02:00[Europe/Vienna]")
public class ShowInformationDto   {

  @JsonProperty("seats")
  @Valid
  private List<SeatWithBookingStatusDto> seats = new ArrayList<>();

  @JsonProperty("sectors")
  @Valid
  private List<SectorDto> sectors = new ArrayList<>();

  @JsonProperty("seatingPlan")
  private SeatingPlanDto seatingPlan;

  public ShowInformationDto seats(List<SeatWithBookingStatusDto> seats) {
    this.seats = seats;
    return this;
  }

  public ShowInformationDto addSeatsItem(SeatWithBookingStatusDto seatsItem) {
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
  public List<SeatWithBookingStatusDto> getSeats() {
    return seats;
  }

  public void setSeats(List<SeatWithBookingStatusDto> seats) {
    this.seats = seats;
  }

  public ShowInformationDto sectors(List<SectorDto> sectors) {
    this.sectors = sectors;
    return this;
  }

  public ShowInformationDto addSectorsItem(SectorDto sectorsItem) {
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
  public List<SectorDto> getSectors() {
    return sectors;
  }

  public void setSectors(List<SectorDto> sectors) {
    this.sectors = sectors;
  }

  public ShowInformationDto seatingPlan(SeatingPlanDto seatingPlan) {
    this.seatingPlan = seatingPlan;
    return this;
  }

  /**
   * Get seatingPlan
   * @return seatingPlan
  */
  @NotNull @Valid 
  @Schema(name = "seatingPlan", required = true)
  public SeatingPlanDto getSeatingPlan() {
    return seatingPlan;
  }

  public void setSeatingPlan(SeatingPlanDto seatingPlan) {
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
    ShowInformationDto showInformation = (ShowInformationDto) o;
    return Objects.equals(this.seats, showInformation.seats) &&
        Objects.equals(this.sectors, showInformation.sectors) &&
        Objects.equals(this.seatingPlan, showInformation.seatingPlan);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seats, sectors, seatingPlan);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShowInformationDto {\n");
    sb.append("    seats: ").append(toIndentedString(seats)).append("\n");
    sb.append("    sectors: ").append(toIndentedString(sectors)).append("\n");
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

