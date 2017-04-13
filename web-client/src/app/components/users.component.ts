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

  onUserFormSubmit() {
    this.usersService.addUser(this.currentUser)
      .then(user => {
        this.restoreState();
      });
  }

  onUserRoleFormSubmit() {
    if(this.getRoleById(this.currentRole) === this.selectedUser.role.name) {
      this.restoreState();
    }
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
