package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
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
 * OrdersPageDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class OrdersPageDto   {

  @JsonProperty("orders")
  @Valid
  private List<OrderDto> orders = null;

  @JsonProperty("currentPage")
  private Integer currentPage;

  @JsonProperty("numberOfResults")
  private Integer numberOfResults;

  @JsonProperty("pagesTotal")
  private Integer pagesTotal;

  public OrdersPageDto orders(List<OrderDto> orders) {
    this.orders = orders;
    return this;
  }

  public OrdersPageDto addOrdersItem(OrderDto ordersItem) {
    if (this.orders == null) {
      this.orders = new ArrayList<>();
    }
    this.orders.add(ordersItem);
    return this;
  }

  /**
   * Get orders
   * @return orders
  */
  @Valid 
  @Schema(name = "orders", required = false)
  public List<OrderDto> getOrders() {
    return orders;
  }

  public void setOrders(List<OrderDto> orders) {
    this.orders = orders;
  }

  public OrdersPageDto currentPage(Integer currentPage) {
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

  public OrdersPageDto numberOfResults(Integer numberOfResults) {
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

  public OrdersPageDto pagesTotal(Integer pagesTotal) {
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
    OrdersPageDto ordersPage = (OrdersPageDto) o;
    return Objects.equals(this.orders, ordersPage.orders) &&
        Objects.equals(this.currentPage, ordersPage.currentPage) &&
        Objects.equals(this.numberOfResults, ordersPage.numberOfResults) &&
        Objects.equals(this.pagesTotal, ordersPage.pagesTotal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orders, currentPage, numberOfResults, pagesTotal);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrdersPageDto {\n");
    sb.append("    orders: ").append(toIndentedString(orders)).append("\n");
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

