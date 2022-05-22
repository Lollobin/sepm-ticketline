package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
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
 * ArtistsSearchResultDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ArtistsSearchResultDto   {

  @JsonProperty("artists")
  @Valid
  private List<ArtistDto> artists = null;

  @JsonProperty("currentPage")
  private Integer currentPage;

  @JsonProperty("numberOfResults")
  private Integer numberOfResults;

  @JsonProperty("pagesTotal")
  private Integer pagesTotal;

  public ArtistsSearchResultDto artists(List<ArtistDto> artists) {
    this.artists = artists;
    return this;
  }

  public ArtistsSearchResultDto addArtistsItem(ArtistDto artistsItem) {
    if (this.artists == null) {
      this.artists = new ArrayList<>();
    }
    this.artists.add(artistsItem);
    return this;
  }

  /**
   * Get artists
   * @return artists
  */
  @Valid 
  @Schema(name = "artists", required = false)
  public List<ArtistDto> getArtists() {
    return artists;
  }

  public void setArtists(List<ArtistDto> artists) {
    this.artists = artists;
  }

  public ArtistsSearchResultDto currentPage(Integer currentPage) {
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

  public ArtistsSearchResultDto numberOfResults(Integer numberOfResults) {
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

  public ArtistsSearchResultDto pagesTotal(Integer pagesTotal) {
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
    ArtistsSearchResultDto artistsSearchResult = (ArtistsSearchResultDto) o;
    return Objects.equals(this.artists, artistsSearchResult.artists) &&
        Objects.equals(this.currentPage, artistsSearchResult.currentPage) &&
        Objects.equals(this.numberOfResults, artistsSearchResult.numberOfResults) &&
        Objects.equals(this.pagesTotal, artistsSearchResult.pagesTotal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(artists, currentPage, numberOfResults, pagesTotal);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ArtistsSearchResultDto {\n");
    sb.append("    artists: ").append(toIndentedString(artists)).append("\n");
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

