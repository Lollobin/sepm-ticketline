package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.GenderDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * UserDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-26T12:20:21.425982882+02:00[Europe/Vienna]")
public class UserDto   {

  @JsonProperty("userId")
  private Integer userId;

  @JsonProperty("firstName")
  private String firstName;

  @JsonProperty("lastName")
  private String lastName;

  @JsonProperty("email")
  private String email;

  @JsonProperty("gender")
  private GenderDto gender;

  @JsonProperty("street")
  private String street;

  @JsonProperty("zipCode")
  private String zipCode;

  @JsonProperty("city")
  private String city;

  @JsonProperty("country")
  private String country;

  @JsonProperty("isLocked")
  private Boolean isLocked;

  public UserDto userId(Integer userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
  */
  @NotNull 
  @Schema(name = "userId", required = true)
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public UserDto firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
  */
  @NotNull 
  @Schema(name = "firstName", required = true)
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public UserDto lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
  */
  @NotNull 
  @Schema(name = "lastName", required = true)
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public UserDto email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  */
  @NotNull 
  @Schema(name = "email", required = true)
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserDto gender(GenderDto gender) {
    this.gender = gender;
    return this;
  }

  /**
   * Get gender
   * @return gender
  */
  @NotNull @Valid 
  @Schema(name = "gender", required = true)
  public GenderDto getGender() {
    return gender;
  }

  public void setGender(GenderDto gender) {
    this.gender = gender;
  }

  public UserDto street(String street) {
    this.street = street;
    return this;
  }

  /**
   * Get street
   * @return street
  */
  @NotNull 
  @Schema(name = "street", required = true)
  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public UserDto zipCode(String zipCode) {
    this.zipCode = zipCode;
    return this;
  }

  /**
   * Get zipCode
   * @return zipCode
  */
  @NotNull 
  @Schema(name = "zipCode", required = true)
  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public UserDto city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Get city
   * @return city
  */
  @NotNull 
  @Schema(name = "city", required = true)
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public UserDto country(String country) {
    this.country = country;
    return this;
  }

  /**
   * Get country
   * @return country
  */
  @NotNull 
  @Schema(name = "country", required = true)
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public UserDto isLocked(Boolean isLocked) {
    this.isLocked = isLocked;
    return this;
  }

  /**
   * Get isLocked
   * @return isLocked
  */
  @NotNull 
  @Schema(name = "isLocked", required = true)
  public Boolean getIsLocked() {
    return isLocked;
  }

  public void setIsLocked(Boolean isLocked) {
    this.isLocked = isLocked;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDto user = (UserDto) o;
    return Objects.equals(this.userId, user.userId) &&
        Objects.equals(this.firstName, user.firstName) &&
        Objects.equals(this.lastName, user.lastName) &&
        Objects.equals(this.email, user.email) &&
        Objects.equals(this.gender, user.gender) &&
        Objects.equals(this.street, user.street) &&
        Objects.equals(this.zipCode, user.zipCode) &&
        Objects.equals(this.city, user.city) &&
        Objects.equals(this.country, user.country) &&
        Objects.equals(this.isLocked, user.isLocked);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, firstName, lastName, email, gender, street, zipCode, city, country, isLocked);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserDto {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    street: ").append(toIndentedString(street)).append("\n");
    sb.append("    zipCode: ").append(toIndentedString(zipCode)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    isLocked: ").append(toIndentedString(isLocked)).append("\n");
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

