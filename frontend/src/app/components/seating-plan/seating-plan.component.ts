import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from "@angular/core";
import {countBy, find, groupBy, map, noop} from "lodash";
import {Application} from "pixi.js";
import {
  Artist,
  ArtistsService,
  Event,
  EventsService,
  SeatingPlanLayout,
  SeatingPlansService,
  SeatWithBookingStatus,
  Show,
  ShowInformation,
  ShowsService,
  TicketsService,
} from "src/app/generated-sources/openapi";
import { drawSeatingPlan } from "src/app/shared_modules/seatingPlanGraphics";
import { applyShowInformation } from "./seatingPlanEvents";
import { ActivatedRoute, Router } from "@angular/router";
import {CustomAuthService} from "../../services/custom-auth.service";
import {faCircleInfo} from "@fortawesome/free-solid-svg-icons";
import { ToastrService } from "ngx-toastr";

interface SeatBookingInformation {
  color: number;
  isStandingSector: boolean;
  totalPrice: number;
  singlePrice: number;
  ticketCount: number;
}

@Component({
  selector: "app-seating-plan",
  templateUrl: "./seating-plan.component.html",
  styleUrls: ["./seating-plan.component.scss"],
})
export class SeatingPlanComponent implements OnInit, AfterViewInit {
  @ViewChild("pixiContainer") pixiContainer: ElementRef<HTMLDivElement>;
  @ViewChild("infoOverlay") infoOverlay: ElementRef<HTMLDivElement>;

  getValues = Object.values;

  pixiApplication: Application;
  hoverInfo: { seatNumber: number; rowNumber: number; price: number; color: number } | undefined =
    undefined;
  showInformation: ShowInformation;
  chosenSeats: { [seatId: number]: SeatWithBookingStatus } = {};
  seatingPlan: SeatingPlanLayout;
  sectorBookingInformation: SeatBookingInformation[] = [];
  sectorPriceMap: { [sectorId: number]: number } = {};
  totalPrice = 0;
  show: Show;
  event: Event = {
    eventId: 0,
    name: "",
    category: null,
    duration: 0,
    content: "",
  };
  error = undefined;
  artists: Artist[] = [];
  info = faCircleInfo;

  constructor(
    private showsService: ShowsService,
    private artistsService: ArtistsService,
    private eventsService: EventsService,
    private seatingPlansService: SeatingPlansService,
    private ticketsService: TicketsService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    public authService: CustomAuthService
  ) {}
  async ngOnInit() {
    this.route.paramMap.subscribe({
      next: (params) => {
        if (isNaN(+params.get("showId"))) {
          this.error = new Error("Could not process ID in parameter");
          return;
        }
        const showId = +params.get("showId");
        this.showsService.showsIdGet(showId).subscribe({
          next: (show) => {
            this.show = show;
            this.retreiveArtists(show);
            this.retreiveEvent(show);
            this.retreiveSeatingPlan(show);
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
  }

  ngAfterViewInit() {
    this.pixiApplication = new Application({
      antialias: true,
      backgroundAlpha: 0,
    });
  }

  retreiveEvent(show: Show) {
    this.eventsService.eventsIdGet(show.event.eventId).subscribe({
      next: (event) => {
        this.event = event;
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
  }

  retreiveArtists(show: Show) {
    this.artists = [];
    for (const artistId of show.artists) {
      this.artistsService.artistsIdGet(artistId).subscribe({
        next: (artist) => {
          this.artists.push(artist);
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
    }
  }

  retreiveSeatingPlan(show: Show) {
    this.showsService.showTicketsIdGet(show.showId).subscribe({
      next: (showInformation) => {
        this.showInformation = showInformation;
        this.seatingPlansService
        .seatingPlanLayoutsIdGet(this.showInformation.seatingPlan.seatingPlanLayoutId)
        .subscribe({
          next: async (seatingPlan) => {
            this.seatingPlan = seatingPlan;
            this.showInformation.sectors.forEach((sector) => {
              this.sectorPriceMap[sector.sectorId] = sector.price;
            });
            this.calculateSectorBookingInformation();
            this.initializeSeatingPlan();
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
  }

  setError(error: any) {
    this.error = error;
  }

  initializeSeatingPlan() {
    this.pixiApplication.stage.removeChildren();
    this.pixiApplication.view.width = this.seatingPlan.general.width;
    this.pixiApplication.view.height = this.seatingPlan.general.height;
    document.addEventListener("mousemove", (event) => {
      this.infoOverlay.nativeElement.style.left = event.x + 20 + "px";
      this.infoOverlay.nativeElement.style.top = event.y + "px";
      return event;
    });
    this.pixiContainer.nativeElement.appendChild(this.pixiApplication.view);
    drawSeatingPlan(this.pixiApplication.stage, this.seatingPlan);
    applyShowInformation(
      this.pixiApplication.stage,
      this.showInformation,
      {
        mouseover: this.seatHover.bind(this),
        mouseout: this.seatBlur.bind(this),
        click: this.triggerSeat.bind(this),
      },
      {mouseover: noop, mouseout: noop, click: this.addStandingSeat.bind(this)},
      {mouseover: noop, mouseout: noop, click: this.removeStandingSeat.bind(this)}
    );
  }

  convertToCurrency(value: number) {
    return value.toLocaleString(undefined, {style: "currency", currency: "EUR"});
  }

  numberToCssColorString(color: number) {
    return `#${color.toString(16).padStart(6, "0")}`;
  }

  confirmPurchase() {
    //TODO: Add redirect to bill
    this.ticketsService
      .ticketsPost({
        reserved: [],
        purchased: map(this.chosenSeats, (seat) => seat.ticketId),
      })
      .subscribe({
        next: (response) => {
          console.log(response);
          this.router.navigate(["/", "orders"]);
          this.toastr.success("Succesfully purchased tickets!");
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
  }
  confirmReservation() {
    //TODO: Add redirect to "reservation"-bill
    this.ticketsService
      .ticketsPost({
        reserved: map(this.chosenSeats, (seat) => seat.ticketId),
        purchased: [],
      })
      .subscribe({
        next: (response) => {
          console.log(response);
          this.router.navigate(["/", "orders"]);
          this.toastr.success("Succesfully reserved tickets!");
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
  }

  calculateSectorBookingInformation() {
    this.sectorBookingInformation = this.seatingPlan.sectors.map((sector) => {
      const sectorSeatInformation = this.getSectorSeatInformation(sector.id);
      return {color: sector.color, isStandingSector: sector.noSeats, ...sectorSeatInformation};
    });
    this.totalPrice =
      this.sectorBookingInformation.reduce(
        (oldValue, sector) => oldValue + Math.ceil(sector.totalPrice * 100),
        0
      ) / 100;
  }

  getSectorSeatInformation(sectorId: number) {
    const sectorSeats = groupBy(this.chosenSeats, "sector")[sectorId];
    if (!sectorSeats) {
      const emptySector = this.showInformation.sectors.find(
        (sector) => sector.sectorId === sectorId
      );
      return {totalPrice: 0, singlePrice: emptySector.price, ticketCount: 0};
    }
    const totalPrice =
      sectorSeats.reduce(
        (oldValue, seat) => oldValue + Math.ceil(this.sectorPriceMap[seat.sector] * 100),
        0
      ) / 100;
    const ticketCount = sectorSeats.length;
    return {totalPrice, singlePrice: this.sectorPriceMap[sectorId], ticketCount};
  }

  private seatHover(seatId: number) {
    const seatInformation = this.showInformation.seats.find((seat) => seat.seatId === seatId);
    const sectorInformation = this.seatingPlan.sectors.find(
      (sector) => seatInformation.sector === sector.id
    );
    this.hoverInfo = {
      rowNumber: seatInformation.rowNumber,
      seatNumber: seatInformation.seatNumber,
      price: this.sectorPriceMap[seatInformation.sector],
      color: sectorInformation ? sectorInformation.color : 0xffffff,
    };
  }

  private seatBlur(seatId: number) {
    this.hoverInfo = undefined;
  }

  private triggerSeat(seatId: number) {
    if (this.chosenSeats[seatId]) {
      delete this.chosenSeats[seatId];
      this.calculateSectorBookingInformation();
      return "available";
    }
    const availableSeat = this.showInformation.seats.find((seat) => seat.seatId === seatId);
    if (availableSeat && !availableSeat.purchased && !availableSeat.reserved) {
      this.chosenSeats[availableSeat.seatId] = availableSeat;
      this.calculateSectorBookingInformation();
      return "unavailable";
    }
  }

  private addStandingSeat(sectorId: number) {
    const freeSeat = this.showInformation.seats.find(
      (seat) =>
        seat.sector === sectorId &&
        !seat.reserved &&
        !seat.purchased &&
        !this.chosenSeats[seat.seatId]
    );
    if (freeSeat) {
      this.chosenSeats[freeSeat.seatId] = freeSeat;
      this.calculateSectorBookingInformation();
      return countBy(this.chosenSeats, "sector")[sectorId];
    }
  }

  private removeStandingSeat(sectorId: number) {
    const seatToFree = find(this.chosenSeats, (seat) => seat.sector === sectorId);
    if (seatToFree) {
      delete this.chosenSeats[seatToFree.seatId];
      this.calculateSectorBookingInformation();
      const count = countBy(this.chosenSeats, "sector")[sectorId];
      return count !== undefined ? count : 0;
    }
  }
}
