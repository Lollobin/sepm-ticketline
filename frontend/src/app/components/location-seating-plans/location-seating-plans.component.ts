import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { Application } from "pixi.js";
import { Location, LocationsService, SeatingPlan, SeatingPlanLayout, SeatingPlansService } from "src/app/generated-sources/openapi";
import { drawSeatingPlanPreview } from "src/app/shared_modules/seatingPlanGraphics";
import {faPlus} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: "app-location-seating-plans",
  templateUrl: "./location-seating-plans.component.html",
  styleUrls: ["./location-seating-plans.component.scss"],
})
export class LocationSeatingPlansComponent implements OnInit, AfterViewInit {
  @ViewChild("pixiContainer") pixiContainer: ElementRef<HTMLDivElement>;
  @ViewChild("infoOverlay") infoOverlay: ElementRef<HTMLDivElement>;

  pixiApplication: Application;

  locationId = 1;
  location: Location;
  seatingPlans: SeatingPlan[];
  seatingPlanLayout: SeatingPlanLayout;
  selectedSeatingPlan: SeatingPlan;
  plus = faPlus;

  constructor(private route: ActivatedRoute, private locationsService: LocationsService, private toastr: ToastrService,
    private seatingPlansService: SeatingPlansService) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.locationId = params["id"];
      this.locationsService.locationsIdGet(this.locationId).subscribe({
        next: (location) => {
          this.location = location;
          this.locationsService.locationsIdSeatingPlansGet(this.locationId).subscribe({
            next: (seatingPlans) => {
              this.seatingPlans = seatingPlans;
              if (this.seatingPlans.length === 0) {
                this.toastr.info("There are no seating plans for this location!");
              }
            },
            error: (error) => {
              console.log(error);
              if (error.status === 0 || error.status === 500) {
                this.toastr.error(error.message);
              } else {
                this.toastr.warning(error.error);
              }
            }
          });
        },
        error: (error) => {
          console.log(error);
          if (error.status === 0 || error.status === 500) {
            this.toastr.error(error.message);
          } else {
            this.toastr.warning(error.error);
          }
        }
      });
    });
  }

  ngAfterViewInit() {
    this.pixiApplication = new Application({
      antialias: true,
      backgroundAlpha: 0,
    });
  }

  initializeSeatingPlan() {
    this.pixiApplication.stage.removeChildren();
    this.pixiApplication.view.width = this.seatingPlanLayout.general.width;
    this.pixiApplication.view.height = this.seatingPlanLayout.general.height;
    document.addEventListener("mousemove", (event) => event);
    this.pixiContainer.nativeElement.appendChild(this.pixiApplication.view);
    drawSeatingPlanPreview(this.pixiApplication.stage, this.seatingPlanLayout);
  }

  numberToCssColorString(color: number) {
    return `#${color.toString(16).padStart(6, "0")}`;
  }

  getSeatingPlanLayout(seatingPlan: SeatingPlan) {
    this.selectedSeatingPlan = seatingPlan;
    this.seatingPlansService
      .seatingPlanLayoutsIdGet(seatingPlan.seatingPlanId)
      .subscribe({
        next: async (seatingPlanLayout) => {
          this.seatingPlanLayout = seatingPlanLayout;
          this.initializeSeatingPlan();
        },
        error: error => {
          console.log("Error getting sectors of seatingPlanLayout with id", seatingPlan.seatingPlanId);
          if (error.status === 0 || error.status === 500) {
            this.toastr.error(error.message);
          } else {
            this.toastr.warning(error.error);
          }
        },
      });
  }
}
