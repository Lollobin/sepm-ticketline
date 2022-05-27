package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * UsersPageDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class UsersPageDto   {

  @JsonProperty("users")
  @Valid
  private List<UserDto> users = null;

  @JsonProperty("currentPage")
  private Integer currentPage;

  @JsonProperty("numberOfResults")
  private Integer numberOfResults;

  @JsonProperty("pagesTotal")
  private Integer pagesTotal;

  public UsersPageDto users(List<UserDto> users) {
    this.users = users;
    return this;
  }

  public UsersPageDto addUsersItem(UserDto usersItem) {
    if (this.users == null) {
      this.users = new ArrayList<>();
    }
    this.users.add(usersItem);
    return this;
  }

  /**
   * Get users
   * @return users
  */
  @Valid 
  @Schema(name = "users", required = false)
  public List<UserDto> getUsers() {
    return users;
  }

  public void setUsers(List<UserDto> users) {
    this.users = users;
  }

  public UsersPageDto currentPage(Integer currentPage) {
    this.currentPage = currentPage;
    return this;
  }

  /**
   * Get currentPage
   * @return currentPage
  */
  
  @Schema(name = "currentPage", required = false)
  public Integer getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(Integer currentPage) {
    this.currentPage = currentPage;
  }

  public UsersPageDto numberOfResults(Integer numberOfResults) {
    this.numberOfResults = numberOfResults;
    return this;
  }

  /**
   * Get numberOfResults
   * @return numberOfResults
  */
  
  @Schema(name = "numberOfResults", required = false)
  public Integer getNumberOfResults() {
    return numberOfResults;
  }

  public void setNumberOfResults(Integer numberOfResults) {
    this.numberOfResults = numberOfResults;
  }

  public UsersPageDto pagesTotal(Integer pagesTotal) {
    this.pagesTotal = pagesTotal;
    return this;
  }

  /**
   * Get pagesTotal
   * @return pagesTotal
  */
  
  @Schema(name = "pagesTotal", required = false)
  public Integer getPagesTotal() {
    return pagesTotal;
  }

  public void setPagesTotal(Integer pagesTotal) {
    this.pagesTotal = pagesTotal;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UsersPageDto usersPage = (UsersPageDto) o;
    return Objects.equals(this.users, usersPage.users) &&
        Objects.equals(this.currentPage, usersPage.currentPage) &&
        Objects.equals(this.numberOfResults, usersPage.numberOfResults) &&
        Objects.equals(this.pagesTotal, usersPage.pagesTotal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(users, currentPage, numberOfResults, pagesTotal);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UsersPageDto {\n");
    sb.append("    users: ").append(toIndentedString(users)).append("\n");
    sb.append("    currentPage: ").append(toIndentedString(currentPage)).append("\n");
    sb.append("    numberOfResults: ").append(toIndentedString(numberOfResults)).append("\n");
    sb.append("    pagesTotal: ").append(toIndentedString(pagesTotal)).append("\n");
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

