import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { countBy, mapValues } from "lodash";
import {
  Application,
  Container,
  Graphics,
  Rectangle,
  Text,
  TextStyle,
} from "pixi.js";
import {
  SeatWithBookingStatus,
  Sector,
  ShowInformation,
} from "src/app/generated-sources/openapi";
import {
  SeatingPlan,
  drawSeatingPlan,
  generateSeatId,
  generateStandingAreaId,
  addListeners,
} from "./drawElements";
import sample from "./sampleStructure.json";
import sampleData from "./sampleShowInformation.json";

@Component({
  selector: "app-seating-plan",
  templateUrl: "./seating-plan.component.html",
  styleUrls: ["./seating-plan.component.scss"],
})
export class SeatingPlanComponent implements OnInit {
  @ViewChild("pixiContainer") pixiContainer: ElementRef<HTMLDivElement>;
  @ViewChild("infoOverlay") infoOverlay: ElementRef<HTMLDivElement>;

  hoverInfo: SeatWithBookingStatus | undefined = undefined;
  seatUsage: ShowInformation = sampleData;
  chosenSeats: Map<number, SeatWithBookingStatus> = new Map();

  constructor() {}
  ngAfterViewInit() {
    const app = new Application({
      resizeTo: this.pixiContainer.nativeElement,
      antialias: true,
      backgroundAlpha: 0,
    });
    document.addEventListener("mousemove", (event) => {
      this.infoOverlay.nativeElement.style.left = event.x + 20 + "px";
      this.infoOverlay.nativeElement.style.top = event.y + "px";
      return event;
    });
    this.pixiContainer.nativeElement.appendChild(app.view);
    drawSeatingPlan(app.stage, <SeatingPlan>sample);
    this.applyShowInformation(app.stage, this.seatUsage);
  }
  private addStandingSeat(sectorId: number) {
    const freeSeat = this.seatUsage.seats.find(
      (seat) =>
        seat.sector === sectorId &&
        !seat.reserved &&
        !seat.purchased &&
        !this.chosenSeats.get(seat.seatId)
    );
    if (freeSeat) {
      this.chosenSeats.set(freeSeat.seatId, freeSeat);
      return countBy(Array.from(this.chosenSeats.values()), "sector")[sectorId];
    }
  }
  private removeStandingSeat(sectorId: number) {
    const seatToFree = Array.from(this.chosenSeats.values()).find(
      (seat) => seat.sector === sectorId
    );
    if (seatToFree) {
      this.chosenSeats.delete(seatToFree.seatId);
      const count = countBy(Array.from(this.chosenSeats.values()), "sector")[
        sectorId
      ];
      return count!==undefined ? count : 0;
    }
  }
  private applyShowInformation(stage: Container, info: ShowInformation) {
    const sectorMap: { [id: number]: Sector } = {};
    const unavailableSeats: { [sectorId: number]: number } = {};
    const totalSeats = countBy(info.seats, "sector");
    for (const sector of info.sectors) {
      sectorMap[sector.sectorId] = sector;
      unavailableSeats[sector.sectorId] = 0;
    }
    for (const seat of info.seats) {
      const graphics = stage.getChildByName(generateSeatId(seat.seatId));
      if (seat.purchased || seat.reserved) {
        if (graphics) {
          graphics.alpha = 0.1;
        } else {
          unavailableSeats[seat.sector] += 1;
        }
      } else if (graphics) {
        graphics.interactive = true;
        graphics.buttonMode = true;
        addListeners(
          <Graphics>graphics,
          () => {
            this.hoverInfo = seat;
          },
          () => {
            this.hoverInfo = undefined;
          },
          () => {}
        );
      }
    }
    for (const sector of info.sectors) {
      const graphics = stage.getChildByName(
        generateStandingAreaId(sector.sectorId)
      );
      if (graphics) {
        const seatAvailability = <Text>(
          (<Container>graphics).getChildByName("seatAvailability")
        );
        seatAvailability.text = `${unavailableSeats[sector.sectorId]}/${
          totalSeats[sector.sectorId]
        }`;
        const plusMinusContainer = <Container>(
          (<Container>graphics).getChildByName("plusMinusContainer")
        );
        const plus = plusMinusContainer.getChildByName("plus");
        plus.buttonMode = true;
        plus.interactive = true;
        const minus = plusMinusContainer.getChildByName("minus");
        minus.buttonMode = true;
        minus.interactive = true;
        const counter = <Text>(
          plusMinusContainer.getChildByName("ticketCounter")
        );
        addListeners(
          <Graphics>plus,
          () => {},
          () => {},
          () => {
            const updatedCount = this.addStandingSeat(sector.sectorId);
            if (updatedCount) {
              counter.text = `${updatedCount}`;
            }
          }
        );
        addListeners(
          <Graphics>minus,
          () => {},
          () => {},
          () => {
            const updatedCount = this.removeStandingSeat(sector.sectorId);
            if (updatedCount!==undefined) {
              counter.text = `${updatedCount}`;
            }
          }
        );
      }
    }
  }
  ngOnInit(): void {}
}
