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
 * TicketDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class TicketDto   {

  @JsonProperty("ticketId")
  private BigDecimal ticketId;

  @JsonProperty("rowNumber")
  private Long rowNumber;

  @JsonProperty("seatNumber")
  private Long seatNumber;

  @JsonProperty("sector")
  private Long sector;

  public TicketDto ticketId(BigDecimal ticketId) {
    this.ticketId = ticketId;
    return this;
  }

  /**
   * Get ticketId
   * @return ticketId
  */
  @NotNull @Valid 
  @Schema(name = "ticketId", required = true)
  public BigDecimal getTicketId() {
    return ticketId;
  }

  public void setTicketId(BigDecimal ticketId) {
    this.ticketId = ticketId;
  }

  public TicketDto rowNumber(Long rowNumber) {
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

  public TicketDto seatNumber(Long seatNumber) {
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

  public TicketDto sector(Long sector) {
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
    TicketDto ticket = (TicketDto) o;
    return Objects.equals(this.ticketId, ticket.ticketId) &&
        Objects.equals(this.rowNumber, ticket.rowNumber) &&
        Objects.equals(this.seatNumber, ticket.seatNumber) &&
        Objects.equals(this.sector, ticket.sector);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticketId, rowNumber, seatNumber, sector);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TicketDto {\n");
    sb.append("    ticketId: ").append(toIndentedString(ticketId)).append("\n");
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

