import { Graphics } from "pixi.js";

interface ButtonCallbacks {
  mouseover: () => void;
  mouseout: () => void;
  click: () => void;
}

const addButtonListeners = (
  graphics: Graphics,
  callbacks: ButtonCallbacks,
  seatHoverAlpha = 0.7
) => {
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
};
export { addButtonListeners };
