import { countBy } from "lodash";
import { Container, Graphics, Text } from "pixi.js";
import { Sector, ShowInformation } from "src/app/generated-sources/openapi";
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

function applyShowInformation(
  stage: Container,
  info: ShowInformation,
  seatCallbacks: SeatCallbacks,
  plusCallbacks: CounterCallbacks,
  minusCallbacks: CounterCallbacks
) {
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
      addButtonListeners(<Graphics>graphics, {
        mouseover: () => {
          seatCallbacks.mouseover(seat.seatId);
        },
        mouseout: () => {
          seatCallbacks.mouseout(seat.seatId);
        },
        click: () => {
          const graphicsCover = stage.getChildByName(
            `${generateSeatId(seat.seatId)}_cover`
          );
          const availability = seatCallbacks.click(seat.seatId);
          if (availability === "unavailable") {
            graphicsCover.visible = true;
          }
          if (availability === "available") {
            graphicsCover.visible = false;
          }
        },
      });
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
      const counter = <Text>plusMinusContainer.getChildByName("ticketCounter");
      addButtonListeners(<Graphics>plus, {
        mouseover: () => {
          plusCallbacks.mouseover();
        },
        mouseout: () => {
          plusCallbacks.mouseout();
        },
        click: () => {
          const updatedCount = plusCallbacks.click(sector.sectorId);
          if (updatedCount) {
            counter.text = `${updatedCount}`;
          }
        },
      });
      addButtonListeners(<Graphics>minus, {
        mouseover: () => {
          minusCallbacks.mouseover();
        },
        mouseout: () => {
          minusCallbacks.mouseout();
        },
        click: () => {
          const updatedCount = minusCallbacks.click(sector.sectorId);
          if (updatedCount !== undefined) {
            counter.text = `${updatedCount}`;
          }
        },
      });
    }
  }
}

function addButtonListeners(
  graphics: Graphics,
  callbacks: ButtonCallbacks,
  seatHoverAlpha = 0.7
) {
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
