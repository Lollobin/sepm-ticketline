package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
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
 * EventSearchResultDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class EventSearchResultDto   {

  @JsonProperty("events")
  @Valid
  private List<EventDto> events = null;

  @JsonProperty("currentPage")
  private Integer currentPage;

  @JsonProperty("numberOfResults")
  private Integer numberOfResults;

  @JsonProperty("pagesTotal")
  private Integer pagesTotal;

  public EventSearchResultDto events(List<EventDto> events) {
    this.events = events;
    return this;
  }

  public EventSearchResultDto addEventsItem(EventDto eventsItem) {
    if (this.events == null) {
      this.events = new ArrayList<>();
    }
    this.events.add(eventsItem);
    return this;
  }

  /**
   * Get events
   * @return events
  */
  @Valid 
  @Schema(name = "events", required = false)
  public List<EventDto> getEvents() {
    return events;
  }

  public void setEvents(List<EventDto> events) {
    this.events = events;
  }

  public EventSearchResultDto currentPage(Integer currentPage) {
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

  public EventSearchResultDto numberOfResults(Integer numberOfResults) {
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

  public EventSearchResultDto pagesTotal(Integer pagesTotal) {
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
    EventSearchResultDto eventSearchResult = (EventSearchResultDto) o;
    return Objects.equals(this.events, eventSearchResult.events) &&
        Objects.equals(this.currentPage, eventSearchResult.currentPage) &&
        Objects.equals(this.numberOfResults, eventSearchResult.numberOfResults) &&
        Objects.equals(this.pagesTotal, eventSearchResult.pagesTotal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(events, currentPage, numberOfResults, pagesTotal);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventSearchResultDto {\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
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

