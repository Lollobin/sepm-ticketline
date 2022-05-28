package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanLayoutGeneralDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanSeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanSectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanStaticElementDto;
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
 * SeatingPlanLayoutDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SeatingPlanLayoutDto   {

  @JsonProperty("general")
  private SeatingPlanLayoutGeneralDto general;

  @JsonProperty("seats")
  @Valid
  private List<SeatingPlanSeatDto> seats = new ArrayList<>();

  @JsonProperty("sectors")
  @Valid
  private List<SeatingPlanSectorDto> sectors = new ArrayList<>();

  @JsonProperty("staticElements")
  @Valid
  private List<SeatingPlanStaticElementDto> staticElements = new ArrayList<>();

  public SeatingPlanLayoutDto general(SeatingPlanLayoutGeneralDto general) {
    this.general = general;
    return this;
  }

  /**
   * Get general
   * @return general
  */
  @NotNull @Valid 
  @Schema(name = "general", required = true)
  public SeatingPlanLayoutGeneralDto getGeneral() {
    return general;
  }

  public void setGeneral(SeatingPlanLayoutGeneralDto general) {
    this.general = general;
  }

  public SeatingPlanLayoutDto seats(List<SeatingPlanSeatDto> seats) {
    this.seats = seats;
    return this;
  }

  public SeatingPlanLayoutDto addSeatsItem(SeatingPlanSeatDto seatsItem) {
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
  public List<SeatingPlanSeatDto> getSeats() {
    return seats;
  }

  public void setSeats(List<SeatingPlanSeatDto> seats) {
    this.seats = seats;
  }

  public SeatingPlanLayoutDto sectors(List<SeatingPlanSectorDto> sectors) {
    this.sectors = sectors;
    return this;
  }

  public SeatingPlanLayoutDto addSectorsItem(SeatingPlanSectorDto sectorsItem) {
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
  public List<SeatingPlanSectorDto> getSectors() {
    return sectors;
  }

  public void setSectors(List<SeatingPlanSectorDto> sectors) {
    this.sectors = sectors;
  }

  public SeatingPlanLayoutDto staticElements(List<SeatingPlanStaticElementDto> staticElements) {
    this.staticElements = staticElements;
    return this;
  }

  public SeatingPlanLayoutDto addStaticElementsItem(SeatingPlanStaticElementDto staticElementsItem) {
    if (this.staticElements == null) {
      this.staticElements = new ArrayList<>();
    }
    this.staticElements.add(staticElementsItem);
    return this;
  }

  /**
   * Get staticElements
   * @return staticElements
  */
  @NotNull @Valid 
  @Schema(name = "staticElements", required = true)
  public List<SeatingPlanStaticElementDto> getStaticElements() {
    return staticElements;
  }

  public void setStaticElements(List<SeatingPlanStaticElementDto> staticElements) {
    this.staticElements = staticElements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SeatingPlanLayoutDto seatingPlanLayout = (SeatingPlanLayoutDto) o;
    return Objects.equals(this.general, seatingPlanLayout.general) &&
        Objects.equals(this.seats, seatingPlanLayout.seats) &&
        Objects.equals(this.sectors, seatingPlanLayout.sectors) &&
        Objects.equals(this.staticElements, seatingPlanLayout.staticElements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(general, seats, sectors, staticElements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SeatingPlanLayoutDto {\n");
    sb.append("    general: ").append(toIndentedString(general)).append("\n");
    sb.append("    seats: ").append(toIndentedString(seats)).append("\n");
    sb.append("    sectors: ").append(toIndentedString(sectors)).append("\n");
    sb.append("    staticElements: ").append(toIndentedString(staticElements)).append("\n");
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

