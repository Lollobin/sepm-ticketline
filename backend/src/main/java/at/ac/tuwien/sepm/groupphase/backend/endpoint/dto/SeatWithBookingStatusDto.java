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
 * SeatWithBookingStatusDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SeatWithBookingStatusDto   {

  @JsonProperty("seatId")
  private BigDecimal seatId;

  @JsonProperty("reserved")
  private Boolean reserved;

  @JsonProperty("purchased")
  private Boolean purchased;

  @JsonProperty("rowNumber")
  private BigDecimal rowNumber;

  @JsonProperty("seatNumber")
  private BigDecimal seatNumber;

  @JsonProperty("sector")
  private BigDecimal sector;

  public SeatWithBookingStatusDto seatId(BigDecimal seatId) {
    this.seatId = seatId;
    return this;
  }

  /**
   * Get seatId
   * @return seatId
  */
  @NotNull @Valid 
  @Schema(name = "seatId", required = true)
  public BigDecimal getSeatId() {
    return seatId;
  }

  public void setSeatId(BigDecimal seatId) {
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

  public SeatWithBookingStatusDto rowNumber(BigDecimal rowNumber) {
    this.rowNumber = rowNumber;
    return this;
  }

  /**
   * Get rowNumber
   * @return rowNumber
  */
  @Valid 
  @Schema(name = "rowNumber", required = false)
  public BigDecimal getRowNumber() {
    return rowNumber;
  }

  public void setRowNumber(BigDecimal rowNumber) {
    this.rowNumber = rowNumber;
  }

  public SeatWithBookingStatusDto seatNumber(BigDecimal seatNumber) {
    this.seatNumber = seatNumber;
    return this;
  }

  /**
   * Get seatNumber
   * @return seatNumber
  */
  @Valid 
  @Schema(name = "seatNumber", required = false)
  public BigDecimal getSeatNumber() {
    return seatNumber;
  }

  public void setSeatNumber(BigDecimal seatNumber) {
    this.seatNumber = seatNumber;
  }

  public SeatWithBookingStatusDto sector(BigDecimal sector) {
    this.sector = sector;
    return this;
  }

  /**
   * Get sector
   * @return sector
  */
  @NotNull @Valid 
  @Schema(name = "sector", required = true)
  public BigDecimal getSector() {
    return sector;
  }

  public void setSector(BigDecimal sector) {
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

