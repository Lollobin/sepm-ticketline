import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { countBy, find, groupBy, mapValues, noop } from "lodash";
import { Application, Container, Graphics, Rectangle, Text, TextStyle } from "pixi.js";
import {
  Artist,
  Event,
  SeatWithBookingStatus,
  Sector,
  Show,
  ShowInformation,
} from "src/app/generated-sources/openapi";
import {
  SeatingPlan,
  drawSeatingPlan,
  generateSeatId,
  generateStandingAreaId,
} from "./seatingPlanGraphics";
import sample from "./sampleStructure.json";
import sampleData from "./sampleShowInformation.json";
import { applyShowInformation } from "./seatingPlanEvents";

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

  hoverInfo: { seatNumber: number; rowNumber: number; price: number; color: number } | undefined =
    undefined;
  showInformation: ShowInformation = sampleData;
  chosenSeats: { [seatId: number]: SeatWithBookingStatus } = {};
  seatingPlan: SeatingPlan = sample;
  sectorBookingInformation: SeatBookingInformation[] = [];
  sectorPriceMap: { [sectorId: number]: number } = {};
  totalPrice: number = 0;
  show: Show = { showId: 1234, date: new Date().toLocaleString(), event: 1234, artists: [12] };
  event: Event = {
    eventId: 1234,
    name: "Rock am Berg",
    category: "Zeltfest",
    duration: 144,
    content:
      "This festival contains many different artists, mainly carlus and hios gang. This is very good. I like that. Can we have more like this? I Hope no one notices this sample text. You know, I like sample text. It makes me feel good. Anyways, enjoy the demo!",
  };
  artists: Artist[] = [
    { artistId: 12, bandName: "Carlos Rock Band" },
    { artistId: 133, firstName: "Karlo", lastName: "Steinband" },
  ];
  constructor() {}
  ngOnInit(): void {
    //TODO: Add retreival of necessary data here (when backend is implemented)
    this.showInformation.sectors.forEach((sector) => {
      this.sectorPriceMap[sector.sectorId] = sector.price;
    });
    this.calculateSectorBookingInformation();
  }
  ngAfterViewInit() {
    const app = new Application({
      width: this.seatingPlan.general.width,
      height: this.seatingPlan.general.height,
      antialias: true,
      backgroundAlpha: 0,
    });
    document.addEventListener("mousemove", (event) => {
      this.infoOverlay.nativeElement.style.left = event.x + 20 + "px";
      this.infoOverlay.nativeElement.style.top = event.y + "px";
      return event;
    });
    this.pixiContainer.nativeElement.appendChild(app.view);
    drawSeatingPlan(app.stage, this.seatingPlan);
    applyShowInformation(
      app.stage,
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
    const seat = this.showInformation.seats.find((seat) => seat.seatId === seatId);
    const sector = this.seatingPlan.sectors.find((sector) => seat.sector === sector.id);
    this.hoverInfo = {
      rowNumber: seat.rowNumber,
      seatNumber: seat.seatNumber,
      price: this.sectorPriceMap[seat.sector],
      color: sector ? sector.color : 0xffffff,
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
