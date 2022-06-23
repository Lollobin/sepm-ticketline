import {Component, OnInit} from "@angular/core";
import {User, UserManagementService, UsersPage} from "../../generated-sources/openapi";
import {faLockOpen} from "@fortawesome/free-solid-svg-icons";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: "app-unlock-user",
  templateUrl: "./unlock-user.component.html",
  styleUrls: ["./unlock-user.component.scss"]
})
export class UnlockUserComponent implements OnInit {

  data: UsersPage = null;
  page = 1;
  users: User[];
  error = "";
  empty = false;
  success = false;
  firstName = "";
  lastName = "";
  userEmail = "";
  faLockOpen = faLockOpen;
  errorFetch = "";
  pageSize = 10;
  sort: 'ASC' | 'DESC' = 'ASC';
  numberOfElems = 0;

  constructor(private userManagementService: UserManagementService, private toastr: ToastrService) {
  }

  ngOnInit(): void {
    this.reloadUser();
  }

  reloadUser() {
    this.userManagementService.usersGet(true, this.pageSize, this.page - 1).subscribe({
      next: data => {
        this.numberOfElems = data.numberOfResults;
        this.users = data.users;
        this.empty = data.users.length === 0;
      },
      error: err => {
        console.log("Error fetching users: ", err);
        this.showErrorFetch("Not allowed, " + err.message);
        this.toastr.error(err.message);
      }
    });
  }

  onPageChange(ngbpage: number) {
    this.page = ngbpage;
    this.reloadUser();
  }

  unlockUser(id: number, email: string) {
    this.userManagementService.lockStatusIdPut(id, false).subscribe({
      next: () => {
        this.userEmail = email;
        this.success = true;
        this.reloadUser();
      },
      error: err => {
        console.log("Error unlocking user: ", err);
        this.showError(err.error);
        this.toastr.error(err.message);
      }
    });
  }

  public vanishEmpty(): void {
    this.empty = null;
  }

  public vanishError(): void {
    this.error = null;
  }

  public vanishErrorFetch(): void {
    this.errorFetch = null;
  }

  public vanishSuccess(): void {
    this.success = null;
  }

  private showError(msg: string) {
    this.error = msg;
  }

  private showErrorFetch(msg: string) {
    this.errorFetch = msg;
  }


}
