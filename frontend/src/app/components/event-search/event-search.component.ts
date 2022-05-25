import {Component, OnInit} from '@angular/core';
import {EventSearch, EventSearchResult, EventsService} from "../../generated-sources/openapi";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-event-search',
  templateUrl: './event-search.component.html',
  styleUrls: ['./event-search.component.scss']
})
export class EventSearchComponent implements OnInit {
  page=0;
  pageSize=10;
  eventsResult: EventSearchResult;
  eventForm: FormGroup;
  err;
  categoryService;
  categories;
  events: EventSearchResult;

  constructor(private formBuilder: FormBuilder,private eventService: EventsService) {
    this.eventForm = this.formBuilder.group({
      name: [],
      category: [],
      duration: [],
      description: []
    });
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

  onSearch() {

    const search: EventSearch={
      name: this.name.value,
      category:this.category.value,
      duration:this.duration.value,
      content:this.description.value
    };
    console.log(search);

    this.eventService.eventsGet(search,this.pageSize,this.page).subscribe(
        {
          next: events => this.eventsResult=events,
          error: err => this.err=err
        }
    );
  }

  resetDuration(){
    this.eventForm.controls.duration.setValue(undefined);
  }

  handleEventPageEmit(number){
    this.page=number;
    this.onSearch();
  }
}
