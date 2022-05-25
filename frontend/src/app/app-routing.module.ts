import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {HomeComponent} from "./components/home/home.component";
import {LoginComponent} from "./components/login/login.component";
import {AuthGuard} from "./guards/auth.guard";
import {MessageComponent} from "./components/message/message.component";
import {SeatingPlanComponent} from "./components/seating-plan/seating-plan.component";
import {OrderOverviewComponent} from "./components/order-overview/order-overview.component";
import {RegistrationComponent} from "./components/registration/registration.component";
import {CreateEventComponent} from './components/create-event/create-event.component';
import {CreateShowComponent} from './components/create-show/create-show.component';
import {UnlockUserComponent} from "./components/unlock-user/unlock-user.component";
import {AdminComponent} from "./components/admin/admin.component";
import {EventsComponent} from "./components/events/events.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'message', component: MessageComponent, canActivate: [AuthGuard], data: {role: ["USER", "ADMIN"]} },
  {path: 'buyTickets/:showId', component: SeatingPlanComponent, data: {role: "USER"}},
  {path: 'registration', component: RegistrationComponent},
  {path: 'lockedUsers', component: UnlockUserComponent, canActivate: [AuthGuard],  data: {role: "ADMIN"}},
  {path: 'registration', component: RegistrationComponent},
  {path: 'admin', component: AdminComponent, canActivate: [AuthGuard],  data: {role: "ADMIN"}},
  {path: 'events/create', component: CreateEventComponent, canActivate: [AuthGuard],  data: {role: "ADMIN"}},
  {path: 'events/:id/shows', component: CreateShowComponent, canActivate: [AuthGuard], data: {role: "ADMIN"}},
  {path: 'events', component: EventsComponent},
  {path: 'events/:id/shows', component: CreateShowComponent, canActivate: [AuthGuard], data: {role: "ADMIN"}},
  {path: "orders", component: OrderOverviewComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
