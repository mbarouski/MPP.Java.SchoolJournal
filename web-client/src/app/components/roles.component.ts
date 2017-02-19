import { Component } from '@angular/core';
import {RolesService} from "../services/roles.service";
import {Subject, Observable} from 'rxjs';

@Component({
  moduleId: module.id,
  selector: 'roles-component',
  template: '<h1>Roles component</h1>'
})
export class RolesComponent  {
  roles: any[] = [];

  constructor(private rolesService: RolesService){
    rolesService.rolesSubject.subscribe((roles) => {
      this.roles = roles;
    });
  }
}
