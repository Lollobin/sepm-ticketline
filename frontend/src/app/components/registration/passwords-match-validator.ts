import {AbstractControl, ValidationErrors, ValidatorFn} from "@angular/forms";

export const passwordMatchValidator: ValidatorFn = (control: AbstractControl):
    ValidationErrors | null => {
  const pass = control.get('password');
  const passConfirm= control.get('confirmPassword');

  return pass?.value === passConfirm?.value ? null : { passwordsDoNotMatch: true };

};
