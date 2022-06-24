import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";

import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { HeaderComponent } from "./components/header/header.component";
import { FooterComponent } from "./components/footer/footer.component";
import { HomeComponent } from "./components/home/home.component";
import { LoginComponent } from "./components/login/login.component";
import { MessageComponent } from "./components/message/message.component";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { httpInterceptorProviders } from "./interceptors";
import { SeatingPlanComponent } from "./components/seating-plan/seating-plan.component";
import { CreateEventComponent } from "./components/create-event/create-event.component";
import { CreateShowComponent } from "./components/create-show/create-show.component";
import { ErrorAlertComponent } from "./components/error-alert/error-alert.component";
import { RegistrationComponent } from "./components/registration/registration.component";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { UnlockUserComponent } from "./components/unlock-user/unlock-user.component";
import { SeatingPlanEditorComponent } from "./components/seating-plan-editor/seating-plan-editor.component";
import { CreateSeatingPlanComponent } from './components/create-seating-plan/create-seating-plan.component';
import { OrderOverviewComponent } from './components/order-overview/order-overview.component';
import { AdminComponent } from './components/admin/admin.component';
import {AuthService} from "./generated-sources/openapi";
import { EventsComponent } from './components/events/events.component';
import { ArtistSearchComponent } from './components/artist-search/artist-search.component';
import { EventSearchResultComponent } from './components/event-search-result/event-search-result.component';
import { ShowSearchComponent } from './components/show-search/show-search.component';
import { EventSearchComponent} from "./components/event-search/event-search.component";
import { LocationSearchComponent } from './components/location-search/location-search.component';
import { ShowSearchResultComponent } from './components/show-search-result/show-search-result.component';
import { CreateLocationComponent } from './components/create-location/create-location.component';
import { LocationSeatingPlansComponent } from './components/location-seating-plans/location-seating-plans.component';
import { LocationAdminOverviewComponent } from './components/location-admin-overview/location-admin-overview.component';
import { CreateArticleComponent } from './components/create-article/create-article.component';
import { EditUserComponent } from './components/edit-user/edit-user.component';
import { PasswordResetComponent } from "./components/password-reset/password-reset.component";
import {PasswordUpdateComponent} from "./components/password-update/password-update.component";
import {NewsOverviewComponent} from './components/news-overview/news-overview.component';
import {
  ArticleDetailedViewComponent
} from './components/article-detailed-view/article-detailed-view.component';
import {UserManagementComponent} from './components/user-management/user-management.component';
import {UserDetailComponent} from './components/user-detail/user-detail.component';
import {TopEventsComponent} from './components/top-events/top-events.component';
import {LazyLoadImageModule} from "ng-lazyload-image";
import { CreateUserComponent } from './components/create-user/create-user.component';
import {ImageCropperModule} from "ngx-image-cropper";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { ToastrModule } from "ngx-toastr";

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
    SeatingPlanEditorComponent,
    CreateSeatingPlanComponent,
    AdminComponent,
    EventsComponent,
    ArtistSearchComponent,
    EventSearchResultComponent,
    AdminComponent,
    UnlockUserComponent,
    OrderOverviewComponent,
    ShowSearchComponent,
    EventSearchComponent,
    LocationSearchComponent,
    ShowSearchResultComponent,
    CreateLocationComponent,
    LocationSeatingPlansComponent,
    EditUserComponent,
    PasswordResetComponent,
    PasswordUpdateComponent,
    LocationAdminOverviewComponent,
    NewsOverviewComponent,
    ArticleDetailedViewComponent,
    CreateArticleComponent,
    TopEventsComponent,
    LocationSeatingPlansComponent,
    UserManagementComponent,
    UserDetailComponent,
    CreateUserComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    FontAwesomeModule,
    LazyLoadImageModule,
    ImageCropperModule, 
    BrowserAnimationsModule,
    ToastrModule.forRoot({positionClass: 'toast-bottom-right', progressBar: true, timeOut: 7000})
  ],
  providers: [httpInterceptorProviders, AuthService, UnlockUserComponent],
  bootstrap: [AppComponent]
})
export class AppModule {
}
