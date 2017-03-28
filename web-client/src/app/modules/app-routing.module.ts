import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RolesComponent} from "../components/roles.component";
import {ProfileComponent} from "../components/profile.component";

const routes: Routes = [
  { path: '', redirectTo: '/roles', pathMatch: 'full' },
  { path: 'roles', component: RolesComponent },
  { path: 'profile', component: ProfileComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
