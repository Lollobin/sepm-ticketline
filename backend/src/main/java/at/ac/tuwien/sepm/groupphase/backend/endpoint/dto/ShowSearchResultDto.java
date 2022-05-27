package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * ShowSearchResultDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ShowSearchResultDto   {

  @JsonProperty("shows")
  @Valid
  private List<ShowDto> shows = null;

  @JsonProperty("locationId")
  private BigDecimal locationId;

  @JsonProperty("currentPage")
  private Integer currentPage;

  @JsonProperty("numberOfResults")
  private Integer numberOfResults;

  @JsonProperty("pagesTotal")
  private Integer pagesTotal;

  public ShowSearchResultDto shows(List<ShowDto> shows) {
    this.shows = shows;
    return this;
  }

  public ShowSearchResultDto addShowsItem(ShowDto showsItem) {
    if (this.shows == null) {
      this.shows = new ArrayList<>();
    }
    this.shows.add(showsItem);
    return this;
  }

  /**
   * Get shows
   * @return shows
  */
  @Valid 
  @Schema(name = "shows", required = false)
  public List<ShowDto> getShows() {
    return shows;
  }

  public void setShows(List<ShowDto> shows) {
    this.shows = shows;
  }

  public ShowSearchResultDto locationId(BigDecimal locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * Get locationId
   * @return locationId
  */
  @Valid 
  @Schema(name = "locationId", required = false)
  public BigDecimal getLocationId() {
    return locationId;
  }

  public void setLocationId(BigDecimal locationId) {
    this.locationId = locationId;
  }

  public ShowSearchResultDto currentPage(Integer currentPage) {
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

  public ShowSearchResultDto numberOfResults(Integer numberOfResults) {
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

  public ShowSearchResultDto pagesTotal(Integer pagesTotal) {
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
    ShowSearchResultDto showSearchResult = (ShowSearchResultDto) o;
    return Objects.equals(this.shows, showSearchResult.shows) &&
        Objects.equals(this.locationId, showSearchResult.locationId) &&
        Objects.equals(this.currentPage, showSearchResult.currentPage) &&
        Objects.equals(this.numberOfResults, showSearchResult.numberOfResults) &&
        Objects.equals(this.pagesTotal, showSearchResult.pagesTotal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shows, locationId, currentPage, numberOfResults, pagesTotal);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShowSearchResultDto {\n");
    sb.append("    shows: ").append(toIndentedString(shows)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
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

