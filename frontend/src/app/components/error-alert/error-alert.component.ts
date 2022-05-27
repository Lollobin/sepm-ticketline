import { Component, Input, OnInit } from "@angular/core";

@Component({
  selector: "app-error-alert",
  templateUrl: "./error-alert.component.html",
  styleUrls: ["./error-alert.component.scss"],
})
export class ErrorAlertComponent implements OnInit {
  errorMessage = "";
  isOpen = false;

  @Input() set error(error: any) {
    console.log(error);
    this.defaultServiceErrorHandling(error);
  }

  ngOnInit(): void {}
  vanishError() {
    this.isOpen = false;
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    if (!error) {
      return;
    }
    this.isOpen = true;
    if (error instanceof Error) {
      this.errorMessage = error.message;
    } else if (typeof error.error === "object") {
      this.errorMessage = error.error.message;
    } else {
      this.errorMessage = error.error;
    }
  }
}
