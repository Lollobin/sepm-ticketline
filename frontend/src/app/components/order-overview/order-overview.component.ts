import {Component, OnInit} from "@angular/core";
import {Order, TicketsService} from "../../generated-sources/openapi";

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
    this.ticketService.ordersGet().subscribe({
      next: data => {
        this.orders = data;
      },
      error: error => {
        console.error("Error getting orders", error.message);
      },
      complete: () => {
        console.log("Received orders");
      }
    });
  }

  dateInFuture(date: string) {
    const orderDate = new Date(date);
    return orderDate > this.today;
  }
}
