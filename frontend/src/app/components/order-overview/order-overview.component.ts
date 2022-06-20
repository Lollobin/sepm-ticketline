import {Component, OnInit, TemplateRef} from "@angular/core";
import {
  OrdersPage,
  Ticket,
  TicketsService,
  TicketStatus,
  TicketWithShowInfo,
  TicketWithShowInfoTypeEnum
} from "../../generated-sources/openapi";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {FormArray, FormBuilder, FormControl, FormGroup, ValidatorFn} from "@angular/forms";
import {forkJoin} from "rxjs";
import {faFileArrowDown} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: "app-order-overview",
  templateUrl: "./order-overview.component.html",
  styleUrls: ["./order-overview.component.scss"],
})
export class OrderOverviewComponent implements OnInit {
  error = undefined;
  tickets: TicketWithShowInfo[];
  today = new Date();
  faFileArrowDown = faFileArrowDown;

  orders: OrdersPage;
  currentPage = 1;
  pageSize = 10;

  selectedTickets: TicketWithShowInfo;
  ticketSelectionForm: FormGroup;

  constructor(
      private ticketService: TicketsService,
      private modalService: NgbModal,
      private formBuilder: FormBuilder
  ) {
  }

  get ticketsFormArray() {
    return this.ticketSelectionForm.controls.tickets as FormArray;
  }

  ngOnInit(): void {
    this.ticketService.ticketInfoGet().subscribe({
      next: (data) => {
        this.tickets = data;
      },
      error: (error) => {
        console.error("Error getting tickets", error.message);
        this.setError(error);
      },
      complete: () => {
        console.log("Received tickets");
      },
    });
    this.reloadOrders();
  }

  reloadOrders() {
    this.ticketService.ordersGet(this.pageSize, this.currentPage - 1).subscribe({
      next: (data) => {
        this.orders = data;
      },
      error: (error) => {
        console.error("Error getting orders", error.message);
        this.setError(error);
      },
      complete: () => {
        console.log("Received orders");
      },
    });
  }

  onPageChange(ngbpage: number) {
    this.currentPage = ngbpage;
    this.reloadOrders();
  }

  openTicketSelectionModal(
      ticketSelectionModal: TemplateRef<any>,
      ticketsToSelect: TicketWithShowInfo
  ) {
    this.selectedTickets = ticketsToSelect;
    this.ticketSelectionForm = this.formBuilder.group({
      tickets: new FormArray([], minSelectedCheckboxes(1)),
    });
    this.addCheckboxes();
    this.modalService.open(ticketSelectionModal, {ariaLabelledBy: "modal-basic-title"});
  }

  purchaseTickets() {
    const selectedTicketIds: Array<number> = this.ticketSelectionForm.value.tickets
    .map((checked, i) => (checked ? this.selectedTickets.ticket[i].ticketId : null))
    .filter((v) => v !== null);

    const unSelectedTicketIds: Array<number> = this.ticketSelectionForm.value.tickets
    .map((checked, i) => (checked ? null : this.selectedTickets.ticket[i].ticketId))
    .filter((v) => v !== null);

    console.log("Buying tickets:" + selectedTicketIds);
    console.log("Cancelling reservations:" + selectedTicketIds);

    if (unSelectedTicketIds.length > 0) {
      forkJoin([
        this.ticketService.ticketsPost({
          reserved: [],
          purchased: selectedTicketIds,
        }),
        this.ticketService.ticketCancellationsPost({
          reserved: unSelectedTicketIds,
          purchased: [],
        }),
      ]).subscribe({
        next: (response) => console.log(response),
        error: (error) => this.setError(error),
        complete: () => {
          this.modalService.dismissAll();
          this.ngOnInit();
        },
      });
    } else {
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
        },
        complete: () => {
          this.modalService.dismissAll();
          this.ngOnInit();
        },
      });
    }
  }

  cancelTickets() {
    const selectedTicketIds: Array<number> = this.ticketSelectionForm.value.tickets
    .map((checked, i) => (checked ? this.selectedTickets.ticket[i].ticketId : null))
    .filter((v) => v !== null);

    console.log("Cancelling tickets/reservations:" + selectedTicketIds);

    let ticketStatus: TicketStatus;

    if (this.selectedTickets.type === TicketWithShowInfoTypeEnum.Purchased) {
      ticketStatus = {
        reserved: [],
        purchased: selectedTicketIds,
      };
    }

    if (this.selectedTickets.type === TicketWithShowInfoTypeEnum.Reserved) {
      ticketStatus = {
        reserved: selectedTicketIds,
        purchased: [],
      };
    }

    this.ticketService.ticketCancellationsPost(ticketStatus).subscribe({
      next: (response) => {
        console.log(response);
      },
      error: (error) => {
        this.setError(error);
      },
      complete: () => {
        this.modalService.dismissAll();
        this.ngOnInit();
      },
    });
  }

  getTransactionPdf(id) {
    this.ticketService.billsIdGet(id).subscribe({
      next: (blob) => {
        const fileURL = URL.createObjectURL(blob);
        window.open(fileURL, "_blank");
      },
      error: (err) => this.setError(err),
    });
  }

  setError(error: any) {
    this.error = error;
  }

  selectAll() {
    for (let i = 0; i < this.ticketsFormArray.length; i++) {
      this.ticketsFormArray.at(i).setValue(true);
    }
  }

  clearAll() {
    for (let i = 0; i < this.ticketsFormArray.length; i++) {
      this.ticketsFormArray.at(i).setValue(false);
    }
  }

  openTicketPdf(tickets: Ticket[]) {
    let fileErrorCount = 0;
    const ids: Array<number>= new Array<number>();
    for (const ticket of tickets) {
      ids.push( ticket.ticketId);
    }

    this.ticketService.ticketPrintsGet(ids).subscribe({
      next: (blob) => {
        window.open(URL.createObjectURL(blob));
      },
      error: () => {
        fileErrorCount++;
        this.error = new Error("Failed download of " + fileErrorCount + " files");
      },
    });
  }

  private addCheckboxes() {
    this.selectedTickets.ticket.forEach(() => this.ticketsFormArray.push(new FormControl(false)));
  }
}

// eslint-disable-next-line prefer-arrow/prefer-arrow-functions
function minSelectedCheckboxes(min = 1) {
  const validator: ValidatorFn = (formArray: FormArray) => {
    const totalSelected = formArray.controls
    // get a list of checkbox values (boolean)
    .map((control) => control.value)
    // total up the number of checked checkboxes
    .reduce((prev, next) => (next ? prev + next : prev), 0);

    // if the total is not greater than the minimum, return the error message
    return totalSelected >= min ? null : {required: true};
  };

  return validator;
}
