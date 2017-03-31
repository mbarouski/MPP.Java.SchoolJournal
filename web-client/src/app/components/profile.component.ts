import {Component, OnInit, AfterViewInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {UsersService} from "../services/users.service";
import {TeachersService} from "../services/teachers.service";
import {Router} from "@angular/router";
import {PupilsService} from "../services/pupils.service";

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
              private pupilsService: PupilsService,
              private authService: AuthService,
              private router: Router) { }

  ngAfterViewInit() {
    if(!this.authService.isLogged()){
      return this.router.navigate(['/login']);
    }
    this.fetchUser();
  }

  fetchUser() {
    if(!this.authService.isLogged()) return;
    this.role = this.authService.role;

    switch (this.role) {
      case 'teacher':
        this.teachersService.fetchTeacher(this.authService.user.userId)
          .then(teacher => {
            this.teacher = teacher;
          });
        break;
      case 'pupil':
        this.pupilsService.fetchPupil(this.authService.user.userId)
          .then(pupil => {
            this.pupil = pupil;
          });
        break;
      default:
        break;
    }
    this.user = this.authService.user;
  }

  getFullName() {
    switch(this.role) {
      case 'pupil':
        if(!this.pupil) return '';
        return `${this.pupil.firstName} ${this.pupil.pathronymic} ${this.pupil.lastName}`;
      case 'teacher':
      case 'director_of_studies':
        if(!this.teacher) return '';
        return `${this.teacher.firstName} ${this.teacher.pathronymic} ${this.teacher.lastName}`;
      default:
        return '';
    }
  }

}
