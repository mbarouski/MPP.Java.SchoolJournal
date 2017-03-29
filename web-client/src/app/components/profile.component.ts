import {Component, OnInit, AfterViewInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {UsersService} from "../services/users.service";
import {TeachersService} from "../services/teachers.service";
import {Router} from "@angular/router";

@Component({
  moduleId: module.id,
  selector: 'profile-component',
  templateUrl: './templates/profile.component.html',
  styleUrls: ['./styles/profile.component.css']
})
export class ProfileComponent implements AfterViewInit {
  private user: any;
  private teacher: any;
  private pupil: any;
  private role: string;

  russianRoleName = {
    'teacher': () => 'Учитель',
    'pupil': () => 'Ученик',
    'director': () => 'Директор'
  }

  constructor(private usersService: UsersService,
              private teachersService: TeachersService,
              private authService: AuthService,
              private router: Router) { }

  ngAfterViewInit() {
    if(!this.authService.user){
      return this.router.navigate(['/login']);
    }
    this.fetchUser();
  }

  fetchUser() {
    if(!this.authService.user) return;
    this.role = this.authService.user.user.role.name.toLowerCase();

    switch (this.role) {
      case 'teacher':
        this.teachersService.fetchTeacher(this.authService.user.userId)
          .then(teacher => {
            debugger;
            this.teacher = teacher;
          });
        break;
      case 'pupil':
        break;
      default:
        break;
    }
    this.user = this.authService.user.user;
  }

}
