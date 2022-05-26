package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * ShowSearchDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ShowSearchDto   {

  @JsonProperty("event")
  private String event;

  @JsonProperty("date")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime date;

  @JsonProperty("price")
  private BigDecimal price;

  @JsonProperty("seatingPlan")
  private Long seatingPlan;

  public ShowSearchDto event(String event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
  */
  
  @Schema(name = "event", required = false)
  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public ShowSearchDto date(OffsetDateTime date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
  */
  @Valid 
  @Schema(name = "date", required = false)
  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  public ShowSearchDto price(BigDecimal price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * @return price
  */
  @Valid 
  @Schema(name = "price", required = false)
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public ShowSearchDto seatingPlan(Long seatingPlan) {
    this.seatingPlan = seatingPlan;
    return this;
  }

  /**
   * Get seatingPlan
   * @return seatingPlan
  */
  
  @Schema(name = "seatingPlan", required = false)
  public Long getSeatingPlan() {
    return seatingPlan;
  }

  public void setSeatingPlan(Long seatingPlan) {
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
    ShowSearchDto showSearch = (ShowSearchDto) o;
    return Objects.equals(this.event, showSearch.event) &&
        Objects.equals(this.date, showSearch.date) &&
        Objects.equals(this.price, showSearch.price) &&
        Objects.equals(this.seatingPlan, showSearch.seatingPlan);
  }

  @Override
  public int hashCode() {
    return Objects.hash(event, date, price, seatingPlan);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShowSearchDto {\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
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

