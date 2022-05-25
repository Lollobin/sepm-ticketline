import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Artist, EventSearchResult} from "../../generated-sources/openapi";

@Component({
  selector: 'app-event-search-result',
  templateUrl: './event-search-result.component.html',
  styleUrls: ['./event-search-result.component.scss']
})
export class EventSearchResultComponent implements OnInit {
  @Output() nextRequestedPage = new EventEmitter<number>();
  @Input() events: EventSearchResult;
  @Input() location?: Location;
  @Input() pageSize = 15;
  page = 1;
  _artist: Artist;

  get artist(): Artist {
    return this._artist;
  }

  @Input() set artist(value: Artist) {
    this._artist = value;
    this.ngOnInit();
  }

  ngOnInit() {
    console.log(this.events?.currentPage);
    this.page = this.events?.currentPage;
  }


  onPageChange(num: number) {
    console.log("page change?" + num);
    this.nextRequestedPage.emit(num);
  }

}
