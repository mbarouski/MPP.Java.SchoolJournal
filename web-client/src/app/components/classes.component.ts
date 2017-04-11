import {Component, AfterViewInit, ViewChild} from '@angular/core';
import {RolesService} from "../services/roles.service";
import {Subject, Observable} from 'rxjs';
import {TeachersService} from "../services/teachers.service";
import {AuthService} from "../services/auth.service";
import {ClassesService} from "../services/classes.service";
import {Router} from "@angular/router";
import {PupilsService} from "../services/pupils.service";
import {ModalDirective} from "ng2-bootstrap";
import {Class} from "../models/Class";

@Component({
  moduleId: module.id,
  selector: 'classes-component',
  templateUrl: './templates/classes.component.html',
})
export class ClassesComponent implements AfterViewInit{
  selectedClass: any;
  selectedPupil: any;
  selectedPupilWithoutClass: any;
  pupils = [];
  classes = [];
  teachers = [];
  pupilsWithoutClass = [];
  currentClass: any;
  classForPupil = {
    classId: 0
  };
  classTeacher: number;

  @ViewChild('classModal') public classModal: ModalDirective;
  @ViewChild('classForPupilModal') public classForPupilModal: ModalDirective;
  @ViewChild('classTeacherModal') public classTeacherModal: ModalDirective;

  constructor(private router: Router,
              private authService: AuthService,
              private classesService: ClassesService,
              private teachersService: TeachersService,
              private pupilsService: PupilsService){
    classesService.classesSubject.subscribe(classes => {
      this.classes = classes;
    });
    teachersService.teachersSubject.subscribe(teachers => {
      this.teachers = teachers;
    });
  }

  ngAfterViewInit() {
    if(!this.authService.isLogged()) this.router.navigate(['/login']);
    this.restoreState();
  }

  getClassById(id) {
    return this.classes.find(clazz => {
      return clazz.classId === id;
    });
  }

  selectClass(classId) {
    this.selectedClass = this.getClassById(classId);
    this.pupilsService.fetchPupilsByClass(classId)
      .then((pupils:any) => {
        this.pupils = pupils;
      });
  }

  getPupilById(id) {
    return this.pupils.find(pupil => {
      return pupil.userId === id;
    });
  }

  getPupilWithoutClassById(id) {
    return this.pupilsWithoutClass.find(pupil => {
      return pupil.userId == id;
    });
  }

  selectPupil(pupilId) {
    this.selectedPupil = this.getPupilById(pupilId);
  }

  selectPupilWithoutClass(pupilId) {
    this.selectedPupilWithoutClass = this.getPupilWithoutClassById(pupilId);
  }

  removeFromClass() {
    if(!this.selectedPupil) return;
    this.pupilsService.removeFromClass(this.selectedPupil.userId)
      .then((pupil) => {
        this.selectClass(this.selectedClass.classId);
        this.pupilsService.fetchPupilsWithoutClass();
      });
  }

  openModalForAddClass() {
    this.currentClass = new Class(0);
    this.classModal.show();
  }

  closeClassModal() {
    this.classModal.hide();
    this.currentClass = null;
  }

  onFormSubmit() {
    this.classesService.createClass(this.currentClass)
      .then(clazz => {
        this.restoreState();
      });
  }

  restoreState() {
    this.classesService.fetchClasses();
    this.teachersService.fetchTeachers();
    this.pupilsService.fetchPupilsWithoutClass()
      .then((pupils: any) => {
        this.pupilsWithoutClass = pupils;
      });
    this.selectedClass = null;
    this.selectedPupil = null;
    this.selectedPupilWithoutClass = null;
    this.closeClassModal();
    this.closeClassForPupilModal();
    this.closeClassTeacherModal();
  }

  deleteClass() {
    if(!this.selectedClass) return;
    this.classesService.deleteClass(this.selectedClass.classId)
      .then(() => {
        this.restoreState();
      });
  }

  getClassTeacherOfSelectedClass() {
    let teacher = this.teachers.find(teacher => teacher.classId == this.selectedClass.classId);
    return teacher ? this.teachersService.getTeacherFullName(teacher) : 'Не назначен'
  }

  onClassForPupilFormSubmit() {
    debugger;
    if(!this.selectedPupilWithoutClass && !this.classForPupil.classId) return;
    this.pupilsService.movePupilToAnotherClass(this.selectedPupilWithoutClass.userId, +this.classForPupil.classId)
      .then(() => {
        this.restoreState();
        this.closeClassForPupilModal();
        this.closeClassForPupilModal();
      });
  }

  openClassForPupilModal() {
    this.classForPupil.classId = 0;
    this.classForPupilModal.show();
  }

  closeClassForPupilModal() {
    this.classForPupilModal.hide();
  }

  openClassTeacherModal() {
    this.classTeacher = -1;
    this.classTeacherModal.show();
  }

  onClassTeacherFormSubmit() {
    this.teachersService.setAsClassTeacher(this.classTeacher, this.selectedClass.classId)
      .then(() => {
        this.restoreState();
      });
  }

  closeClassTeacherModal() {
    this.classTeacherModal.hide();
    this.classTeacher = -1;
  }

}
