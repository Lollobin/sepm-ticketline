import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {
  Location,
  Show,
  ShowSearchResult,
  ShowsService
} from "../../generated-sources/openapi";
import {ActivatedRoute} from "@angular/router";
import { ToastrService } from 'ngx-toastr';

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
  @Input() empty = null;
  page = 1;
  _show: Show;

  eventId?;
  eventName;
  eventDescription;


  constructor(private showService: ShowsService,
              private route: ActivatedRoute, private toastr: ToastrService) {

  }

  get show(): Show {
    return this._show;
  }


  @Input() set show(value: Show) {
    this._show = value;
  }


  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.eventId = params["id"];
    });
    if (this.eventId) {
      this.showService.showsGet({eventId: this.eventId}, this.pageSize, this.page - 1).subscribe({
        next: response => {
          this.shows = response;
          console.log(response);
          this.eventName = this.shows.shows[0]?.event.name;
          this.eventDescription = this.shows.shows[0]?.event.content;
          if (!this.shows?.numberOfResults) {
            this.toastr.info("There aren't any shows fitting your input!");
          }
        },
        error: (error) => {
          console.log(error);
          if (error.status === 0 || error.status === 500) {
            this.toastr.error(error.message);
          } else {
            this.toastr.warning(error.error);
          }
        }
      });
    } else {
      console.log(this.shows);
      console.log(this.shows.numberOfResults + " number of results");
    }
    this.page = this.shows?.currentPage;

  }

  onPageChange(num: number) {

    this.nextRequestedPage.emit(num);
  }

  public vanishEmpty(): void {
    this.empty = null;
  }


}
