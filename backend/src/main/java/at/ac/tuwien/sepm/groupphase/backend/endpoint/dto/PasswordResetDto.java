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
 * PasswordResetDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class PasswordResetDto   {

  @JsonProperty("email")
  private String email;

  @JsonProperty("clientURI")
  private String clientURI;

  public PasswordResetDto email(String email) {
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

  public PasswordResetDto clientURI(String clientURI) {
    this.clientURI = clientURI;
    return this;
  }

  /**
   * Get clientURI
   * @return clientURI
  */
  @NotNull 
  @Schema(name = "clientURI", required = true)
  public String getClientURI() {
    return clientURI;
  }

  public void setClientURI(String clientURI) {
    this.clientURI = clientURI;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PasswordResetDto passwordReset = (PasswordResetDto) o;
    return Objects.equals(this.email, passwordReset.email) &&
        Objects.equals(this.clientURI, passwordReset.clientURI);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, clientURI);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PasswordResetDto {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    clientURI: ").append(toIndentedString(clientURI)).append("\n");
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

