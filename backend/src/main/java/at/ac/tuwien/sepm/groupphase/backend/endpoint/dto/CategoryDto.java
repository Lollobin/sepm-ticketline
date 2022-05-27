package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets Category
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public enum CategoryDto {
  
  CLASSICAL("Classical"),
  
  COUNTRY("Country"),
  
  EDM("EDM"),
  
  JAZZ("Jazz"),
  
  OLDIES("Oldies"),
  
  POP("Pop"),
  
  RAP("Rap"),
  
  RNB("RnB"),
  
  ROCK("Rock"),
  
  TECHNO("Techno");

  private String value;

  CategoryDto(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CategoryDto fromValue(String value) {
    for (CategoryDto b : CategoryDto.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

