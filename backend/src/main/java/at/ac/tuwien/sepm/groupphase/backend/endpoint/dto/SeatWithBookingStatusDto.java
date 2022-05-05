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
 * SeatWithBookingStatusDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SeatWithBookingStatusDto   {

  @JsonProperty("seatId")
  private Long seatId;

  @JsonProperty("reserved")
  private Boolean reserved;

  @JsonProperty("purchased")
  private Boolean purchased;

  @JsonProperty("rowNumber")
  private Long rowNumber;

  @JsonProperty("seatNumber")
  private Long seatNumber;

  @JsonProperty("sector")
  private Long sector;

  public SeatWithBookingStatusDto seatId(Long seatId) {
    this.seatId = seatId;
    return this;
  }

  /**
   * Get seatId
   * @return seatId
  */
  @NotNull 
  @Schema(name = "seatId", required = true)
  public Long getSeatId() {
    return seatId;
  }

  public void setSeatId(Long seatId) {
    this.seatId = seatId;
  }

  public SeatWithBookingStatusDto reserved(Boolean reserved) {
    this.reserved = reserved;
    return this;
  }

  /**
   * Get reserved
   * @return reserved
  */
  @NotNull 
  @Schema(name = "reserved", required = true)
  public Boolean getReserved() {
    return reserved;
  }

  public void setReserved(Boolean reserved) {
    this.reserved = reserved;
  }

  public SeatWithBookingStatusDto purchased(Boolean purchased) {
    this.purchased = purchased;
    return this;
  }

  /**
   * Get purchased
   * @return purchased
  */
  @NotNull 
  @Schema(name = "purchased", required = true)
  public Boolean getPurchased() {
    return purchased;
  }

  public void setPurchased(Boolean purchased) {
    this.purchased = purchased;
  }

  public SeatWithBookingStatusDto rowNumber(Long rowNumber) {
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

  public SeatWithBookingStatusDto seatNumber(Long seatNumber) {
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

  public SeatWithBookingStatusDto sector(Long sector) {
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
    SeatWithBookingStatusDto seatWithBookingStatus = (SeatWithBookingStatusDto) o;
    return Objects.equals(this.seatId, seatWithBookingStatus.seatId) &&
        Objects.equals(this.reserved, seatWithBookingStatus.reserved) &&
        Objects.equals(this.purchased, seatWithBookingStatus.purchased) &&
        Objects.equals(this.rowNumber, seatWithBookingStatus.rowNumber) &&
        Objects.equals(this.seatNumber, seatWithBookingStatus.seatNumber) &&
        Objects.equals(this.sector, seatWithBookingStatus.sector);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seatId, reserved, purchased, rowNumber, seatNumber, sector);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SeatWithBookingStatusDto {\n");
    sb.append("    seatId: ").append(toIndentedString(seatId)).append("\n");
    sb.append("    reserved: ").append(toIndentedString(reserved)).append("\n");
    sb.append("    purchased: ").append(toIndentedString(purchased)).append("\n");
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

