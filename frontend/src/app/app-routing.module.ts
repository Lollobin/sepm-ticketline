import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import { SeatingPlanComponent } from './components/seating-plan/seating-plan.component';
import {RegistrationComponent} from "./components/registration/registration.component";
import { CreateEventComponent } from './components/create-event/create-event.component';
import { CreateShowComponent } from './components/create-show/create-show.component';
import {UnlockUserComponent} from "./components/unlock-user/unlock-user.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'message', canActivate: [AuthGuard], component: MessageComponent},
  {path: 'buyTickets/:showId', component: SeatingPlanComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'lockedUsers', component: UnlockUserComponent, canActivate: [AuthGuard]},
  {path: 'registration', component: RegistrationComponent},
  {path: 'events/create', component: CreateEventComponent, canActivate: [AuthGuard]},
  {path: 'events/:id/shows', component: CreateShowComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
