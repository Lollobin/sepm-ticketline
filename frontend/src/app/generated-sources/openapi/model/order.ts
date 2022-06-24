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
import { Artist } from './artist';
import { BookingType } from './bookingType';


export interface Order { 
    type: BookingType;
    transactionDate?: string;
    showDate?: string;
    artists: Array<Artist>;
    eventName: string;
    city: string;
    locationName: string;
    transactionId: number;
    ticketIds: Array<number>;
}

