import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {MessageComponent} from './components/message/message.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';
import { SeatingPlanComponent } from './components/seating-plan/seating-plan.component';
import { CreateEventComponent } from './components/create-event/create-event.component';
import { CreateShowComponent } from './components/create-show/create-show.component';
import { ErrorAlertComponent } from './components/error-alert/error-alert.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {UnlockUserComponent} from "./components/unlock-user/unlock-user.component";
import { OrderOverviewComponent } from './components/order-overview/order-overview.component';
import { AdminComponent } from './components/admin/admin.component';
import {AuthService} from "./generated-sources/openapi";
import { EventsComponent } from './components/events/events.component';
import { ArtistSearchComponent } from './components/artist-search/artist-search.component';
import { EventSearchResultComponent } from './components/event-search-result/event-search-result.component';
import { LocationSearchComponent } from './components/location-search/location-search.component';
import { ShowSearchResultComponent } from './components/show-search-result/show-search-result.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    MessageComponent,
    SeatingPlanComponent,
    CreateEventComponent,
    CreateShowComponent,
    ErrorAlertComponent,
    RegistrationComponent,
    UnlockUserComponent,
    AdminComponent,
    EventsComponent,
    ArtistSearchComponent,
    EventSearchResultComponent,
    AdminComponent,
    UnlockUserComponent,
    OrderOverviewComponent,
    LocationSearchComponent,
    ShowSearchResultComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    FontAwesomeModule
  ],
  providers: [httpInterceptorProviders, AuthService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
