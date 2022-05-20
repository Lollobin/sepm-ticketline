package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * SectorPriceDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SectorPriceDto   {

  @JsonProperty("sectorId")
  private Long sectorId;

  @JsonProperty("price")
  private Float price;

  public SectorPriceDto sectorId(Long sectorId) {
    this.sectorId = sectorId;
    return this;
  }

  /**
   * Get sectorId
   * @return sectorId
  */
  
  @Schema(name = "sectorId", required = false)
  public Long getSectorId() {
    return sectorId;
  }

  public void setSectorId(Long sectorId) {
    this.sectorId = sectorId;
  }

  public SectorPriceDto price(Float price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * @return price
  */
  
  @Schema(name = "price", required = false)
  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
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
    SectorPriceDto sectorPrice = (SectorPriceDto) o;
    return Objects.equals(this.sectorId, sectorPrice.sectorId) &&
        Objects.equals(this.price, sectorPrice.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sectorId, price);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SectorPriceDto {\n");
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

