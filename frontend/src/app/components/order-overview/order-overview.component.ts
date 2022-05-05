import {Component, OnInit} from "@angular/core";
import {TicketsService, Transaction} from "../../generated-sources/openapi";

@Component({
  selector: "app-order-overview",
  templateUrl: "./order-overview.component.html",
  styleUrls: ["./order-overview.component.scss"]
})
export class OrderOverviewComponent implements OnInit {
  transactions: Transaction[];

  constructor(private ticketService: TicketsService) {
  }

  ngOnInit(): void {
    this.ticketService.ordersGet().subscribe({
      next: data => {
        this.transactions = data;
      },
      error: error => {
        console.error("Error getting orders", error.message);
      },
      complete: () => {
        console.log("Received orders");
      }
    });
  }
}
