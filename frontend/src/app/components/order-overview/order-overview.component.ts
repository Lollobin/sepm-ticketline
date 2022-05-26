import {Component, OnInit, TemplateRef} from "@angular/core";
import {Order, TicketsService, TicketWithShowInfo} from "../../generated-sources/openapi";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {FormArray, FormBuilder, FormControl, FormGroup, ValidatorFn} from "@angular/forms";

@Component({
  selector: "app-order-overview",
  templateUrl: "./order-overview.component.html",
  styleUrls: ["./order-overview.component.scss"]
})
export class OrderOverviewComponent implements OnInit {
  orders: Order[];
  tickets: TicketWithShowInfo[];
  today = new Date();
  error = undefined;

  selectedReservation: TicketWithShowInfo;
  purchaseForm: FormGroup;

  constructor(private ticketService: TicketsService,
              private modalService: NgbModal,
              private formBuilder: FormBuilder) {
  }

  get ticketsFormArray() {
    return this.purchaseForm.controls.tickets as FormArray;
  }

  ngOnInit(): void {
    this.ticketService.ticketInfoGet().subscribe({
      next: data => {
        this.tickets = data;
      },
      error: error => {
        console.error("Error getting tickets", error.message);
        this.setError(error);
      },
      complete: () => {
        console.log("Received tickets");
      }
    });

    this.ticketService.ordersGet().subscribe({
      next: data => {
        this.orders = data;
      },
      error: error => {
        console.error("Error getting orders", error.message);
        this.setError(error);
      },
      complete: () => {
        console.log("Received orders");
      }
    });
  }

  openPurchaseModal(messageAddModal: TemplateRef<any>, reservation: TicketWithShowInfo) {
    this.selectedReservation = reservation;
    this.purchaseForm = this.formBuilder.group({
      tickets: new FormArray([], minSelectedCheckboxes(1))
    });
    this.addCheckboxes();
    this.modalService.open(messageAddModal, {ariaLabelledBy: "modal-basic-title"});
  }

  purchaseTickets() {
    const selectedTicketIds = this.purchaseForm.value.tickets
    .map((checked, i) => checked ? this.selectedReservation.ticket[i].ticketId : null)
    .filter(v => v !== null);

    console.log("Buying tickets:" + selectedTicketIds);

    this.ticketService
    .ticketsPost({
      reserved: [],
      purchased: selectedTicketIds,
    })
    .subscribe({
      next: (response) => {
        console.log(response);
      },
      error: (error) => {
        this.setError(error);
      }, complete: () => {
        this.modalService.dismissAll();
        this.ngOnInit();
      }
    });

    /*
    TODO: uncomment when backend is implemented
    const unSelectedTicketIds = this.purchaseForm.value.tickets
    .map((checked, i) => checked ? null : this.selectedReservation.ticket[i].ticketId)
    .filter(v => v !== null);
    console.log("Cancelling reservations:" + selectedTicketIds);

    this.ticketService
    .ticketCancellationsPost({
      reserved: [unSelectedTicketIds],
      purchased: [],
    })
    .subscribe({
      next: (response) => {
        console.log(response);
      },
      error: (error) => {
        this.setError(error);
      },
    });
     */
  }

  setError(error: any) {
    this.error = error;
  }

  private addCheckboxes() {
    this.selectedReservation.ticket.forEach(() => this.ticketsFormArray.push(new FormControl(false)));
  }
}

// eslint-disable-next-line prefer-arrow/prefer-arrow-functions
function minSelectedCheckboxes(min = 1) {
  const validator: ValidatorFn = (formArray: FormArray) => {
    const totalSelected = formArray.controls
    // get a list of checkbox values (boolean)
    .map(control => control.value)
    // total up the number of checked checkboxes
    .reduce((prev, next) => next ? prev + next : prev, 0);

    // if the total is not greater than the minimum, return the error message
    return totalSelected >= min ? null : {required: true};
  };

  return validator;
}

