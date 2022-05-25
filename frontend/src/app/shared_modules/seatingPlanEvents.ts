import { Graphics } from "pixi.js";

interface ButtonCallbacks {
  mouseover: (event?) => void;
  mouseout: (event?) => void;
  click: (event?) => void;
}

const addButtonListeners = (
  graphics: Graphics,
  callbacks: ButtonCallbacks,
  seatHoverAlpha = 0.7
) => {
  graphics.interactive = true;
  graphics.buttonMode = true;
  graphics.on("mouseover", (event) => {
    graphics.alpha = seatHoverAlpha;
    callbacks.mouseover(event);
  });
  graphics.on("mouseout", (event) => {
    graphics.alpha = 1;
    callbacks.mouseout(event);
  });
  graphics.on("click", (event) => {
    callbacks.click(event);
  });
};
export { addButtonListeners };
