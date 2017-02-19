import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import  {AppRoutingModule } from './app-routing.module';

import { AppComponent }  from '../components/app.component';
import { RolesComponent } from "../components/roles.component";
import { RolesService } from "../services/roles.service";
import {APP_CONFIG, AppConfig} from "../configs/app.config";
import {HttpModule} from "@angular/http";


@NgModule({
  imports:      [
    BrowserModule,
    AppRoutingModule,
    HttpModule,
  ],
  declarations: [
    AppComponent,
    RolesComponent,
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
