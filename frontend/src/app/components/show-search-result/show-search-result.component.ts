import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Event, ShowSearchResult, ShowsService} from "../../generated-sources/openapi";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-show-search-result',
  templateUrl: './show-search-result.component.html',
  styleUrls: ['./show-search-result.component.scss']
})
export class ShowSearchResultComponent implements OnInit {
  @Output() nextRequestedPage = new EventEmitter<number>();
  @Input() shows: ShowSearchResult;
  @Input() pageSize = 15;
  page = 1;
  _event: Event;
  error;

  constructor(private route: ActivatedRoute, private showService: ShowsService) {
  }

  get event(): Event {
    return this._event;
  }

  @Input() set event(value: Event) {
    this._event = value;
    this.ngOnInit();
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this._event.eventId = params["id"];
    });
    if (this.event) {
    }
//TODO: add eventId as a possible search parameter for showSearch
    /*
    this.showService.showsGet({event:this.event}, this.pageSize,page-1).subscribe(
        {
          next: result=> this.shows =result,
          error: err => this.error= err
        }
    )
  }*/
  }

  onPageChange(num: number) {
    console.log("page change?" + num);
    this.nextRequestedPage.emit(num);
  }
}
