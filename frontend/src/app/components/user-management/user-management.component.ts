import {Component, OnInit} from "@angular/core";
import {User, UserManagementService, UsersPage} from "../../generated-sources/openapi";
import {faArrowRight, faLockOpen} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {

  userDetail;

  data: UsersPage = null;
  page = 1;
  users: User[];
  error = "";
  faLockOpen = faLockOpen;
  faArrowRight = faArrowRight;
  success;
  firstName = "";
  lastName = "";
  userEmail = "";
  errorFetch = "";
  pageSize = 10;
  sort: 'ASC' | 'DESC' = 'ASC';
  numberOfElems = 0;

  constructor(private userManagementService: UserManagementService) {
  }

  ngOnInit(): void {
    this.reloadUser();
  }

  reloadUser() {
    this.userManagementService.usersGet(false, this.pageSize, this.page - 1).subscribe({
      next: data => {
        this.numberOfElems = data.numberOfResults;
        this.users = data.users;
      },
      error: err => {
        console.log("Error fetching users: ", err);
        this.showErrorFetch("Not allowed, " + err.message);

      }
    });
  }

  onPageChange(ngbpage: number) {
    this.page = ngbpage;
    this.reloadUser();
  }

  unlockUser(id: number, email: string) {
    console.log("hier" + id);
    this.userManagementService.lockStatusIdPut(id, false).subscribe({
      next: () => {
        this.userEmail = email;
        this.success = "Successfully unlocked user with email " + email + "!";
        this.reloadUser();
      },
      error: err => {
        console.log("Error unlocking user: ", err);
        this.showError(err.error);
      }
    });
  }


  getDetail(user: User) {
    this.userDetail = user;
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
