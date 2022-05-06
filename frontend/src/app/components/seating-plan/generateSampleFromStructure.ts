/**
 * This is only a utility function right now for generating a valid layout file.
 * Will be incorporated when create of floor plan is possible as "AutoLayout" functionality
 */

import { ShowInformation } from "src/app/generated-sources/openapi";
import { SeatingPlan } from "./seatingPlanGraphics";

const generateFromShowInfo = (showInformation: ShowInformation): SeatingPlan => {
  const seatingPlan: SeatingPlan = {
    general: {
      width: 1000,
      height: 1000,
    },
    seats: createSeats(showInformation),
    sectors: createSectors(showInformation),
    staticElements: [],
  };
  return seatingPlan;
};

const createSeats = (showInformation: ShowInformation): SeatingPlan["seats"] => {
  return showInformation.seats.map((seat, index) => {
    return {
      id: seat.seatId,
      sectorId: seat.sector,
      location:
        seat.seatNumber !== undefined && seat.rowNumber !== undefined
          ? { x: 100 + seat.seatNumber * 20, y: 100 + seat.rowNumber * 20, w: 15, h: 15 }
          : undefined,
    };
  });
};

const createSectors = (showInformation: ShowInformation): SeatingPlan["sectors"] => {
  const standingSectors: { [sectorId: number]: true } = {};
  showInformation.seats.forEach((seat) => {
    console.log(
      seat.rowNumber,
      seat.seatNumber,
      seat.rowNumber === undefined || seat.seatNumber === undefined
    );
    if (seat.rowNumber === undefined || seat.seatNumber === undefined) {
      standingSectors[seat.sector] = true;
    }
  });
  console.log(standingSectors);
  return showInformation.sectors.map((sector, index) => {
    return {
      id: sector.sectorId,
      noSeats: !!standingSectors[sector.sectorId],
      location: standingSectors[sector.sectorId]
        ? { x: index * 120, y: 5, w: 100, h: 90 }
        : undefined,
      color: 0,
    };
  });
};

export { generateFromShowInfo };
