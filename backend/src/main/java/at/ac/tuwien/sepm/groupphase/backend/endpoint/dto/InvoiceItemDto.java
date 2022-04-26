package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingTypeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
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
 * InvoiceItemDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-26T12:09:28.088881827+02:00[Europe/Vienna]")
public class InvoiceItemDto   {

  @JsonProperty("priceAtBookingTime")
  private BigDecimal priceAtBookingTime;

  @JsonProperty("type")
  private BookingTypeDto type;

  @JsonProperty("ticket")
  private TicketDto ticket;

  public InvoiceItemDto priceAtBookingTime(BigDecimal priceAtBookingTime) {
    this.priceAtBookingTime = priceAtBookingTime;
    return this;
  }

  /**
   * Get priceAtBookingTime
   * @return priceAtBookingTime
  */
  @NotNull @Valid 
  @Schema(name = "priceAtBookingTime", required = true)
  public BigDecimal getPriceAtBookingTime() {
    return priceAtBookingTime;
  }

  public void setPriceAtBookingTime(BigDecimal priceAtBookingTime) {
    this.priceAtBookingTime = priceAtBookingTime;
  }

  public InvoiceItemDto type(BookingTypeDto type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  */
  @NotNull @Valid 
  @Schema(name = "type", required = true)
  public BookingTypeDto getType() {
    return type;
  }

  public void setType(BookingTypeDto type) {
    this.type = type;
  }

  public InvoiceItemDto ticket(TicketDto ticket) {
    this.ticket = ticket;
    return this;
  }

  /**
   * Get ticket
   * @return ticket
  */
  @NotNull @Valid 
  @Schema(name = "ticket", required = true)
  public TicketDto getTicket() {
    return ticket;
  }

  public void setTicket(TicketDto ticket) {
    this.ticket = ticket;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InvoiceItemDto invoiceItem = (InvoiceItemDto) o;
    return Objects.equals(this.priceAtBookingTime, invoiceItem.priceAtBookingTime) &&
        Objects.equals(this.type, invoiceItem.type) &&
        Objects.equals(this.ticket, invoiceItem.ticket);
  }

  @Override
  public int hashCode() {
    return Objects.hash(priceAtBookingTime, type, ticket);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InvoiceItemDto {\n");
    sb.append("    priceAtBookingTime: ").append(toIndentedString(priceAtBookingTime)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    ticket: ").append(toIndentedString(ticket)).append("\n");
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

