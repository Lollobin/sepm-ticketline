import { Component, OnInit } from "@angular/core";
import {UserService} from "../../services/user.service";
import {User} from "../../generated-sources/openapi";

@Component({
  selector: "app-unlock-user",
  templateUrl: "./unlock-user.component.html",
  styleUrls: ["./unlock-user.component.scss"]
})
export class UnlockUserComponent implements OnInit {

  users: User[];
  error = "";
  success = null;
  firstName = "";
  lastName = "";

  constructor(private userService: UserService) { }

  ngOnInit(): void {
      this.reloadUser();
  }

  reloadUser() {
    this.userService.getLockedUser().subscribe({
      next: user => {
        this.users = user;
        console.log("received user", user);
      },
      error: err => {
        console.log("Error fetching user: ", err.message);
        this.showError(err.error.message);
      }
    });
  }

  unlockUser(id: number) {
    this.userService.unlockUser(id).subscribe({
      next: () => {
        this.success = true;
      },
      error: err => {
        this.showError(err.error.message);
      }
    });
  }

  private showError(msg: string) {
    this.error = msg;
  }


}
