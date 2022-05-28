import { countBy } from "lodash";
import { Container, Graphics, Text } from "pixi.js";
import { SeatWithBookingStatus, Sector, ShowInformation } from "src/app/generated-sources/openapi";
import { addButtonListeners } from "src/app/shared_modules/seatingPlanEvents";
import { generateSeatId, generateStandingAreaId } from "src/app/shared_modules/seatingPlanGraphics";

interface SeatCallbacks {
  mouseover: (seatId: number) => void;
  mouseout: (seatId: number) => void;
  click: (seatId: number) => "available" | "unavailable";
}

interface CounterCallbacks {
  mouseover: () => void;
  mouseout: () => void;
  click: (sectorId: number) => number;
}

/**
 * Applies the click logic for a seat
 *
 * @param stage container where the seat graphics object is located
 * @param seat Seat that is processed
 * @param seatCallbacks Callbacks for th seats
 * @returns The number of seats that do not have a graphic object and are in a seating sector(1 or 0)
 */
const applySeatLogic = (
  stage: Container,
  seat: SeatWithBookingStatus,
  seatCallbacks: SeatCallbacks
): boolean => {
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
    addButtonListeners(seatGraphics as Graphics, {
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
};

const initCounterCallbacks = (
  button: Graphics,
  counter: Text,
  callbacks: CounterCallbacks,
  sector: Sector
) => {
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
};

const applySectorLogic = (
  stage: Container,
  sector: Sector,
  plusCallbacks: CounterCallbacks,
  minusCallbacks: CounterCallbacks,
  unavailableSeats: { [sectorId: number]: number },
  seats: ShowInformation["seats"]
) => {
  const graphics = stage.getChildByName(generateStandingAreaId(sector.sectorId)) as
    | Container
    | undefined;
  if (!graphics) {
    return;
  }
  const seatsPerSector = countBy(seats, "sector");
  const seatAvailability = graphics.getChildByName("seatAvailability") as Text;
  seatAvailability.text = `${unavailableSeats[sector.sectorId]}/${seatsPerSector[sector.sectorId]}`;

  const plusMinusContainer = graphics.getChildByName("plusMinusContainer") as Container;
  const counter = plusMinusContainer.getChildByName("ticketCounter") as Text;
  const plus = plusMinusContainer.getChildByName("plus") as Graphics;
  initCounterCallbacks(plus, counter, plusCallbacks, sector);

  const minus = plusMinusContainer.getChildByName("minus") as Graphics;
  initCounterCallbacks(minus, counter, minusCallbacks, sector);
};

const applyShowInformation = (
  stage: Container,
  info: ShowInformation,
  seatCallbacks: SeatCallbacks,
  plusCallbacks: CounterCallbacks,
  minusCallbacks: CounterCallbacks
) => {
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
};


export { applyShowInformation };
