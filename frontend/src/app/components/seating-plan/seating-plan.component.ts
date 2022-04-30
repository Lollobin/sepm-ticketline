import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { countBy, find, mapValues, noop } from "lodash";
import { Application, Container, Graphics, Rectangle, Text, TextStyle } from "pixi.js";
import { SeatWithBookingStatus, Sector, ShowInformation } from "src/app/generated-sources/openapi";
import {
  SeatingPlan,
  drawSeatingPlan,
  generateSeatId,
  generateStandingAreaId,
} from "./seatingPlanGraphics";
import sample from "./sampleStructure.json";
import sampleData from "./sampleShowInformation.json";
import { applyShowInformation } from "./seatingPlanEvents";

@Component({
  selector: "app-seating-plan",
  templateUrl: "./seating-plan.component.html",
  styleUrls: ["./seating-plan.component.scss"],
})
export class SeatingPlanComponent implements OnInit, AfterViewInit {
  @ViewChild("pixiContainer") pixiContainer: ElementRef<HTMLDivElement>;
  @ViewChild("infoOverlay") infoOverlay: ElementRef<HTMLDivElement>;

  getValues = Object.values;

  hoverInfo: SeatWithBookingStatus | undefined = undefined;
  seatUsage: ShowInformation = sampleData;
  chosenSeats: { [seatId: number]: SeatWithBookingStatus } = {};
  seatingPlan: SeatingPlan = sample;
  constructor() {}
  ngOnInit(): void {}
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
      this.seatUsage,
      {
        mouseover: this.seatHover.bind(this),
        mouseout: this.seatBlur.bind(this),
        click: this.triggerSeat.bind(this),
      },
      { mouseover: noop, mouseout: noop, click: this.addStandingSeat.bind(this) },
      { mouseover: noop, mouseout: noop, click: this.removeStandingSeat.bind(this) }
    );
  }
  private seatHover(seatId: number) {
    this.hoverInfo = this.seatUsage.seats.find((seat) => seat.seatId === seatId);
  }
  private seatBlur(seatId: number) {
    this.hoverInfo = undefined;
  }
  private triggerSeat(seatId: number) {
    if (this.chosenSeats[seatId]) {
      delete this.chosenSeats[seatId];
      return "available";
    }
    const availableSeat = this.seatUsage.seats.find((seat) => seat.seatId === seatId);
    if (availableSeat && !availableSeat.purchased && !availableSeat.reserved) {
      this.chosenSeats[availableSeat.seatId] = availableSeat;
      return "unavailable";
    }
  }
  private addStandingSeat(sectorId: number) {
    const freeSeat = this.seatUsage.seats.find(
      (seat) =>
        seat.sector === sectorId &&
        !seat.reserved &&
        !seat.purchased &&
        !this.chosenSeats[seat.seatId]
    );
    if (freeSeat) {
      this.chosenSeats[freeSeat.seatId] = freeSeat;
      return countBy(this.chosenSeats, "sector")[sectorId];
    }
  }
  private removeStandingSeat(sectorId: number) {
    const seatToFree = find(this.chosenSeats, (seat) => seat.sector === sectorId);
    if (seatToFree) {
      delete this.chosenSeats[seatToFree.seatId];
      const count = countBy(this.chosenSeats, "sector")[sectorId];
      return count !== undefined ? count : 0;
    }
  }

}
