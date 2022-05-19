/**
 * This is only a utility function right now for generating a valid layout file.
 * Will be incorporated when create of floor plan is possible as "AutoLayout" functionality
 */

import { uniqueId } from "lodash";
import { ShowInformation } from "src/app/generated-sources/openapi";
import { SeatingPlan, SectorBuilder } from "src/app/shared_modules/seatingPlanGraphics";

const generateFromSectorBuilder = (sectors: SectorBuilder[]): SeatingPlan => {
  const seatingPlan: SeatingPlan = {
    general: {
      width: 1000,
      height: 1000,
    },
    seats: createSeatsFromBuilder(sectors),
    sectors: createSectorsFromBuilder(sectors),
    staticElements: [],
  };
  return seatingPlan;
};

const parseColor = (color: string): number => {
  return Number.parseInt(color.substring(1), 16)
};

const createSectorsFromBuilder = (sectors: SectorBuilder[]): SeatingPlan["sectors"] =>
  sectors.map((sector, index) => {
    console.log(parseColor(sector.color))
    if (sector.standingSector) {
      return {
        location: { x: index * 110, y: 10, w: 100, h: 100 },
        noSeats: true,
        id: index,
        color: parseColor(sector.color),
      };
    }
    return {
      noSeats: false,
      id: index,
      color: parseColor(sector.color),
    };
  });

const createSeatsFromBuilder = (sectors: SectorBuilder[]): SeatingPlan["seats"] => {
  const seats: SeatingPlan["seats"] = [];
  sectors.forEach((sector, index) => {
    let seatCount = 0;
    while (seatCount < sector.seatCount) {
      const sideLength = 100;
      const blockGap = 10;
      const seatWidth = 10;
      const seatGap = 5;
      const seatWithGap = seatWidth + seatGap;
      seats.push({
        id: +uniqueId(),
        sectorId: index,
        location: !sector.standingSector
          ? {
              x:
                index * (blockGap + sideLength) +
                (seatCount % Math.floor(sideLength / seatWithGap)) * seatWithGap,
              y: 10 + Math.floor(seatCount / Math.floor(sideLength / seatWithGap)) * seatWithGap,
              w: seatWidth,
              h: seatWidth,
            }
          : undefined,
      });
      seatCount++;
    }
  });
  return seats;
};

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

const createSeats = (showInformation: ShowInformation): SeatingPlan["seats"] =>
  showInformation.seats.map((seat) => ({
    id: seat.seatId,
    sectorId: seat.sector,
    location:
      seat.seatNumber !== undefined && seat.rowNumber !== undefined
        ? { x: 100 + seat.seatNumber * 20, y: 100 + seat.rowNumber * 20, w: 15, h: 15 }
        : undefined,
  }));

const createSectors = (showInformation: ShowInformation): SeatingPlan["sectors"] => {
  const standingSectors: { [sectorId: number]: true } = {};
  showInformation.seats.forEach((seat) => {
    if (seat.rowNumber === undefined || seat.seatNumber === undefined) {
      standingSectors[seat.sector] = true;
    }
  });
  return showInformation.sectors.map((sector, index) => ({
    id: sector.sectorId,
    noSeats: !!standingSectors[sector.sectorId],
    location: standingSectors[sector.sectorId]
      ? { x: index * 120, y: 5, w: 100, h: 90 }
      : undefined,
    color: 0,
  }));
};

export { generateFromShowInfo, generateFromSectorBuilder };
