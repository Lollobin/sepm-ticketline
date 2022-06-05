import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {faLock, faLockOpen, faRotateLeft, faTrashCan} from '@fortawesome/free-solid-svg-icons';
import {Gender, User, UserManagementService} from "../../generated-sources/openapi";
import {UnlockUserComponent} from "../unlock-user/unlock-user.component";
import {Location} from '@angular/common';

@Component({
  selector: 'app-user-deatil',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.scss']
})
export class UserDetailComponent implements OnInit {
  error;
  success;
  faLockOpen = faLockOpen;
  faLockClose = faLock;
  faTrashCan = faTrashCan;
  faRotateLeft = faRotateLeft;
  id: number;
  user: User = {
    firstName: 'Fname',
    lastName: 'Lname',
    address: {street: 'street', houseNumber: '5', country: 'AT', city: 'City', zipCode: '1010'},
    email: 'email@email',
    userId: 1,
    gender: Gender.Female,
    lockedAccount: false
  };

  constructor(private location: Location, private unlockComponent: UnlockUserComponent,
              private route: ActivatedRoute, private usermanagementService: UserManagementService) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.id = params["id"];
    });

    this.refreshPage();
  }

  refreshPage() {
    this.usermanagementService.usersIdGet(this.id).subscribe(
        {next: user => this.user = user, error: err => this.error = err}
    );
  }

  unlockUser(id, mail) {
    this.unlockComponent.unlockUser(id, mail);
    if (this.unlockComponent.success) {
      this.success = "Successfully unlocked user with email " + mail + "!";
      this.unlockComponent.success = null;
    } else if (this.unlockComponent.error) {
      this.error = this.unlockComponent.error;
      this.unlockComponent.error = null;
    }
    this.refreshPage();
  }

  passwordReset(id) {
    this.usermanagementService.passwordResetIdPost(id).subscribe(
        {
          next: () => this.success = "Successfully reset password of user " + this.user.email + "!",
          error: (err) => this.handleError(err)
        }
    );
  }

  handleError(error) {
   this.error=  error.message ? error.message : "Unknown Error";
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
