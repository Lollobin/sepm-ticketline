package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * TopShowSearchDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-27T17:50:38.012857315+02:00[Europe/Vienna]")
public class TopShowSearchDto   {

  @JsonProperty("category")
  private String category;

  @JsonProperty("month")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate month;

  public TopShowSearchDto category(String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
  */
  @NotNull 
  @Schema(name = "category", required = true)
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public TopShowSearchDto month(LocalDate month) {
    this.month = month;
    return this;
  }

  /**
   * Get month
   * @return month
  */
  @NotNull @Valid 
  @Schema(name = "month", required = true)
  public LocalDate getMonth() {
    return month;
  }

  public void setMonth(LocalDate month) {
    this.month = month;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TopShowSearchDto topShowSearch = (TopShowSearchDto) o;
    return Objects.equals(this.category, topShowSearch.category) &&
        Objects.equals(this.month, topShowSearch.month);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, month);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopShowSearchDto {\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    month: ").append(toIndentedString(month)).append("\n");
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

