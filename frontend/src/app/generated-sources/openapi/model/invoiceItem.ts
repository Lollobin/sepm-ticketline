/**
 * Ticketline API
 * Documentation for the Ticketline API
 *
 * The version of the OpenAPI document: 0.0.1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { Ticket } from './ticket';
import { BookingType } from './bookingType';


export interface InvoiceItem { 
    priceAtBookingTime: number;
    type: BookingType;
    ticket: Ticket;
}

