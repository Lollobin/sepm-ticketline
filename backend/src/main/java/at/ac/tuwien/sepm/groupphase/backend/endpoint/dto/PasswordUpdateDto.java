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
 * PasswordUpdateDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class PasswordUpdateDto   {

  @JsonProperty("newPassword")
  private String newPassword;

  @JsonProperty("token")
  private String token;

  public PasswordUpdateDto newPassword(String newPassword) {
    this.newPassword = newPassword;
    return this;
  }

  /**
   * Get newPassword
   * @return newPassword
  */
  @NotNull 
  @Schema(name = "newPassword", required = true)
  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public PasswordUpdateDto token(String token) {
    this.token = token;
    return this;
  }

  /**
   * Get token
   * @return token
  */
  @NotNull 
  @Schema(name = "token", required = true)
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PasswordUpdateDto passwordUpdate = (PasswordUpdateDto) o;
    return Objects.equals(this.newPassword, passwordUpdate.newPassword) &&
        Objects.equals(this.token, passwordUpdate.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newPassword, token);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PasswordUpdateDto {\n");
    sb.append("    newPassword: ").append(toIndentedString(newPassword)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
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

