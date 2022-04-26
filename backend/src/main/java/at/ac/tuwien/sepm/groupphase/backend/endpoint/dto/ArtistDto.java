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
 * ArtistDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-26T12:20:21.425982882+02:00[Europe/Vienna]")
public class ArtistDto   {

  @JsonProperty("artistId")
  private Integer artistId;

  @JsonProperty("bandName")
  private String bandName;

  @JsonProperty("knownAs")
  private String knownAs;

  @JsonProperty("firstName")
  private String firstName;

  @JsonProperty("lastName")
  private String lastName;

  public ArtistDto artistId(Integer artistId) {
    this.artistId = artistId;
    return this;
  }

  /**
   * Get artistId
   * @return artistId
  */
  @NotNull 
  @Schema(name = "artistId", required = true)
  public Integer getArtistId() {
    return artistId;
  }

  public void setArtistId(Integer artistId) {
    this.artistId = artistId;
  }

  public ArtistDto bandName(String bandName) {
    this.bandName = bandName;
    return this;
  }

  /**
   * Get bandName
   * @return bandName
  */
  
  @Schema(name = "bandName", required = false)
  public String getBandName() {
    return bandName;
  }

  public void setBandName(String bandName) {
    this.bandName = bandName;
  }

  public ArtistDto knownAs(String knownAs) {
    this.knownAs = knownAs;
    return this;
  }

  /**
   * Get knownAs
   * @return knownAs
  */
  
  @Schema(name = "knownAs", required = false)
  public String getKnownAs() {
    return knownAs;
  }

  public void setKnownAs(String knownAs) {
    this.knownAs = knownAs;
  }

  public ArtistDto firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
  */
  
  @Schema(name = "firstName", required = false)
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public ArtistDto lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
  */
  
  @Schema(name = "lastName", required = false)
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArtistDto artist = (ArtistDto) o;
    return Objects.equals(this.artistId, artist.artistId) &&
        Objects.equals(this.bandName, artist.bandName) &&
        Objects.equals(this.knownAs, artist.knownAs) &&
        Objects.equals(this.firstName, artist.firstName) &&
        Objects.equals(this.lastName, artist.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(artistId, bandName, knownAs, firstName, lastName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ArtistDto {\n");
    sb.append("    artistId: ").append(toIndentedString(artistId)).append("\n");
    sb.append("    bandName: ").append(toIndentedString(bandName)).append("\n");
    sb.append("    knownAs: ").append(toIndentedString(knownAs)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
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

