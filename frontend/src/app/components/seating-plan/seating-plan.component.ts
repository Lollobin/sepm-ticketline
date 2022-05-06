import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { countBy, find, groupBy, mapValues, noop } from "lodash";
import { Application, Container, Graphics, Rectangle, Text, TextStyle } from "pixi.js";
import {
  Artist,
  ArtistsService,
  Event,
  EventsService,
  SeatWithBookingStatus,
  Sector,
  Show,
  ShowInformation,
  ShowsService,
} from "src/app/generated-sources/openapi";
import {
  SeatingPlan,
  drawSeatingPlan
} from "./seatingPlanGraphics";
import { applyShowInformation } from "./seatingPlanEvents";
import { generateFromShowInfo } from "./generateSampleFromStructure";

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
  seatingPlan: SeatingPlan;
  sectorBookingInformation: SeatBookingInformation[] = [];
  sectorPriceMap: { [sectorId: number]: number } = {};
  totalPrice = 0;
  show: Show = { showId: 0, date: "", event: 0, artists: [] };
  event: Event = {
    eventId: 0,
    name: "",
    category: "",
    duration: 0,
    content: "",
  };
  artists: Artist[] = [];
  constructor(
    private showsService: ShowsService,
    private artistsService: ArtistsService,
    private eventsService: EventsService
  ) {}
  async ngOnInit() {
    //TODO: Add retreival of necessary data here (when backend is implemented)
    //TODO: Add error handlers
    //TODO: GET SHOW; with id from route parameter
    const showId = 1;
    this.showsService.showsIdGet(showId).subscribe({
      next: (show) => {
        this.show = show;
        for (let artistId of this.show.artists) {
          this.artistsService.artistsIdGet(artistId).subscribe({
            next: (artist) => {
              this.artists.push(artist);
            },
          });
        }
        this.eventsService.eventsIdGet(this.show.event).subscribe({
          next: (event) => {
            this.event = event;
          },
        });
        this.showsService.showTicketsIdGet(this.show.showId).subscribe({
          next: (showInformation) => {
            this.showInformation = showInformation;
            this.seatingPlan = generateFromShowInfo(showInformation);
            this.showInformation.sectors.forEach((sector) => {
              this.sectorPriceMap[sector.sectorId] = sector.price;
            });
            this.calculateSectorBookingInformation();
            this.initializeSeatingPlan();
          },
        });
      },
    });
  }
  ngAfterViewInit() {
    this.pixiApplication = new Application({
      antialias: true,
      backgroundAlpha: 0,
    });
  }
  initializeSeatingPlan() {
    this.pixiApplication.stage.removeChildren()
    this.pixiApplication.view.width = this.seatingPlan.general.width
    this.pixiApplication.view.height = this.seatingPlan.general.height
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
      { mouseover: noop, mouseout: noop, click: this.addStandingSeat.bind(this) },
      { mouseover: noop, mouseout: noop, click: this.removeStandingSeat.bind(this) }
    );
  }
  convertToCurrency(value: number) {
    return value.toLocaleString(undefined, { style: "currency", currency: "EUR" });
  }
  numberToCssColorString(color: number) {
    return `#${color.toString(16).padStart(6, "0")}`;
  }
  confirmPurchase() {
    //TODO: Add redirect to bill and show purchase overview
    console.log("YOU BOUGHT THEM TICkETS");
  }
  confirmReservation() {
    //TODO: Add redirect to "reservation"-bill and show purchase overview
    console.log("YOU RESERVED THEM TICKETS");
  }
  calculateSectorBookingInformation() {
    this.sectorBookingInformation = this.seatingPlan.sectors.map((sector) => {
      const sectorSeatInformation = this.getSectorSeatInformation(sector.id);
      return { color: sector.color, isStandingSector: sector.noSeats, ...sectorSeatInformation };
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
      return { totalPrice: 0, singlePrice: emptySector.price, ticketCount: 0 };
    }
    const totalPrice =
      sectorSeats.reduce(
        (oldValue, seat) => oldValue + Math.ceil(this.sectorPriceMap[seat.sector] * 100),
        0
      ) / 100;
    const ticketCount = sectorSeats.length;
    return { totalPrice, singlePrice: this.sectorPriceMap[sectorId], ticketCount };
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
