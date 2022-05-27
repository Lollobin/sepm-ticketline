import {Component} from '@angular/core';
import {
  Artist,
  ArtistsSearchResult,
  ArtistsService,
  Category,
  EventSearch,
  EventSearchResult,
  EventsService
} from "../../generated-sources/openapi";

@Component({
  selector: 'app-artist-search',
  templateUrl: './artist-search.component.html',
  styleUrls: ['./artist-search.component.scss']
})
export class ArtistSearchComponent {
  data: ArtistsSearchResult = null;
  page = 1;
  //rest expects page values beginning with 0

  pageSize = 10;
  artists: Artist[];
  search: string;
  sort: 'ASC' | 'DESC' = 'ASC';
  eventsOfClickedArtist: EventSearchResult = null;
  clickedArtist: Artist;


  constructor(private artistService: ArtistsService, private eventsService: EventsService) {
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
          }
        }
    );
  }

  onPageChange(ngbpage: number) {
    this.page = ngbpage;
    this.onSearch();
  }


//faker until eventsearch is done
  fakeEvents(artist: Artist, eventPage: number) {
    console.log("a new page was requeted" + eventPage);
    const imp = artist.firstName;
    this.clickedArtist = artist;
    this.eventsOfClickedArtist = {
      currentPage: 1,
      numberOfResults: 6,
      events: [{
        eventId: 1,
        name: imp,
        category: Category.Classical,
        duration: 3
      }, {eventId: 2, name: imp + '2', category: Category.Classical, duration: 3}, {
        eventId: 4,
        name: imp,
        category: Category.Classical,
        duration: 3
      }, {eventId: 5, name: imp + '2', category: Category.Classical, duration: 3}, {
        eventId: 1,
        name: imp,
        category: Category.Classical,
        duration: 3
      }, {eventId: 6, name: imp + '2', category: Category.Classical, duration: 3},]
    };

  }

//real implementation, not yet in use
  artistGetEvents(artist: Artist, page: number) {
    const id = artist.artistId;
    this.clickedArtist = artist;
    const searchParams: EventSearch = {artist: id};
    this.eventsService.eventsGet(searchParams, 15, page).subscribe({
      next: response => this.eventsOfClickedArtist = response,
      error: err => {
        console.log(err.error?.error);
      }

    });
  }

  handleEventPageEmit(number){
    console.log("hndleemit"+number);
   //here we will call artistsGetEvents(this.clickedArtist,number)
  }

}
