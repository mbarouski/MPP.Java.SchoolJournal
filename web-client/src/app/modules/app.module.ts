import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import  {AppRoutingModule } from './app-routing.module';

import { AppComponent }  from '../components/app.component';
import { RolesComponent } from "../components/roles.component";
import { RolesService } from "../services/roles.service";
import {APP_CONFIG, AppConfig} from "../configs/app.config";
import {HttpModule} from "@angular/http";
import {MenuComponent} from "../components/menu.component";
import {Ng2BootstrapModule, CollapseModule} from "ng2-bootstrap";
import {ProfileComponent} from "../components/profile.component";

@NgModule({
  imports: [
    Ng2BootstrapModule,
    BrowserModule,
    AppRoutingModule,
    HttpModule,
    CollapseModule.forRoot()
  ],
  declarations: [
    AppComponent,
    RolesComponent,
    MenuComponent,
    ProfileComponent
  ],
  providers: [
    RolesService,
    { provide: APP_CONFIG, useValue: AppConfig },
  ],
  bootstrap:    [
    AppComponent
  ]
})
export class AppModule { }
