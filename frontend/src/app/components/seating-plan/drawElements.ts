import { countBy } from "lodash";
import { Container, Graphics, Text } from "pixi.js";

interface Location {
  x: number;
  y: number;
  w: number;
  h: number;
}
interface Color {
  baseColor: number;
  strokeColor: number;
}

interface Sector {
  id: number;
  noSeats: boolean;
  color: number;
  description?: string;
}

interface SectorWithLocation extends Sector {
  noSeats: true;
  location: Location;
}

interface StaticElement {
  id: number;
  color: number;
  description?: string;
  location: Location;
}

interface SeatingPlan {
  general: any;
  seats: Array<{
    id: number;
    sectorId: number;
    location?: Location;
  }>;
  sectors: Array<Sector | SectorWithLocation>;
  staticElements: Array<StaticElement>;
}

function drawSeatingPlan(stage: Container, seatingPlan: SeatingPlan) {
  drawSeats(stage, seatingPlan);
  drawStandingAreas(stage, seatingPlan);
  drawStaticAreas(stage, seatingPlan);
}
function drawSeats(stage: Container, seatingPlan: SeatingPlan) {
  const sectorMap: { [id: number]: Sector | SectorWithLocation } = {};
  for (const sector of seatingPlan.sectors) {
    sectorMap[sector.id] = sector;
  }
  for (const seat of seatingPlan.seats) {
    if (sectorMap[seat.sectorId].noSeats === false) {
      const seatGraphics = drawArea(
        seat.location,
        { baseColor: 0xf0f0f0, strokeColor: sectorMap[seat.sectorId].color },
        4
      );
      seatGraphics.name = `seat${seat.id}`;
      addListeners(
        seatGraphics,
        () => {},
        () => {},
        () => {}
      );
      stage.addChild(seatGraphics);
    }
  }
}
function drawStandingAreas(stage: Container, seatingPlan: SeatingPlan) {
  const standingAreas = <Array<SectorWithLocation>>(
    seatingPlan.sectors.filter((sector) => sector.noSeats)
  );
  const seatCounts = countBy(seatingPlan.seats, "sectorId");
  for (const standingArea of standingAreas) {
    const numberOfAvailableSeats = seatCounts[standingArea.id];
    const standingAreaGraphics = drawStandingArea(
      standingArea.location,
      { baseColor: 0xf0f0f0, strokeColor: standingArea.color },
      numberOfAvailableSeats,
      0,
      standingArea.description ? standingArea.description : ""
    );
    standingAreaGraphics.name = `standingArea${standingArea.id}`;
    stage.addChild(standingAreaGraphics);
  }
}
function drawStaticAreas(stage: Container, seatingPlan: SeatingPlan) {
  for (const staticArea of seatingPlan.staticElements) {
    const staticGraphics = drawNoSeatArea(
      staticArea.location,
      { baseColor: 0xf0f0f0, strokeColor: staticArea.color },
      staticArea.description ? staticArea.description : ""
    );
    stage.addChild(staticGraphics);
  }
}
function addListeners(
  graphics: Graphics,
  mouseOverCallback: () => void,
  mouseOutCallback: () => void,
  clickCallback: () => void, 
  seatHoverAlpha = 0.7
) {
  graphics.interactive = true;
  graphics.buttonMode = true;
  graphics.on("mouseover", () => {
    graphics.alpha = seatHoverAlpha;
    mouseOverCallback();
  });
  graphics.on("mouseout", () => {
    graphics.alpha = 1;
    mouseOutCallback();
  });
  graphics.on("click", () => {
    console.log("click");
    clickCallback();
  });
}

function drawPlus(color: Color) {
  const w = 20;
  const h = 20;
  const scaleFactor = 0.2;
  const plusContainer = drawArea({ x: 0, y: 0, w, h }, color, w);
  const plusSign = new Graphics()
    .lineStyle({ width: 3, color: color.strokeColor })
    .moveTo(w / 2, h - h * scaleFactor)
    .lineTo(w / 2, h * scaleFactor)
    .moveTo(w - w * scaleFactor, h / 2)
    .lineTo(w * scaleFactor, h / 2);
  plusContainer.addChild(plusSign);
  return plusContainer;
}
function drawMinus(color: Color) {
  const w = 20;
  const h = 20;
  const scaleFactor = 0.2;
  const plusContainer = drawArea({ x: 0, y: 0, w, h }, color, w);
  const plusSign = new Graphics()
    .lineStyle({ width: 3, color: color.strokeColor })
    .moveTo(w - w * scaleFactor, h / 2)
    .lineTo(w * scaleFactor, h / 2);
  plusContainer.addChild(plusSign);
  return plusContainer;
}

function calculateBoxCenterPoint(parentWidth: number, childWidth: number) {
  return (parentWidth - childWidth) / 2;
}

function centerText(text: Text, parentWidth: number) {
  text.anchor.set(0.5, 0);
  text.setTransform(parentWidth / 2);
}

function drawStandingArea(
  location: Location,
  color: Color,
  numberOfSeatsAvailable: number,
  numberOfSeatsUnavailable: number,
  text?: string
) {
  const areaGraphics = drawArea(location, color, 0);
  const seatAvailability = drawText(
    `${numberOfSeatsUnavailable}/${numberOfSeatsAvailable}`,
    15,
    areaGraphics.width
  );
  seatAvailability.setTransform(
    calculateBoxCenterPoint(areaGraphics.width, seatAvailability.width),
    location.h * 0.05
  );
  areaGraphics.addChild(seatAvailability);
  const plusMinusContainer = new Graphics();
  const plus = drawPlus(color);
  const minus = drawMinus(color);
  const ticketCounter = drawText("0", 15, 100);
  ticketCounter.anchor.set(0.5, 0);
  plus.setTransform(
    areaGraphics.width / 2 + areaGraphics.width / 3 - plus.width
  );
  minus.setTransform(areaGraphics.width / 2 - areaGraphics.width / 3);
  plusMinusContainer.addChild(plus);
  plusMinusContainer.addChild(minus);
  ticketCounter.setTransform(areaGraphics.width / 2, 0);
  plusMinusContainer.addChild(ticketCounter);
  plusMinusContainer.setTransform(
    0,
    seatAvailability.position.y + seatAvailability.height + location.h * 0.05
  );

  areaGraphics.addChild(plusMinusContainer);
  if (text) {
    const additionalText = drawText(text, 15, areaGraphics.width);
    centerText(additionalText, areaGraphics.width);
    additionalText.setTransform(
      additionalText.position.x,
      plusMinusContainer.position.y +
        plusMinusContainer.height +
        location.h * 0.05
    );
    areaGraphics.addChild(additionalText);
  }
  return areaGraphics;
}
function drawNoSeatArea(location: Location, color: Color, text?: string) {
  const areaGraphics = drawArea(location, color, 0);
  if (text) {
    const additionalText = drawText(text, 15, areaGraphics.width);
    centerText(additionalText, areaGraphics.width);
    additionalText.setTransform(additionalText.position.x, location.h * 0.05);
    areaGraphics.addChild(additionalText);
  }
  return areaGraphics;
}
function drawText(text: string, fontSize: number, maxWidth: number) {
  const textGraphics = new Text(text, {
    fontSize,
    wordWrap: true,
    wordWrapWidth: maxWidth,
    breakWords: true,
    align: "center",
  });
  return textGraphics;
}
function drawArea(location: Location, color: Color, radius: number) {
  const areaGraphics = new Graphics();
  areaGraphics
    .beginFill(color.baseColor)
    .lineStyle({ width: 3, color: color.strokeColor, alignment: 1 })
    .setTransform(location.x, location.y)
    .drawRoundedRect(0, 0, location.w, location.h, radius);
  return areaGraphics;
}

export {
  drawSeatingPlan,
  addListeners,
  SeatingPlan,
  Sector,
  SectorWithLocation,
};
