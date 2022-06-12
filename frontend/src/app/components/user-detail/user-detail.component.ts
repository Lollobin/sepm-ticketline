import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {faLock, faLockOpen, faRotateLeft, faTrashCan} from '@fortawesome/free-solid-svg-icons';
import {AdminPasswordReset, UserManagementService} from "../../generated-sources/openapi";
import {UnlockUserComponent} from "../unlock-user/unlock-user.component";
import {Location} from '@angular/common';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.scss']
})
export class UserDetailComponent implements OnInit {
  @Input() user;

  error;
  success;
  faLockOpen = faLockOpen;
  faLockClose = faLock;
  faTrashCan = faTrashCan;
  faRotateLeft = faRotateLeft;
  clientURI = "http://" + window.location.host + "/#/passwordUpdate";


  constructor(private location: Location, private unlockComponent: UnlockUserComponent,
              private route: ActivatedRoute, private userManagementService: UserManagementService) {
  }

  ngOnInit(): void {

  }

  refreshPage() {
    this.userManagementService.usersIdGet(this.user.userId).subscribe(
        {
          next: user => this.user = user, error: err => {
            this.error = err;
            console.log(err + "  das ist das problem");
          }
        }
    );
  }

  unlockUser(id, mail) {
    this.userManagementService.lockStatusIdPut(id, false).subscribe({
      next: () => {
        this.success = "Successfully unlocked user with email " + mail + "!";
        this.refreshPage();
      },
      error: err => {
        this.handleError(err);
      }
    });


  }

  passwordReset(id) {
    const adminpasswordReset: AdminPasswordReset = {
      clientURI: this.clientURI
    };
    console.log("reset with id " + id + "and clienturi " + this.clientURI);
    this.userManagementService.passwordResetIdPost(id, adminpasswordReset).subscribe(
        {
          next: () => this.success = "Successfully reset password of user " + this.user.email + "!",
          error: (err) => this.handleError(err)
        }
    );
  }

  handleError(error) {
    this.error = error.message ? error.message : "Unknown Error";
  }

  back(): void {
    this.location.back();
  }

  vanishError() {
    this.error = null;
  }


  vanishSuccess() {
    this.success = null;
  }
}
