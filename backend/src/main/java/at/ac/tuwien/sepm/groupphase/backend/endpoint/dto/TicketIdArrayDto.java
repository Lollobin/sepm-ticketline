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
 * TicketIdArrayDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class TicketIdArrayDto   {

  @JsonProperty("ticketIds")
  @Valid
  private List<Long> ticketIds = null;

  public TicketIdArrayDto ticketIds(List<Long> ticketIds) {
    this.ticketIds = ticketIds;
    return this;
  }

  public TicketIdArrayDto addTicketIdsItem(Long ticketIdsItem) {
    if (this.ticketIds == null) {
      this.ticketIds = new ArrayList<>();
    }
    this.ticketIds.add(ticketIdsItem);
    return this;
  }

  /**
   * Get ticketIds
   * @return ticketIds
  */
  
  @Schema(name = "ticketIds", required = false)
  public List<Long> getTicketIds() {
    return ticketIds;
  }

  public void setTicketIds(List<Long> ticketIds) {
    this.ticketIds = ticketIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TicketIdArrayDto ticketIdArray = (TicketIdArrayDto) o;
    return Objects.equals(this.ticketIds, ticketIdArray.ticketIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticketIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TicketIdArrayDto {\n");
    sb.append("    ticketIds: ").append(toIndentedString(ticketIds)).append("\n");
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

