import {AbstractControl, ValidationErrors, ValidatorFn} from "@angular/forms";

export const dateTimeValidator: ValidatorFn = (control: AbstractControl):
    ValidationErrors | null => {
    const date = control.get('date');
    const time = control.get('time');

    const currentDateTime = new Date();
    let monthValue = currentDateTime.getMonth()+1;
    let monthString = monthValue < 10 ? "0" + monthValue : monthValue;
    let dayValue = currentDateTime.getDate();
    let dayString = dayValue < 10 ? "0" + dayValue : dayValue;
    const formattedDate = currentDateTime.getFullYear() +'-'+ 
    monthString +'-'+dayString;
    const formattedTime = currentDateTime.getHours() + ":" + currentDateTime.getMinutes();

    console.log("input date: " + date.value + ", new Date(): ", formattedDate);
    if (date.value < formattedDate) {
        return { dateInPast: true }
    }
    console.log("passed 1");
    console.log(time.value + ", formattedTime: ", formattedTime);
    if (date.value === formattedDate && time.value < formattedTime) {
        return { dateTimeInPast: true }
    }
    console.log("passed 2");
    return null;
};
