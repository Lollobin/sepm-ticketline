import { countBy, min } from "lodash";
import { Container, Graphics, Text } from "pixi.js";
import { SeatWithBookingStatus, Sector, ShowInformation } from "src/app/generated-sources/openapi";
import { generateSeatId, generateStandingAreaId } from "./seatingPlanGraphics";

interface ButtonCallbacks {
  mouseover: Function;
  mouseout: Function;
  click: Function;
}

interface SeatCallbacks extends ButtonCallbacks {
  mouseover: (seatId: number) => void;
  mouseout: (seatId: number) => void;
  click: (seatId: number) => "available" | "unavailable";
}

interface CounterCallbacks extends ButtonCallbacks {
  mouseover: () => void;
  mouseout: () => void;
  click: (sectorId: number) => number;
}

/**
 * Applies the click logic for a seat
 * @param stage container where the seat graphics object is located
 * @param seat Seat that is processed
 * @param seatCallbacks Callbacks for th seats
 * @returns The number of seats that do not have a graphic object and are in a seating sector(1 or 0)
 */
function applySeatLogic(
  stage: Container,
  seat: SeatWithBookingStatus,
  seatCallbacks: SeatCallbacks
): boolean {
  const seatGraphics = stage.getChildByName(generateSeatId(seat.seatId));
  if (seatGraphics && (seat.purchased || seat.reserved)) {
    seatGraphics.alpha = 0.1;
    return false;
  }
  if (!seatGraphics && (seat.purchased || seat.reserved)) {
    return true;
  }
  if (seatGraphics) {
    seatGraphics.interactive = true;
    seatGraphics.buttonMode = true;
    addButtonListeners(<Graphics>seatGraphics, {
      mouseover: () => {
        seatCallbacks.mouseover(seat.seatId);
      },
      mouseout: () => {
        seatCallbacks.mouseout(seat.seatId);
      },
      click: () => {
        const graphicsCover = stage.getChildByName(`${generateSeatId(seat.seatId)}_cover`);
        const availability = seatCallbacks.click(seat.seatId);
        if (availability === "unavailable") {
          graphicsCover.visible = true;
        }
        if (availability === "available") {
          graphicsCover.visible = false;
        }
      },
    });
    return false;
  }
  return false;
}

function initCounterCallbacks(
  button: Graphics,
  counter: Text,
  callbacks: CounterCallbacks,
  sector: Sector
) {
  button.buttonMode = true;
  button.interactive = true;
  addButtonListeners(button, {
    mouseover: () => {
      callbacks.mouseover();
    },
    mouseout: () => {
      callbacks.mouseout();
    },
    click: () => {
      const updatedCount = callbacks.click(sector.sectorId);
      if (updatedCount !== undefined) {
        counter.text = `${updatedCount}`;
      }
    },
  });
}

function applySectorLogic(
  stage: Container,
  sector: Sector,
  plusCallbacks: CounterCallbacks,
  minusCallbacks: CounterCallbacks,
  unavailableSeats: { [sectorId: number]: number },
  seats: ShowInformation["seats"]
) {
  const graphics = <Container | undefined>(
    stage.getChildByName(generateStandingAreaId(sector.sectorId))
  );
  if (!graphics) {
    return;
  }
  const seatsPerSector = countBy(seats, "sector");
  const seatAvailability = <Text>graphics.getChildByName("seatAvailability");
  seatAvailability.text = `${unavailableSeats[sector.sectorId]}/${seatsPerSector[sector.sectorId]}`;

  const plusMinusContainer = <Container>graphics.getChildByName("plusMinusContainer");
  const counter = <Text>plusMinusContainer.getChildByName("ticketCounter");
  const plus = <Graphics>plusMinusContainer.getChildByName("plus");
  initCounterCallbacks(plus, counter, plusCallbacks, sector);

  const minus = <Graphics>plusMinusContainer.getChildByName("minus");
  initCounterCallbacks(minus, counter, minusCallbacks, sector);
}

function applyShowInformation(
  stage: Container,
  info: ShowInformation,
  seatCallbacks: SeatCallbacks,
  plusCallbacks: CounterCallbacks,
  minusCallbacks: CounterCallbacks
) {
  const unavailableSeats: { [sectorId: number]: number } = {};
  info.seats.forEach((seat) => {
    const isReservedSectorSeat = applySeatLogic(stage, seat, seatCallbacks);
    const seatCount = isReservedSectorSeat ? 1 : 0;
    unavailableSeats[seat.sector] = unavailableSeats[seat.sector]
      ? unavailableSeats[seat.sector] + seatCount
      : seatCount;
  });
  info.sectors.forEach((sector) => {
    applySectorLogic(stage, sector, plusCallbacks, minusCallbacks, unavailableSeats, info.seats);
  });
}

function addButtonListeners(graphics: Graphics, callbacks: ButtonCallbacks, seatHoverAlpha = 0.7) {
  graphics.interactive = true;
  graphics.buttonMode = true;
  graphics.on("mouseover", () => {
    graphics.alpha = seatHoverAlpha;
    callbacks.mouseover();
  });
  graphics.on("mouseout", () => {
    graphics.alpha = 1;
    callbacks.mouseout();
  });
  graphics.on("click", () => {
    callbacks.click();
  });
}
export { applyShowInformation, ButtonCallbacks };
