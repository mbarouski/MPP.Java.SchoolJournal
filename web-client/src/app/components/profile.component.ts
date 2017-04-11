import {Component, OnInit, AfterViewInit, ViewChild} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {UsersService} from "../services/users.service";
import {TeachersService} from "../services/teachers.service";
import {Router, ActivatedRoute, Params} from "@angular/router";
import {PupilsService} from "../services/pupils.service";
import ROLES from "./constants/roles.constants";
import {ModalDirective} from "ng2-bootstrap";
import {TeacherPupil} from "../models/TeacherPupil";
import * as _ from 'lodash';

@Component({
  moduleId: module.id,
  selector: 'profile-component',
  templateUrl: './templates/profile.component.html',
  styleUrls: ['./styles/profile.component.css']
})
export class ProfileComponent implements AfterViewInit, OnInit {
  private user: any;
  private teacher: any;
  private pupil: any;
  private fullName: string;
  roles = ROLES;
  currentTeacherPupil: TeacherPupil;

  currentPassword: string;

  @ViewChild('passwordModal') public passwordModal: ModalDirective;
  @ViewChild('editModal') public editModal: ModalDirective;

  constructor(private usersService: UsersService,
              private teachersService: TeachersService,
              private pupilsService: PupilsService,
              private authService: AuthService,
              private router: Router,
              private activatedRoute: ActivatedRoute) {
  }

  ngAfterViewInit() {
    if(!this.authService.isLogged()) return this.router.navigate(['/login']);
  }

  ngOnInit() {
    this.activatedRoute.params.subscribe((params: Params) => {
      if(params['id']) this.fetchUser(params['id']);
      else this.fetchUser(null);
    });
  }

  fetchUserById(id) {
    this.usersService.fetchUser(id)
      .then(user => {
        this.user = user;
        this.loadAdditionalInfo();
      });
  }

  fetchUser(id) {
    if(!this.authService.isLogged()) return;
    if(id) {
      return this.fetchUserById(id);
    }

    this.user = this.authService.user;
    this.loadAdditionalInfo();
  }

  loadAdditionalInfo() {
    switch (this.user.role.name) {
      case 'teacher':
      case 'director_of_studies':
      case 'director':
        this.teachersService.fetchTeacher(this.user.userId)
          .then(teacher => {
            this.teacher = teacher;
            this.fullName = this.teachersService.getTeacherFullName(teacher);
          });
        break;
      case 'pupil':
        this.pupilsService.fetchPupil(this.user.userId)
          .then(pupil => {
            this.pupil = pupil;
            this.fullName = this.pupilsService.getPupilFullName(pupil);
          });
        break;
      default:
        break;
    }
  }

  openPasswordModal() {
    this.currentPassword = '';
    this.passwordModal.show();
  }

  onPasswordFormSubmit() {
    this.usersService.changePassword(this.user.userId, this.currentPassword)
      .then(() => {
        this.closePasswordModal();
      });
  }

  closePasswordModal() {
    this.currentPassword = '';
    this.passwordModal.hide();
  }


  openEditModal() {
    let personInfo = this.user.role == 'pupil' ? this.pupil : this.teacher;
    this.currentTeacherPupil = new TeacherPupil(this.user.userId);
    this.currentTeacherPupil.classId = personInfo.classId;
    this.currentTeacherPupil.firstName = personInfo.firstName;
    this.currentTeacherPupil.pathronymic = personInfo.pathronymic;
    this.currentTeacherPupil.lastName = personInfo.lastName;
    this.currentTeacherPupil.phoneNumber = personInfo.phoneNumber;
    this.currentTeacherPupil.description = personInfo.description;
    this.currentTeacherPupil.characteristic = personInfo.characteristic;
    this.editModal.show();
  }

  onEditFormSubmit() {
    let serviceMethod = this.user.role === 'pupil'
      ? this.pupilsService.updatePupil.bind(this.pupilsService)
      : this.teachersService.updateTeacher.bind(this.teachersService);

    serviceMethod(this.currentTeacherPupil)
      .then(() => {
        this.closeEditModal();
        this.fetchUserById(this.user.userId);
      });
  }

  closeEditModal() {
    this.currentTeacherPupil = null;
    this.editModal.hide();
  }

}
