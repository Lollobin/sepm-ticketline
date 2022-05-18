import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { Application } from 'pixi.js';
import { Show, ShowInformation } from 'src/app/generated-sources/openapi';
import { drawSeatingPlan, SeatingPlan } from 'src/app/shared_modules/seatingPlanGraphics';
import { applyShowInformation } from '../seating-plan/seatingPlanEvents';
import { generateFromShowInfo } from './generateSampleFromStructure';

@Component({
  selector: 'app-seating-plan-editor',
  templateUrl: './seating-plan-editor.component.html',
  styleUrls: ['./seating-plan-editor.component.scss']
})
export class SeatingPlanEditorComponent implements AfterViewInit {
  @ViewChild("pixiContainer") pixiContainer: ElementRef<HTMLDivElement>;
  @ViewChild("infoOverlay") infoOverlay: ElementRef<HTMLDivElement>;
  pixiApplication: Application;
  seatingPlan: SeatingPlan
  @Input() set showInformation(showInformation: ShowInformation) {
    this.seatingPlan = generateFromShowInfo(showInformation);
    this.initializeSeatingPlan()
  }
  constructor() { }
  initializeSeatingPlan() {
    this.pixiApplication.stage.removeChildren();
    this.pixiApplication.view.width = this.seatingPlan.general.width;
    this.pixiApplication.view.height = this.seatingPlan.general.height;
    document.addEventListener("mousemove", (event) => {
      this.infoOverlay.nativeElement.style.left = event.x + 20 + "px";
      this.infoOverlay.nativeElement.style.top = event.y + "px";
      return event;
    });
    this.pixiContainer.nativeElement.appendChild(this.pixiApplication.view);
    drawSeatingPlan(this.pixiApplication.stage, this.seatingPlan);
  }
  ngAfterViewInit() {
    this.pixiApplication = new Application({
      antialias: true,
      backgroundAlpha: 0,
    });
  }
}
