import {Component, OnInit} from '@angular/core';
import {
  Category,
  EventSearch,
  EventSearchResult,
  EventsService
} from "../../generated-sources/openapi";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-event-search',
  templateUrl: './event-search.component.html',
  styleUrls: ['./event-search.component.scss']
})
export class EventSearchComponent implements OnInit {
  page = 1;
  pageSize = 10;
  noCategory = null;
  eventsResult: EventSearchResult;
  eventForm: FormGroup;
  err;
  errorMessage;
  categoriesType = Category;
  categories = [];


  events: EventSearchResult;

  constructor(private formBuilder: FormBuilder, private eventService: EventsService) {

    this.categories = Object.keys(this.categoriesType);
  }


  get name() {
    return this.eventForm.get("name");
  }

  get category() {
    return this.eventForm.get("category");
  }

  get duration() {
    return this.eventForm.get("duration");
  }

  get description() {
    return this.eventForm.get("description");
  }


  ngOnInit(): void {
    this.eventForm = this.formBuilder.group({
      name: [null],
      category: [this.noCategory],
      duration: [0],
      description: [null]
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

  onSearch() {

    const search: EventSearch = {
      name: this.name.value ? this.name.value : null,
      category: this.category.value ? this.category.value : null,
      duration: this.duration.value !== 0 ? this.duration.value : null,
      content: this.description.value ? this.description.value : null
    };
    this.eventService.eventsGet(search, this.pageSize, this.page - 1).subscribe(
        {
          next: events => {
            this.eventsResult = events;
          },
          error: err => {
            console.log('Could not fetch events: ');
            console.log(err);
            this.err = true;
            if (typeof err.error === 'object') {
              this.errorMessage = err.error.error;
            } else {
              this.errorMessage = err.error;
            }
          }
        }
    );
  }

  resetDuration() {
    this.eventForm.controls.duration.setValue(0);
  }

  handleEventPageEmit(number) {
    this.page = number;
    this.onSearch();
  }
}
