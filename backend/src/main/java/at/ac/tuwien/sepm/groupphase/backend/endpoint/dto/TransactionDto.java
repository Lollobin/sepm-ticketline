package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.InvoiceItemDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * TransactionDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-25T14:38:18.865520970+02:00[Europe/Vienna]")
public class TransactionDto   {

  @JsonProperty("transactionId")
  private Integer transactionId;

  @JsonProperty("date")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime date;

  @JsonProperty("invoiceItems")
  @Valid
  private List<InvoiceItemDto> invoiceItems = new ArrayList<>();

  public TransactionDto transactionId(Integer transactionId) {
    this.transactionId = transactionId;
    return this;
  }

  /**
   * Get transactionId
   * @return transactionId
  */
  @NotNull 
  @Schema(name = "transactionId", required = true)
  public Integer getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(Integer transactionId) {
    this.transactionId = transactionId;
  }

  public TransactionDto date(OffsetDateTime date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
  */
  @NotNull @Valid 
  @Schema(name = "date", required = true)
  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  public TransactionDto invoiceItems(List<InvoiceItemDto> invoiceItems) {
    this.invoiceItems = invoiceItems;
    return this;
  }

  public TransactionDto addInvoiceItemsItem(InvoiceItemDto invoiceItemsItem) {
    if (this.invoiceItems == null) {
      this.invoiceItems = new ArrayList<>();
    }
    this.invoiceItems.add(invoiceItemsItem);
    return this;
  }

  /**
   * Get invoiceItems
   * @return invoiceItems
  */
  @NotNull @Valid 
  @Schema(name = "invoiceItems", required = true)
  public List<InvoiceItemDto> getInvoiceItems() {
    return invoiceItems;
  }

  public void setInvoiceItems(List<InvoiceItemDto> invoiceItems) {
    this.invoiceItems = invoiceItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionDto transaction = (TransactionDto) o;
    return Objects.equals(this.transactionId, transaction.transactionId) &&
        Objects.equals(this.date, transaction.date) &&
        Objects.equals(this.invoiceItems, transaction.invoiceItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionId, date, invoiceItems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionDto {\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    invoiceItems: ").append(toIndentedString(invoiceItems)).append("\n");
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

