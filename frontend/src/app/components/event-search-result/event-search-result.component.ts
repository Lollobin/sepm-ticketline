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
  @Input() pageSize;
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
    this.page = this.events?.currentPage + 1;
  }

  onPageChange(num: number) {
    this.nextRequestedPage.emit(num);
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
