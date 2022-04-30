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
 * SectorDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SectorDto   {

  @JsonProperty("sectorId")
  private BigDecimal sectorId;

  @JsonProperty("price")
  private BigDecimal price;

  public SectorDto sectorId(BigDecimal sectorId) {
    this.sectorId = sectorId;
    return this;
  }

  /**
   * Get sectorId
   * @return sectorId
  */
  @NotNull @Valid 
  @Schema(name = "sectorId", required = true)
  public BigDecimal getSectorId() {
    return sectorId;
  }

  public void setSectorId(BigDecimal sectorId) {
    this.sectorId = sectorId;
  }

  public SectorDto price(BigDecimal price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * @return price
  */
  @NotNull @Valid 
  @Schema(name = "price", required = true)
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SectorDto sector = (SectorDto) o;
    return Objects.equals(this.sectorId, sector.sectorId) &&
        Objects.equals(this.price, sector.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sectorId, price);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SectorDto {\n");
    sb.append("    sectorId: ").append(toIndentedString(sectorId)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
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

