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

  validationError = {
    password: {
      status: false,
      message: '',
    },
    firstName: {
      status: false,
      message: '',
    },
    pathronymic: {
      status: false,
      message: '',
    },
    lastName: {
      status: false,
      message: '',
    },
    phoneNumber: {
      status: false,
      message: '',
    },
    description: {
      status: false,
      message: '',
    },
  };

  errorMessage = '';

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

  fetchUser(id) {
    if(id) return this.fetchUserById(id);

    this.user = this.authService.user;
    this.loadAdditionalUserInfo();
  }

  fetchUserById(id) {
    this.usersService.fetchUser(id)
      .then(user => {
        this.user = user;
        this.loadAdditionalUserInfo();
      });
  }

  loadAdditionalUserInfo() {
    switch (this.user.role.name) {
      case 'teacher':
      case 'director_of_studies':
      case 'director':
        this.teachersService.fetchTeacher(this.user.userId)
          .then(teacher => {
            this.teacher = teacher;
          });
        break;
      case 'pupil':
        this.pupilsService.fetchPupil(this.user.userId)
          .then(pupil => {
            this.pupil = pupil;
          });
        break;
      default:
        break;
    }
  }

  validatePassword() {
    const password = this.currentPassword;
    if(!password) {
      this.validationError.password.status = true;
      this.validationError.password.message = 'Введите пароль';
    } else if(password.length < 6 || password.length > 24) {
      this.validationError.password.status = true;
      this.validationError.password.message = 'Пароль должен иметь не менее 6 и не более 24 символов';
    } else {
      this.validationError.password.status = false;
    }
  }

  isPasswordValid() {
    return !this.validationError.password.status;
  }

  validateProfileData() {
    this.validateFirstName();
    this.validatePathronymic();
    this.validateLastName();
    this.validatePhoneNumber();
    this.validateDescription();
  }

  validateDescription() {
    const description = this.currentTeacherPupil.description || this.currentTeacherPupil.characteristic;
    if(!description) {
      this.validationError.description.status = true;
      this.validationError.description.message = 'Введите описание';
    } else {
      this.validationError.description.status = false;
    }
  }

  validatePhoneNumber() {
    const phoneNumber = this.currentTeacherPupil.phoneNumber;
    if(!phoneNumber) {
      this.validationError.phoneNumber.status = true;
      this.validationError.phoneNumber.message = 'Введите номер телефона';
    } else if(!/^\+375[0-9]{9}$/g.test(phoneNumber)) {
      this.validationError.phoneNumber.status = true;
      this.validationError.phoneNumber.message = 'Номер телефона должен быть записан в формате "+375XXXXXXXXX"';
    } else {
      this.validationError.phoneNumber.status = false;
    }
  }

  validateFirstName() {
    const firstName = this.currentTeacherPupil.firstName;
    if(!firstName) {
      this.validationError.firstName.status = true;
      this.validationError.firstName.message = 'Введите имя';
    } else if(!/^[А-Яа-я-]+$/g.test(firstName)) {
      this.validationError.firstName.status = true;
      this.validationError.firstName.message = 'Имя должно содержать только кириллические символы и знак тире "-"';
    } else {
      this.validationError.firstName.status = false;
    }
  }

  validateLastName() {
    const lastName = this.currentTeacherPupil.lastName;
    if(!lastName) {
      this.validationError.lastName.status = true;
      this.validationError.lastName.message = 'Введите фамилию';
    } else if(!/^[А-Яа-я-]+$/g.test(lastName)) {
      this.validationError.lastName.status = true;
      this.validationError.lastName.message = 'Фамилия должна содержать только кириллические символы и знак тире "-"';
    } else {
      this.validationError.lastName.status = false;
    }
  }

  validatePathronymic() {
    const pathronymic = this.currentTeacherPupil.pathronymic;
    if(!pathronymic) {
      this.validationError.pathronymic.status = true;
      this.validationError.pathronymic.message = 'Введите отчество';
    } else if(!/^[А-Яа-я-]+$/g.test(pathronymic)) {
      this.validationError.pathronymic.status = true;
      this.validationError.pathronymic.message = 'Отчество должно содержать только кириллические символы и знак тире "-"';
    } else {
      this.validationError.pathronymic.status = false;
    }
  }

  isProfileDataValid() {
    return !this.validationError.firstName.status &&
      !this.validationError.pathronymic.status &&
      !this.validationError.lastName.status &&
      !this.validationError.phoneNumber.status &&
      !this.validationError.description.status;
  }

  openPasswordModal() {
    this.currentPassword = '';
    this.passwordModal.show();
  }

  onPasswordFormSubmit() {
    this.validatePassword();
    if(!this.isPasswordValid()) return;
    this.usersService.changePassword(this.user.userId, this.currentPassword)
      .then(() => {
        this.closePasswordModal();
      })
      .catch((err) => {
        this.errorMessage = err.status;
      });
  }

  closePasswordModal() {
    this.currentPassword = '';
    this.passwordModal.hide();
  }

  openEditModal() {
    let personInfo = this.user.role == 'pupil' ? this.pupil : this.teacher;
    this.currentTeacherPupil = new TeacherPupil(this.user.userId);
    this.currentTeacherPupil.firstName = personInfo.firstName;
    this.currentTeacherPupil.pathronymic = personInfo.pathronymic;
    this.currentTeacherPupil.lastName = personInfo.lastName;
    this.currentTeacherPupil.phoneNumber = personInfo.phoneNumber;
    this.currentTeacherPupil.description = personInfo.description;
    this.currentTeacherPupil.characteristic = personInfo.characteristic;
    this.editModal.show();
  }

  onEditFormSubmit() {
    this.validateProfileData();
    if(!this.isProfileDataValid()) return;
    let serviceMethod = this.user.role.name === 'pupil'
      ? this.pupilsService.updatePupil.bind(this.pupilsService)
      : this.teachersService.updateTeacher.bind(this.teachersService);

    serviceMethod(this.currentTeacherPupil)
      .then(() => {
        this.closeEditModal();
        this.fetchUserById(this.user.userId);
      })
      .catch((err) => {
        this.errorMessage = err.status;
      });
  }

  closeEditModal() {
    this.currentTeacherPupil = null;
    this.editModal.hide();
  }

}
