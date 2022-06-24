import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export const dateTimeValidator: ValidatorFn = (control: AbstractControl):
    ValidationErrors | null => {
    const date = control.get('date');
    const time = control.get('time');

    const currentDateTime = new Date();
    const monthValue = currentDateTime.getMonth() + 1;
    const monthString = monthValue < 10 ? "0" + monthValue : monthValue;
    const dayValue = currentDateTime.getDate();
    const dayString = dayValue < 10 ? "0" + dayValue : dayValue;
    const formattedDate = currentDateTime.getFullYear() + '-' +
        monthString + '-' + dayString;
    const formattedTime = currentDateTime.getHours() + ":" + currentDateTime.getMinutes();

    if (date.value < formattedDate) {
        return { dateInPast: true };
    }
    if (date.value === formattedDate && time.value < formattedTime) {
        return { dateTimeInPast: true };
    }
    return null;
};
