import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { LocationWithoutId } from "src/app/generated-sources/openapi";

@Component({
  selector: "app-create-location",
  templateUrl: "./create-location.component.html",
  styleUrls: ["./create-location.component.scss"],
})
export class CreateLocationComponent implements OnInit {
  locationForm: FormGroup;

  submitted = false;

  constructor(private formBuilder: FormBuilder) {
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

  signUpUser() {
    this.submitted = true;
    if (this.locationForm.valid) {
      const location: LocationWithoutId = {
        name: this.locationForm.get("name").value,
        address: this.locationForm.get("address").value,
      };
      //TODO: Add backend service
      //TOOD: Navigate to location with ID
    } else {
      console.log("Invalid input");
    }
  }

  ngOnInit(): void {}
}
