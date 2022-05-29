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
 * SeatDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SeatDto   {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("rowNumber")
  private Long rowNumber;

  @JsonProperty("seatNumber")
  private Long seatNumber;

  @JsonProperty("sector")
  private Long sector;

  public SeatDto id(Long id) {
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

  public SeatDto rowNumber(Long rowNumber) {
    this.rowNumber = rowNumber;
    return this;
  }

  /**
   * Get rowNumber
   * @return rowNumber
  */
  
  @Schema(name = "rowNumber", required = false)
  public Long getRowNumber() {
    return rowNumber;
  }

  public void setRowNumber(Long rowNumber) {
    this.rowNumber = rowNumber;
  }

  public SeatDto seatNumber(Long seatNumber) {
    this.seatNumber = seatNumber;
    return this;
  }

  /**
   * Get seatNumber
   * @return seatNumber
  */
  
  @Schema(name = "seatNumber", required = false)
  public Long getSeatNumber() {
    return seatNumber;
  }

  public void setSeatNumber(Long seatNumber) {
    this.seatNumber = seatNumber;
  }

  public SeatDto sector(Long sector) {
    this.sector = sector;
    return this;
  }

  /**
   * Get sector
   * @return sector
  */
  @NotNull 
  @Schema(name = "sector", required = true)
  public Long getSector() {
    return sector;
  }

  public void setSector(Long sector) {
    this.sector = sector;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SeatDto seat = (SeatDto) o;
    return Objects.equals(this.id, seat.id) &&
        Objects.equals(this.rowNumber, seat.rowNumber) &&
        Objects.equals(this.seatNumber, seat.seatNumber) &&
        Objects.equals(this.sector, seat.sector);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, rowNumber, seatNumber, sector);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SeatDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    rowNumber: ").append(toIndentedString(rowNumber)).append("\n");
    sb.append("    seatNumber: ").append(toIndentedString(seatNumber)).append("\n");
    sb.append("    sector: ").append(toIndentedString(sector)).append("\n");
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

