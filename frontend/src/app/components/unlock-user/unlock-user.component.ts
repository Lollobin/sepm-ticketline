import { Component, OnInit } from "@angular/core";
import {User, UserManagementService} from "../../generated-sources/openapi";

@Component({
  selector: "app-unlock-user",
  templateUrl: "./unlock-user.component.html",
  styleUrls: ["./unlock-user.component.scss"]
})
export class UnlockUserComponent implements OnInit {

  users: User[];
  error = "";
  empty = false;
  success = false;
  firstName = "";
  lastName = "";

  constructor(private userManagementService: UserManagementService) { }

  ngOnInit(): void {
      this.reloadUser();
  }

  // getAll(){
  //   this.userManagementService.usersGet(false).subscribe({
  //     next: value => {
  //       console.log("all", value);
  //     }
  //   });
  // }

  reloadUser() {
    this.userManagementService.usersGet(true).subscribe({
      next: user => {
        this.users = user;
        console.log("received user", user);
        // this.getAll();
      },
      error: err => {
        console.log("Error fetching user: ", err.message);
        this.showError(err.error.message);
      }
    });
  }

  unlockUser(id: number) {
    console.log("hier" + id);
    this.userManagementService.lockStatusIdPut(id, false).subscribe({
      next: () => {
        this.success = true;
        this.reloadUser();
      },
      error: err => {
        this.showError(err.error.message);
      }
    });
  }
  public vanishempty(): void {
    this.empty = null;
  }
  public vanishError(): void {
    this.error = null;
  }
  public vanishSuccess(): void {
    this.success = null;
  }
  private showError(msg: string) {
    this.error = msg;
  }













}
