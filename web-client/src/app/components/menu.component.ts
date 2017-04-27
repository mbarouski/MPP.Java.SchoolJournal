import { Component } from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";

@Component({
  moduleId: module.id,
  selector: 'menu-component',
  templateUrl: './templates/menu.component.html',
  styleUrls: ['./styles/menu.component.css']
})
export class MenuComponent {
  isCollapsed: Boolean = true;
  username: string;
  roles = ['user', 'pupil', 'teacher', 'director_of_studies', 'director'];

  constructor(private authService: AuthService, private router: Router) {
    this.authService.userSubject.subscribe(user => {
      this.username = user ? user.username : null;
    });
  }

  hasNeedLevel(role) {
    return this.roles.indexOf(role) <= this.roles.indexOf(this.authService.role);
  }

  onLogoutClick(e) {
    e.preventDefault();
    this.authService.logout()
      .then(() => this.router.navigate(['/login']));
  }
}
