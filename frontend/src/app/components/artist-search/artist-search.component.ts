import {Component, OnInit} from '@angular/core';
import {
  Artist,
  ArtistsSearchResult,
  ArtistsService,
  EventSearch,
  EventSearchResult,
  EventsService
} from "../../generated-sources/openapi";
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-artist-search',
  templateUrl: './artist-search.component.html',
  styleUrls: ['./artist-search.component.scss']
})
export class ArtistSearchComponent implements OnInit{
  data: ArtistsSearchResult = null;
  page = 1;


  pageSize = 10;
  artists: Artist[];
  search: string;
  sort: 'ASC' | 'DESC' = 'ASC';
  eventsOfClickedArtist: EventSearchResult = null;
  clickedArtist: Artist;
  eventPageSize = 5;

  constructor(private artistService: ArtistsService, private eventsService: EventsService, 
    private toastrService: ToastrService) {
  }

  ngOnInit() {
    this.onSearch();
  }

  onSearch() {
    this.eventsOfClickedArtist = null;
    this.clickedArtist = null;
    return this.artistService.artistsGet(
        this.search,
        this.pageSize,
        this.page - 1
    ).subscribe({
          next: result => {
            this.data = result;
            this.artists = result.artists;

          },
          error: err => {
            console.log(err.error?.error);
            this.toastrService.error(err.errorMessage);
          }
        }
    );
  }

  onPageChange(ngbpage: number) {
    this.page = ngbpage;
    this.onSearch();
  }



  artistGetEvents(artist: Artist, childpage: number) {
    const id = artist.artistId;
    this.clickedArtist = artist;
    const searchParams: EventSearch = {artist: id};
    this.eventsService.eventsGet(searchParams, this.eventPageSize, childpage - 1).subscribe({
      next: response => {
        this.eventsOfClickedArtist = response;
      },
      error: err => {
        console.log(err.error?.error);
        this.toastrService.error(err.errorMessage);
      }

    });
  }

  handleEventPageEmit(number) {
    this.artistGetEvents(this.clickedArtist, number);
  }

  resetAll() {
    this.search=null;
    this.onSearch();
  }

}
