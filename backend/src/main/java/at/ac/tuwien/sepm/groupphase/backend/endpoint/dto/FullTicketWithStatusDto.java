package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
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
 * FullTicketWithStatusDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-25T14:38:18.865520970+02:00[Europe/Vienna]")
public class FullTicketWithStatusDto   {

  @JsonProperty("reserved")
  @Valid
  private List<TicketDto> reserved = new ArrayList<>();

  @JsonProperty("purchased")
  @Valid
  private List<TicketDto> purchased = new ArrayList<>();

  public FullTicketWithStatusDto reserved(List<TicketDto> reserved) {
    this.reserved = reserved;
    return this;
  }

  public FullTicketWithStatusDto addReservedItem(TicketDto reservedItem) {
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
  @NotNull @Valid 
  @Schema(name = "reserved", required = true)
  public List<TicketDto> getReserved() {
    return reserved;
  }

  public void setReserved(List<TicketDto> reserved) {
    this.reserved = reserved;
  }

  public FullTicketWithStatusDto purchased(List<TicketDto> purchased) {
    this.purchased = purchased;
    return this;
  }

  public FullTicketWithStatusDto addPurchasedItem(TicketDto purchasedItem) {
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
  @NotNull @Valid 
  @Schema(name = "purchased", required = true)
  public List<TicketDto> getPurchased() {
    return purchased;
  }

  public void setPurchased(List<TicketDto> purchased) {
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
    FullTicketWithStatusDto fullTicketWithStatus = (FullTicketWithStatusDto) o;
    return Objects.equals(this.reserved, fullTicketWithStatus.reserved) &&
        Objects.equals(this.purchased, fullTicketWithStatus.purchased);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reserved, purchased);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FullTicketWithStatusDto {\n");
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

