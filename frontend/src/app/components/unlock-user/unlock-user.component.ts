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

  public vanishEmpty(): void {
    this.empty = null;
  }

  public vanishErrorFetch(): void {
    this.errorFetch = null;
  }

  public vanishSuccess(): void {
    this.success = null;
  }
}
