import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  EventsService, Sector, ShowsService, SeatingPlansService,
  SectorPrice, ShowWithoutId, LocationsService, LocationSearch, Location,
  ArtistsService, Artist, ShowSearch, SeatingPlanLayout, SeatingPlanSector
} from 'src/app/generated-sources/openapi';
import { faCircleQuestion, faUserMinus } from "@fortawesome/free-solid-svg-icons";
import { CustomAuthService } from "../../services/custom-auth.service";
import { debounceTime, distinctUntilChanged, map, Observable, switchMap } from 'rxjs';
import { Application } from 'pixi.js';
import { drawSeatingPlanPreview } from 'src/app/shared_modules/seatingPlanGraphics';
import { dateTimeValidator } from './date-time-validator';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-create-show',
  templateUrl: './create-show.component.html',
  styleUrls: ['./create-show.component.scss']
})
export class CreateShowComponent implements OnInit, AfterViewInit {
  @ViewChild("pixiContainer") pixiContainer: ElementRef<HTMLDivElement>;
  @ViewChild("infoOverlay") infoOverlay: ElementRef<HTMLDivElement>;

  eventId: number;
  eventName: string;
  eventCategory: string;
  eventDuration: number;
  eventDescription: string;
  showWithoutId: ShowWithoutId = {
    date: "",
    event: 0,
    artists: [],
    seatingPlan: 0,
    sectorPrices: []
  };
  sectors: SeatingPlanSector[];
  showForm: any;
  sectorForm = this.formBuilder.group({
    ignore: ["", [Validators.required]]
  });
  sectorPrice: SectorPrice = { price: 0, sectorId: 0 };
  notFound = true;
  role = '';
  faCircleQuestion = faCircleQuestion;
  faUserMinus = faUserMinus;
  sectorString = "sector";
  gotFromSeatingPlan: number;
  locationSearchDto: LocationSearch = {};
  seatingPlans = [];
  artist: Artist;
  artistForm: any;
  artists = [];
  display = "none";
  shows = [];
  showSearch: ShowSearch = { event: null };
  submitted = false;
  seatingPlanLayout: SeatingPlanLayout;
  pixiApplication: Application;

  constructor(private formBuilder: FormBuilder, private showService: ShowsService, private eventService: EventsService,
    private route: ActivatedRoute, private authService: CustomAuthService, private seatingPlansService: SeatingPlansService,
    private locationsService: LocationsService, private artistsService: ArtistsService, private router: Router,
    private toastr: ToastrService) {
    this.showForm = this.formBuilder.group({
      date: ['', [Validators.required]],
      time: ['', [Validators.required]],
      event: [],
      location: ['', [Validators.required]],
      seatingPlan: ['', [Validators.required]],
      sectorPrices: []
    }, {
      validators: dateTimeValidator,
    }
    );
  }

  get date() {
    return this.showForm.get("date");
  }

  get time() {
    return this.showForm.get("time");
  }

  get validForms() {
    return this.showForm.valid && this.sectorForm.valid;
  }

  get artistFormValid() {
    if (!this.artistForm.get("artist").value) {
      return false;
    }
    for (const element of this.artists) {
      if (element.artistId === this.artistForm.get("artist").value.artistId) {
        return false;
      }
    }
    if (this.artistForm.get("artist").value.firstName || this.artistForm.get("artist").value.lastName
      || this.artistForm.get("artist").value.knownAs || this.artistForm.get("artist").value.bandName) {
      return true;
    }
    return false;
  }

  get locationValid() {
    return this.showForm.get("location").valid && this.showForm.get("location").value.locationId;
  }

  get seatingPlan() {
    return this.showForm.get("seatingPlan");
  }

  ngOnInit(): void {
    this.artistForm = this.formBuilder.group({
      artist: []
    });
    this.route.params.subscribe(params => {
      this.eventId = params["id"];
      this.showForm.value.id = this.eventId;
      this.notFound = true;
      this.getDetails(this.eventId);
    });
    this.getShowsOfEvent(this.eventId);
    this.role = this.authService.getUserRole();
    this.showForm.get('location').valueChanges.subscribe(val => {
      if (val && val.locationId) {
        this.getSeatingPlansOfLocation(val.locationId);
      }
    });
    this.showForm.get('seatingPlan').valueChanges.subscribe(val => {
      if (val && val.seatingPlanId) {
        this.getSeatingPlanLayout(val.seatingPlanId);
      }
    });
  }

  ngAfterViewInit() {
    this.pixiApplication = new Application({
      antialias: true,
      backgroundAlpha: 0,
    });
  }

  openModal() {
    if (!this.showForm.valid || !this.sectorForm.valid || !this.sectors || this.gotFromSeatingPlan
      !== this.showForm.value.seatingPlan.seatingPlanId) {
      this.submitted = true;
    } else {
      this.display = "block";
    }
  }
  onCloseHandled() {
    this.display = "none";
  }

  getDetails(id: number): void {
    this.eventService.eventsIdGet(this.eventId).subscribe({
      next: data => {
        console.log("Succesfully got event with id", id);
        this.eventName = data.name;
        this.eventCategory = data.category;
        this.eventDuration = data.duration;
        this.eventDescription = data.content;
        this.notFound = false;
      },
      error: error => {
        console.log("Error getting event", error);
        if (error.status === 0 || error.status === 500) {
          this.toastr.error(error.message);
        } else {
          this.toastr.warning(error.error);
        }
      }
    });
  }

  getShowsOfEvent(id: number): void {
    this.showSearch.eventId = id;
    this.showService.showsGet(this.showSearch).subscribe({
      next: data => {
        console.log("Succesfully got shows of event with id " + id);
        this.shows = data.shows;
        console.log(data.shows);
      },
      error: error => {
        console.log("Error getting shows", error);
        if (error.status === 0 || error.status === 500) {
          this.toastr.error(error.message);
        } else {
          this.toastr.warning(error.error);
        }
      }
    });
  }

  createShow(): void {
    this.showForm.value.event = this.eventId;
    if (this.showForm.value.date.length !== 25) {
      this.showForm.value.date = this.showForm.value.date + "T" + this.showForm.value.time + ":00+02:00";
    }
    this.showWithoutId.date = this.showForm.value.date;
    this.showWithoutId.event = this.eventId;
    for (let i = 0; i < this.sectors.length; i++) {
      this.showWithoutId.sectorPrices[i] = { price: 0, sectorId: 0 };
      this.showWithoutId.sectorPrices[i].price = this.sectorForm.get("sector" + i).value;
      this.showWithoutId.sectorPrices[i].sectorId = this.sectors[i].id;
    }
    this.showWithoutId.artists = this.showForm.value.artists;
    this.showWithoutId.seatingPlan = this.showForm.value.seatingPlan.seatingPlanId;
    this.showWithoutId.artists = [];
    for (const artist of this.artists) {
      this.showWithoutId.artists.push(artist.artistId);
    }
    console.log("POST http://localhost:8080/shows " + JSON.stringify(this.showWithoutId));
    this.showService.showsPost(this.showWithoutId, 'response').subscribe({
      next: data => {
        console.log("Succesfully created show");
        console.log(data.headers.get('Location'));
        this.getShowsOfEvent(this.eventId);
        this.clearForm();
        this.submitted = false;
        this.toastr.success("Succesfully added show!");
      },
      error: error => {
        console.log("Error creating event", error);
        if (error.status === 0 || error.status === 500) {
          this.toastr.error(error.message);
        } else {
          this.toastr.warning(error.error);
        }
      }
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

  clearForm() {
    this.showForm.reset();
    this.sectorForm.reset();
  }

  createGroup() {
    const group = this.formBuilder.group({});
    let i = 0;
    this.sectors.forEach(control => group.addControl("sector" + i++, this.formBuilder.control('', Validators.required)));
    return group;
  }

  getSeatingPlanLayout(id: number) {
    this.seatingPlansService
      .seatingPlanLayoutsIdGet(id)
      .subscribe({
        next: async (seatingPlanLayout) => {
          this.seatingPlanLayout = seatingPlanLayout;
          this.sectors = seatingPlanLayout.sectors;
          this.sectorForm = this.createGroup();
          this.gotFromSeatingPlan = id;
          this.showWithoutId.sectorPrices = [];
          this.initializeSeatingPlan();
        },
        error: error => {
          console.log("Error getting sectors of seatingPlanLayout with id", id);
          if (error.status === 0 || error.status === 500) {
            this.toastr.error(error.message);
          } else {
            this.toastr.warning(error.error);
          }
        }
      });
  }

  getSeatingPlansOfLocation(id: number) {
    if (id === null) {
      return;
    }
    this.locationsService.locationsIdSeatingPlansGet(id).subscribe({
      next: data => {
        console.log("Succesfully got seating plans of location with id", id);
        this.seatingPlans = data;
      },
      error: error => {
        console.log("Error getting seating plans of location with id", id);
        if (error.status === 0 || error.status === 500) {
          this.toastr.error(error.message);
        } else {
          this.toastr.warning(error.error);
        }
      }
    });
  }

  locationSearch = (text$: Observable<string>) => text$.pipe(
    debounceTime(200),
    distinctUntilChanged(),
    switchMap((search: string) => this.locationsService.locationsGet(this.getLocationSearch(search)).pipe(
      map(locationResult => locationResult.locations)
    )
    )
  );

  getLocationSearch(search: string) {
    this.locationSearchDto.name = search;
    return this.locationSearchDto;
  }

  locationFormatter(location: Location) {
    return location.name;
  }

  artistSearch = (text$: Observable<string>) => text$.pipe(
    debounceTime(200),
    distinctUntilChanged(),
    switchMap((search: string) => this.artistsService.artistsGet(search).pipe(
      map(artistResult => artistResult.artists)
    )
    )
  );

  artistResultFormatter(artist: any) {
    return (artist.firstName + " '" + artist.knownAs + "' " + artist.lastName);
  }

  artistInputFormatter(artist: any) {
    return (artist.firstName + " '" + artist.knownAs + "' " + artist.lastName);
  }

  addArtist() {
    this.artists.push(this.artistForm.get("artist").value);
    console.log(this.artists);
    this.artistForm.reset();
  }

  goToHome() {
    this.router.navigateByUrl("/admin");
  }

  removeFromArtists(artist: Artist) {
    for (let i = 0; i < this.artists.length; i++) {
      if (this.artists[i].artistId === artist.artistId) {
        this.artists.splice(i, 1);
        break;
      }
    }
  }

  initializeSeatingPlan() {
    this.pixiApplication.stage.removeChildren();
    this.pixiApplication.view.width = this.seatingPlanLayout.general.width;
    this.pixiApplication.view.height = this.seatingPlanLayout.general.height;
    document.addEventListener("mousemove", (event) => event);
    this.pixiContainer.nativeElement.appendChild(this.pixiApplication.view);
    drawSeatingPlanPreview(this.pixiApplication.stage, this.seatingPlanLayout);
  }

  numberToCssColorString(color: number) {
    return `#${color.toString(16).padStart(6, "0")}`;
  }

  convertToCurrency(value: number) {
    return value.toLocaleString(undefined, { style: "currency", currency: "EUR" });
  }
}
