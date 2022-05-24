import {Component, OnInit} from '@angular/core';
import {EventSearchResult} from "../../generated-sources/openapi";

@Component({
  selector: 'app-event-search',
  templateUrl: './event-search.component.html',
  styleUrls: ['./event-search.component.scss']
})
export class EventSearchComponent implements OnInit {
  err;
  name;
  description;
  category;
  duration;
  categoryService;
  categories;

  events: EventSearchResult;



  ngOnInit(): void {
    this.categories = this.categoryService.categoriesGet().subscribe(
        {
          next: resp => this.categories = resp,
          error: error => this.err = error
        }
    );
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
}
