package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
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
 * TicketStatusDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-26T12:09:28.088881827+02:00[Europe/Vienna]")
public class TicketStatusDto   {

  @JsonProperty("reserved")
  @Valid
  private List<Integer> reserved = new ArrayList<>();

  @JsonProperty("purchased")
  @Valid
  private List<Integer> purchased = new ArrayList<>();

  public TicketStatusDto reserved(List<Integer> reserved) {
    this.reserved = reserved;
    return this;
  }

  public TicketStatusDto addReservedItem(Integer reservedItem) {
    if (this.reserved == null) {
      this.reserved = new ArrayList<>();
    }
    this.reserved.add(reservedItem);
    return this;
  }

  /**
   * Get reserved
   * @return reserved
  */
  @NotNull 
  @Schema(name = "reserved", required = true)
  public List<Integer> getReserved() {
    return reserved;
  }

  public void setReserved(List<Integer> reserved) {
    this.reserved = reserved;
  }

  public TicketStatusDto purchased(List<Integer> purchased) {
    this.purchased = purchased;
    return this;
  }

  public TicketStatusDto addPurchasedItem(Integer purchasedItem) {
    if (this.purchased == null) {
      this.purchased = new ArrayList<>();
    }
    this.purchased.add(purchasedItem);
    return this;
  }

  /**
   * Get purchased
   * @return purchased
  */
  @NotNull 
  @Schema(name = "purchased", required = true)
  public List<Integer> getPurchased() {
    return purchased;
  }

  public void setPurchased(List<Integer> purchased) {
    this.purchased = purchased;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TicketStatusDto ticketStatus = (TicketStatusDto) o;
    return Objects.equals(this.reserved, ticketStatus.reserved) &&
        Objects.equals(this.purchased, ticketStatus.purchased);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reserved, purchased);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TicketStatusDto {\n");
    sb.append("    reserved: ").append(toIndentedString(reserved)).append("\n");
    sb.append("    purchased: ").append(toIndentedString(purchased)).append("\n");
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

