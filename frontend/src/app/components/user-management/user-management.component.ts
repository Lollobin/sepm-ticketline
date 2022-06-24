import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {User, UserManagementService, UsersPage} from "../../generated-sources/openapi";
import {faArrowRight, faLockOpen, faUserPlus} from "@fortawesome/free-solid-svg-icons";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {

  userDetail;

  filterLocked = null;
  data: UsersPage = null;
  page = 1;
  users: User[];
  faLockOpen = faLockOpen;
  faArrowRight = faArrowRight;
  faUserPlus = faUserPlus;
  success;
  firstName = "";
  lastName = "";
  userEmail = "";
  errorFetch = "";
  pageSize = 10;
  sort: 'ASC' | 'DESC' = 'ASC';
  numberOfElems = 0;
  empty = false;

  constructor(private userManagementService: UserManagementService, private toastr: ToastrService) {
  }

  ngOnInit(): void {
    this.reloadUser(null);
  }

  reloadUser(filterLocked: boolean) {
    this.filterLocked = filterLocked;
    this.userManagementService.usersGet(this.filterLocked, this.pageSize, this.page - 1).subscribe({
      next: data => {

        this.numberOfElems = data.numberOfResults;
        this.users = data.users;

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


  handleUnlock($event: any) {
    this.reloadUser($event);
  }

  onPageChange(ngbpage: number) {
    this.page = ngbpage;
    this.reloadUser(this.filterLocked);
  }


  getDetail(user: User) {
    this.userDetail = user;
  }

  public vanishErrorFetch(): void {
    this.errorFetch = null;
  }

  public vanishSuccess(): void {
    this.success = null;
  }


}
