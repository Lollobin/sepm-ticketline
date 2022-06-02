import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { LocationsService, LocationWithoutId } from "src/app/generated-sources/openapi";

@Component({
  selector: "app-create-location",
  templateUrl: "./create-location.component.html",
  styleUrls: ["./create-location.component.scss"],
})
export class CreateLocationComponent implements OnInit {
  locationForm: FormGroup;

  submitted = false;

  constructor(private formBuilder: FormBuilder, private locationsService: LocationsService, private router: Router) {
    this.locationForm = this.formBuilder.group({
      name: ["", [Validators.required]],
      address: this.formBuilder.group({
        houseNumber: ["", [Validators.required]],
        street: ["", [Validators.required]],
        zipCode: ["", [Validators.required]],
        city: ["", [Validators.required]],
        country: ["", [Validators.required]],
      }),
    });
  }

  createLocation() {
    this.submitted = true;
    if (this.locationForm.valid) {
      const location: LocationWithoutId = {
        name: this.locationForm.get("name").value,
        address: this.locationForm.get("address").value,
      };
      //TOOD: Navigate to location with ID
      this.locationsService.locationsPost(location).subscribe({
        next: ()=>{
          console.log("SUCCESS")
          this.router.navigate(["/", "locations"])
        },
        error: ()=>{
          console.log("ERROR")
        }
      })
    } else {
      console.log("Invalid input");
    }
  }

  ngOnInit(): void {}
}
