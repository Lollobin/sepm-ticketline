import {Component, OnInit} from "@angular/core";
import {User, UserManagementService} from "../../generated-sources/openapi";
import {faLockOpen} from "@fortawesome/free-solid-svg-icons";

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
  faLockOpen = faLockOpen;

  constructor(private userManagementService: UserManagementService) {
  }

  ngOnInit(): void {
    this.reloadUser();
  }

  reloadUser() {
    this.userManagementService.usersGet(true).subscribe({
      next: user => {
        this.users = user;
        console.log("received users", user);
        this.empty = this.users.length === 0;
      },
      error: err => {
        console.log("Error fetching users: ", err);
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
        console.log("Error unlocking user: ", err);
        this.showError(err.error);
      }
    });
  }

  public vanishEmpty(): void {
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
