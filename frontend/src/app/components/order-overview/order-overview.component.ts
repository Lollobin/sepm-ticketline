import {Component, OnInit} from "@angular/core";
import {Order, TicketsService} from "../../generated-sources/openapi";
import sampleOrders from "./sampleOrders.json";

@Component({
  selector: "app-order-overview",
  templateUrl: "./order-overview.component.html",
  styleUrls: ["./order-overview.component.scss"]
})
export class OrderOverviewComponent implements OnInit {
  orders: Order[];
  today = new Date();

  constructor(private ticketService: TicketsService) {
  }

  ngOnInit(): void {
    // TODO: Add retreival of necessary data here (when backend is implemented)
    // @ts-ignore
    this.orders = sampleOrders;
    /*
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
     */
  }

  dateInFuture(date: string) {
    const orderDate = new Date(date);
    return orderDate > this.today;
  }
}
