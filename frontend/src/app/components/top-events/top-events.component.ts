import {Component, OnInit} from "@angular/core";
import {
  Category,
  EventsService,
  EventWithTicketsSold,
  TopEventSearch
} from "../../generated-sources/openapi";

@Component({
  selector: "app-top-events",
  templateUrl: "./top-events.component.html",
  styleUrls: ["./top-events.component.scss"]
})
export class TopEventsComponent implements OnInit {

  error = undefined;
  topEvents: EventWithTicketsSold[];
  month: Date = null;
  category: Category = null;
  categories = [];
  categoriesType = Category;

  constructor(private eventsService: EventsService) {
  }

  ngOnInit(): void {
    this.categories = Object.keys(this.categoriesType);
    this.month = new Date(Date.now());
    this.reloadEvents();
  }

  reloadEvents() {
    const search: TopEventSearch = {
      month: this.month.toISOString().split("T")[0],
      category: this.category
    };

    this.eventsService.topEventsGet(search).subscribe({
      next: data => {
        this.topEvents = data;
      },
      error: error => {
        console.error("Error getting top events", error.message);
        this.setError(error);
      },
      complete: () => {
        console.log("Received events");
      }
    });
  }

  secondsToHms(d): string {
    d = Number(d * 60);
    const h = Math.floor(d / 3600);
    const m = Math.floor(d % 3600 / 60);
    const s = Math.floor(d % 3600 % 60);
    const hDisplay = h > 0 ? h + (h === 1 ? " hour" : " hours") + (m > 0 || s > 0 ? ", " : "") : "";
    const mDisplay = m > 0 ? m + (m === 1 ? " minute" : " minutes") + (s > 0 ? ", " : "") : "";
    const sDisplay = s > 0 ? s + (s === 1 ? " second" : " seconds") : "";
    return hDisplay + mDisplay + sDisplay;
  }

  setError(error: any) {
    this.error = error;
  }

}
