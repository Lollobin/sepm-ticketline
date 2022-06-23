import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { LocationsService, LocationWithoutId } from "src/app/generated-sources/openapi";
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: "app-create-location",
  templateUrl: "./create-location.component.html",
  styleUrls: ["./create-location.component.scss"],
})
export class CreateLocationComponent implements OnInit {
  locationForm: FormGroup;
  error: Error;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private locationsService: LocationsService,
    private router: Router,
    private toastr: ToastrService
  ) {
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
      this.locationsService.locationsPost(location).subscribe({
        next: () => {
          this.toastr.success("Succesfully created location!");
          this.router.navigate(["/", "locations"]);
        },
        error: (error) => {
          this.error = error;
          this.toastr.error(error.errorMessage);
        },
      });
    }
  }

  ngOnInit(): void {}
}
