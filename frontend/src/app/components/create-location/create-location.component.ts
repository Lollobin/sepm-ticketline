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
      this.locationsService.locationsPost(location, "response").subscribe({
        next: res => {
          const header = res.headers.get('Location');
          const id = header.split("/").pop();
          this.router.navigateByUrl("/locations/" + id);
          this.toastr.success("Successfully created location!");
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
    }
  }

  ngOnInit(): void {}
}
