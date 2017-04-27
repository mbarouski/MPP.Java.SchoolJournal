import {Component, AfterViewInit, ViewChild} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {ModalDirective} from "ng2-bootstrap";
import {User} from "../models/User";
import {UsersService} from "../services/users.service";
import ROLES from "./constants/roles.constants";
import {TeacherPupil} from "../models/TeacherPupil";
import {RolesService} from "../services/roles.service";
import {PupilsService} from "../services/pupils.service";
import {TeachersService} from "../services/teachers.service";

@Component({
  moduleId: module.id,
  selector: 'users-component',
  templateUrl: './templates/users.component.html',
})
export class UsersComponent implements AfterViewInit{

  users = [];
  roles = [];
  usersOnPage = [];
  selectedUser: any;
  currentUser: User;
  isEdit: boolean;

  validationError = Object.assign({}, ...[
    'roleId',
    'username',
    'email',
    'firstName',
    'pathronymic',
    'lastName',
    'phoneNumber',
    'description',
  ].map((field) => {
    return {
      [field]: {
        status: false,
        message: '',
      },
    };
  }));

  errorMessage = '';

  currentRole = null;
  currentTeacherPupil: TeacherPupil;

  totalPageCount = 0;
  currentPage = 1;
  perPage = 15;

  @ViewChild('userModal') public userModal: ModalDirective;
  @ViewChild('userRoleModal') public userRoleModal: ModalDirective;

  constructor(private authService: AuthService,
              private router: Router,
              private usersService: UsersService,
              private pupilsService: PupilsService,
              private teacherService: TeachersService,
              private rolesService: RolesService) {
    usersService.usersSubject.subscribe(users => {
      this.users = users;
      this.setPaginationState();
    });
    rolesService.rolesSubject.subscribe(roles => {
      this.roles = roles;
    });
  }

  ngAfterViewInit() {
    if(!this.authService.isLogged()) return this.router.navigate(['/login']);
    this.usersService.fetchUsers();
  }

  findRoleById(id) {
    return this.roles.find(role => role.roleId === +id);
  }

  decorateRole(role) {
    return ROLES[role];
  }

  selectUser(id) {
    this.selectedUser = this.users.find(user => user.userId == id);
  }

  closeUserModal() {
    this.userModal.hide();
  }

  getRoleById(id) {
    return this.roles.find(role => role.roleId == +id).name;
  }

  openUserModal() {
    this.currentUser = new User(0);
    this.userModal.show();
  }

  restoreState() {
    this.closeUserModal();
    this.closeUserRoleModal();
    this.currentRole = null;
    this.currentUser = null;
    this.currentTeacherPupil = null;
    this.selectedUser = null;
    this.usersService.fetchUsers();
  }

  validateUser() {
    const username = this.currentUser.username;
    const email = this.currentUser.email;
    if(!username) {
      this.validationError.username.status = true;
      this.validationError.username.message = 'Введите имя пользователя';
    } else if(!/^[A-Za-z_0-9]+$/g.test(username) || username.length < 6) {
      this.validationError.username.status = true;
      this.validationError.username.message = 'Имя пользователя может состоять из латинских символов, цифр и знака подчёркивания';
    } else {
      this.validationError.username.status = false;
    }
    if(!email) {
      this.validationError.email.status = true;
      this.validationError.email.message = 'Введите почтовый адрес';
    }  else {
      this.validationError.email.status = false;
    }
   }

  isUserValid() {
    return !this.validationError.username &&
        !this.validationError.password;
  }

  onUserFormSubmit() {
    this.validateUser();
    if(!this.isUserValid()) return;
    this.usersService.addUser(this.currentUser)
      .then(user => {
        this.restoreState();
      });
  }

  validateRoleInfo() {
    const roleId = this.currentRole;
    const role = this.findRoleById(roleId);
    if(!role) {
      this.validationError.roleId.status = true;
      this.validationError.roleId.message = 'Некорректная роль';
    } else {
      this.validationError.roleId.status = false;
    }
    if(['pupil', 'teacher', 'director_of_studies', 'director'].includes(role.name) && role.name !== this.selectedUser.role.name) {
      this.validateProfileData();
    } else {
      this.validationError.firstName.status = false;
      this.validationError.lastName.status = false;
      this.validationError.pathronymic.status = false;
      this.validationError.phoneNumber.status = false;
      this.validationError.description.status = false;
    }

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

  isRoleInfoValid() {
    const roleId = this.currentRole;
    const role = this.findRoleById(roleId);
    let result =  !this.validationError.roleId.status;
    if(['pupil', 'teacher', 'director_of_studies', 'director'].includes(role.name)) {
      result = result && this.isProfileDataValid();
    }
    return result;
  }

  onUserRoleFormSubmit() {
    if(this.getRoleById(this.currentRole) === this.selectedUser.role.name) {
      this.restoreState();
    }
    this.validateRoleInfo();
    if(!this.isRoleInfoValid()) return;
    this.usersService.changeRole(this.selectedUser.userId, this.currentRole)
      .then(() => {
        if(this.getRoleById(this.currentRole) === 'pupil') {
          this.pupilsService.addPupil(this.currentTeacherPupil)
            .then(pupil => {
              this.restoreState();
            });
        } else if(['teacher', 'director', 'director_of_studies'].includes(this.getRoleById(this.currentRole))) {
          this.teacherService.addTeacher(this.currentTeacherPupil)
            .then(teacher => {
              this.restoreState();
            });
        } else {
          this.restoreState();
        }
      });

  }

  openModalForChangeUserRole() {
    this.currentRole = this.selectedUser.role.roleId;
    this.userRoleModal.show();
  }

  onChangeRole() {
    if(this.getRoleById(this.currentRole) === this.selectedUser.role.name) {
      this.currentTeacherPupil = null;
      return;
    }
    if(['teacher', 'director', 'director_of_studies', 'pupil'].includes(this.getRoleById(this.currentRole))) {
      this.currentTeacherPupil = new TeacherPupil(this.selectedUser.userId);
    }

  }

  closeUserRoleModal() {
    this.userRoleModal.hide();
    this.currentRole = null;
    this.currentTeacherPupil = null;
  }

  deleteUser() {
    this.usersService.deleteUser(this.selectedUser.userId)
      .then(() => {
        this.restoreState();
      });
  }

  setPaginationState() {
    this.perPage = 15;
    this.currentPage = 1;
    this.calculatePageCount();
    this.setUsersOnPage();
  }

  setUsersOnPage() {
    let from = (this.currentPage - 1) * this.perPage;
    let to = from + this.perPage;
    to = to > this.users.length ? this.users.length : to;
    this.usersOnPage = this.users.slice(from, to);
  }

  changePage(page) {
    this.currentPage = page;
    this.setUsersOnPage();
  }

  calculatePageCount() {
    this.totalPageCount = Math.ceil(this.users.length / this.perPage);
  }

  getPages() {
    let result = [];
    let i = 1;
    while(i <= this.totalPageCount) {
      result.push(i++);
    }
    return result;
  }
}
