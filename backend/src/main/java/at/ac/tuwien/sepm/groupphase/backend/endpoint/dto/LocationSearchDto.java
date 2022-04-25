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
 * LocationSearchDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-25T14:38:18.865520970+02:00[Europe/Vienna]")
public class LocationSearchDto   {

  @JsonProperty("name")
  private String name;

  @JsonProperty("street")
  private String street;

  @JsonProperty("city")
  private String city;

  @JsonProperty("country")
  private String country;

  @JsonProperty("zipCode")
  private String zipCode;

  public LocationSearchDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Finds locations that contains the given string in their name
   * @return name
  */
  
  @Schema(name = "name", description = "Finds locations that contains the given string in their name", required = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocationSearchDto street(String street) {
    this.street = street;
    return this;
  }

  /**
   * Finds locations that contains the given string in their street
   * @return street
  */
  
  @Schema(name = "street", description = "Finds locations that contains the given string in their street", required = false)
  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public LocationSearchDto city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Finds locations that contains the given string in their city
   * @return city
  */
  
  @Schema(name = "city", description = "Finds locations that contains the given string in their city", required = false)
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public LocationSearchDto country(String country) {
    this.country = country;
    return this;
  }

  /**
   * Finds locations that contains the given string in their country
   * @return country
  */
  
  @Schema(name = "country", description = "Finds locations that contains the given string in their country", required = false)
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public LocationSearchDto zipCode(String zipCode) {
    this.zipCode = zipCode;
    return this;
  }

  /**
   * Finds locations that contains the given string in their zipCode
   * @return zipCode
  */
  
  @Schema(name = "zipCode", description = "Finds locations that contains the given string in their zipCode", required = false)
  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationSearchDto locationSearch = (LocationSearchDto) o;
    return Objects.equals(this.name, locationSearch.name) &&
        Objects.equals(this.street, locationSearch.street) &&
        Objects.equals(this.city, locationSearch.city) &&
        Objects.equals(this.country, locationSearch.country) &&
        Objects.equals(this.zipCode, locationSearch.zipCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, street, city, country, zipCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationSearchDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    street: ").append(toIndentedString(street)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    zipCode: ").append(toIndentedString(zipCode)).append("\n");
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

