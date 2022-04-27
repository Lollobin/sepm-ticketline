import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Application, Graphics } from "pixi.js"
@Component({
  selector: 'app-seating-plan',
  templateUrl: './seating-plan.component.html',
  styleUrls: ['./seating-plan.component.scss']
})
export class SeatingPlanComponent implements OnInit {
  @ViewChild("pixiContainer") pixiContainer: ElementRef<HTMLDivElement>
  constructor() { }
  ngAfterViewInit(){
    console.log(this.pixiContainer.nativeElement)
    
    const app = new Application({resizeTo: this.pixiContainer.nativeElement})
    this.pixiContainer.nativeElement.appendChild(app.view)
    const testGraphics = new Graphics()
    testGraphics.beginFill(0xAAAAAA)
    testGraphics.drawCircle(100,100,100)
    app.stage.addChild(testGraphics)
  }
  ngOnInit(): void {

  }

}
