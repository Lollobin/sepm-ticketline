import {Component, OnInit, TemplateRef} from "@angular/core";
import {Order, TicketsService, TicketWithShowInfo} from "../../generated-sources/openapi";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {FormArray, FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: "app-order-overview",
  templateUrl: "./order-overview.component.html",
  styleUrls: ["./order-overview.component.scss"]
})
export class OrderOverviewComponent implements OnInit {
  orders: Order[];
  tickets: TicketWithShowInfo[];
  today = new Date();

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
      },
      complete: () => {
        console.log("Received orders");
      }
    });
  }

  openAddModal(messageAddModal: TemplateRef<any>, reservation: TicketWithShowInfo) {
    this.selectedReservation = reservation;

    this.purchaseForm = this.formBuilder.group({
      tickets: new FormArray([])
    });
    this.addCheckboxes();

    this.modalService.open(messageAddModal, {ariaLabelledBy: "modal-basic-title"});
  }

  dateInFuture(date: string) {
    const orderDate = new Date(date);
    return orderDate > this.today;
  }

  purchaseTickets() {
    console.log(this.purchaseForm.value);
    const selectedTicketIds = this.purchaseForm.value.tickets
    .map((checked, i) => checked ? this.selectedReservation.ticket[i].ticketId : null)
    .filter(v => v !== null);
    console.log(selectedTicketIds);
  }

  private addCheckboxes() {
    this.selectedReservation.ticket.forEach(() => this.ticketsFormArray.push(new FormControl(false)));
  }

}
