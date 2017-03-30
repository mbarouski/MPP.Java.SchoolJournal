import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RolesComponent} from "../components/roles.component";
import {ProfileComponent} from "../components/profile.component";
import {LoginComponent} from "../components/login.component";

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'roles', component: RolesComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'login', component: LoginComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
