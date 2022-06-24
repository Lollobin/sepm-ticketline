import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {faLock, faLockOpen, faRotateLeft, faTrashCan} from '@fortawesome/free-solid-svg-icons';
import {AdminPasswordReset, UserManagementService} from "../../generated-sources/openapi";
import {UnlockUserComponent} from "../unlock-user/unlock-user.component";
import {Location} from '@angular/common';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.scss']
})
export class UserDetailComponent implements OnInit {
  @Input() user;
  @Output() reload: EventEmitter<any> = new EventEmitter<any>();


  success;
  faLockOpen = faLockOpen;
  faLockClose = faLock;
  faTrashCan = faTrashCan;
  faRotateLeft = faRotateLeft;
  clientURI = "http://" + window.location.host + "/#/passwordUpdate";
  mail;
  lockStatus;
  lock = false;
  passW = false;


  constructor(private location: Location, private unlockComponent: UnlockUserComponent,
              private route: ActivatedRoute, private userManagementService: UserManagementService, private toastr: ToastrService) {
  }

  ngOnInit(): void {

  }

  refreshPage() {
    this.userManagementService.usersIdGet(this.user.userId).subscribe(
        {
          next: user => {
            this.user = user;
          }, error: (error) => {
            console.log(error);
            if (error.status === 0 || error.status === 500) {
              this.toastr.error(error.message);
            } else {
              this.toastr.warning(error.error);
            }
          }
        }
    );
  }

  manageLockedStatus(id, mail, body) {
    this.userManagementService.lockStatusIdPut(id, body).subscribe({
      next: () => {
        this.lock = true;
        this.passW = false;
        let status: string;
        if (body) {
          status = "locked";
        } else {
          status = "unlocked";
        }
        this.lockStatus = status;
        this.mail = mail;
        this.success = true;

        this.toastr.success("Succesfully " + status + " user!");
        this.reload.emit( null);
        this.refreshPage();
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

  passwordReset(id) {
    const adminpasswordReset: AdminPasswordReset = {
      clientURI: this.clientURI
    };
    this.userManagementService.passwordResetIdPost(id, adminpasswordReset).subscribe(
        {
          next: () => {
            this.mail = this.user.email;
            this.lock = false;
            this.passW = true;

            this.success = "Successfully reset password of user " + this.user.email + "!";
            this.toastr.success("Succesfully reset password of user '" + this.user.email + "'!");
          },
          error: (error) => {
            console.log(error);
            if (error.status === 0 || error.status === 500) {
              this.toastr.error(error.message);
            } else {
              this.toastr.warning(error.error);
            }
          }
        }
    );
  }

  back(): void {
    this.location.back();
  }


  vanishSuccess() {
    this.success = null;
  }

}
