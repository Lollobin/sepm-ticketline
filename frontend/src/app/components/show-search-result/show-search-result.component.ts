import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Location, Show, ShowSearchResult} from "../../generated-sources/openapi";

@Component({
  selector: 'app-show-search-result',
  templateUrl: './show-search-result.component.html',
  styleUrls: ['./show-search-result.component.scss']
})
export class ShowSearchResultComponent implements OnInit {

  @Output() nextRequestedPage = new EventEmitter<number>();
  @Input() shows: ShowSearchResult;
  @Input() location: Location;
  @Input() pageSize = 15;
  page = 1;
  _show: Show;


  constructor() {
  }

  get show(): Show {
    return this._show;
  }


  @Input() set show(value: Show) {
    this._show = value;
  }


  ngOnInit(): void {
    console.log(this.shows);
    console.log(this.shows.numberOfResults + " number of results");
    this.page = this.shows?.currentPage;

  }

  onPageChange(num: number) {

    this.nextRequestedPage.emit(num);
  }


}
