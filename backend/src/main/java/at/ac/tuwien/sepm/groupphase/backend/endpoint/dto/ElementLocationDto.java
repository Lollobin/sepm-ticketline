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
 * ElementLocationDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ElementLocationDto   {

  @JsonProperty("w")
  private BigDecimal w;

  @JsonProperty("h")
  private BigDecimal h;

  @JsonProperty("x")
  private BigDecimal x;

  @JsonProperty("y")
  private BigDecimal y;

  public ElementLocationDto w(BigDecimal w) {
    this.w = w;
    return this;
  }

  /**
   * Get w
   * @return w
  */
  @NotNull @Valid 
  @Schema(name = "w", required = true)
  public BigDecimal getW() {
    return w;
  }

  public void setW(BigDecimal w) {
    this.w = w;
  }

  public ElementLocationDto h(BigDecimal h) {
    this.h = h;
    return this;
  }

  /**
   * Get h
   * @return h
  */
  @NotNull @Valid 
  @Schema(name = "h", required = true)
  public BigDecimal getH() {
    return h;
  }

  public void setH(BigDecimal h) {
    this.h = h;
  }

  public ElementLocationDto x(BigDecimal x) {
    this.x = x;
    return this;
  }

  /**
   * Get x
   * @return x
  */
  @NotNull @Valid 
  @Schema(name = "x", required = true)
  public BigDecimal getX() {
    return x;
  }

  public void setX(BigDecimal x) {
    this.x = x;
  }

  public ElementLocationDto y(BigDecimal y) {
    this.y = y;
    return this;
  }

  /**
   * Get y
   * @return y
  */
  @NotNull @Valid 
  @Schema(name = "y", required = true)
  public BigDecimal getY() {
    return y;
  }

  public void setY(BigDecimal y) {
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ElementLocationDto elementLocation = (ElementLocationDto) o;
    return Objects.equals(this.w, elementLocation.w) &&
        Objects.equals(this.h, elementLocation.h) &&
        Objects.equals(this.x, elementLocation.x) &&
        Objects.equals(this.y, elementLocation.y);
  }

  @Override
  public int hashCode() {
    return Objects.hash(w, h, x, y);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ElementLocationDto {\n");
    sb.append("    w: ").append(toIndentedString(w)).append("\n");
    sb.append("    h: ").append(toIndentedString(h)).append("\n");
    sb.append("    x: ").append(toIndentedString(x)).append("\n");
    sb.append("    y: ").append(toIndentedString(y)).append("\n");
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

