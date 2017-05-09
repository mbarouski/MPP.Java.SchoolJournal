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
import {DocsService} from "../services/docs.service";

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

  validationError = {
    number: {
      status: false,
      message: '',
    },
    letterMark: {
      status: false,
      message: '',
    },
    classId: {
      status: false,
      message: '',
    },
    classTeacher: {
      status: false,
      message: '',
    },
  };

  errorMessage = '';

  yesCallbackForDeleteModalComponent = () => {};
  noCallbackForDeleteModalComponent = () => {};
  isDeleteModalComponentActive = false;

  tabs: any[] = [
    {title: 'Классы'},
    {title: 'Ученики, не состоящие в классе'}
  ];

  @ViewChild('classModal') public classModal: ModalDirective;
  @ViewChild('classForPupilModal') public classForPupilModal: ModalDirective;
  @ViewChild('classTeacherModal') public classTeacherModal: ModalDirective;

  constructor(private router: Router,
              private authService: AuthService,
              private classesService: ClassesService,
              private teachersService: TeachersService,
              private pupilsService: PupilsService,
              private docsService: DocsService){
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
    this.errorMessage = '';
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
      })
      .catch((err) => {
        if(err.status === 500) {
          this.errorMessage = 'Извините, ошибка на сервере';
        } else {
          this.errorMessage = err._body;
        }
      })
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

  showModalForRemoveFromClass() {
    this.isDeleteModalComponentActive = true;
    this.yesCallbackForDeleteModalComponent = this.removeFromClass.bind(this);
    this.noCallbackForDeleteModalComponent = () => {
      this.isDeleteModalComponentActive = false;
    };
  }

  showModalForDeleteClass() {
    this.isDeleteModalComponentActive = true;
    this.yesCallbackForDeleteModalComponent = this.deleteClass.bind(this);
    this.noCallbackForDeleteModalComponent = () => {
      this.isDeleteModalComponentActive = false;
    };
  }

  removeFromClass() {
    if(!this.selectedPupil) return;
    this.pupilsService.removeFromClass(this.selectedPupil.userId)
      .then((pupil) => {
        this.selectClass(this.selectedClass.classId);
        this.pupilsService.fetchPupilsWithoutClass();
      })
      .catch((err) => {
        if(err.status === 500) {
          this.errorMessage = 'Извините, ошибка на сервере';
        } else {
          this.errorMessage = err._body;
        }
      });
  }

  openModalForAddClass() {
    this.currentClass = new Class(0);
    this.currentClass.letterMark = '';
    this.classModal.show();
  }

  closeClassModal() {
    this.classModal.hide();
    this.currentClass = null;
  }

  validateNewClassInfo() {
    const number = this.currentClass.number;
    const letterMark = this.currentClass.letterMark;
    if(!number) {
      this.validationError.number.status = true;
      this.validationError.number.message = 'Введите номер класса';
    } else if(!/[1-9]{1}|10|11/g.test(number)) {
      this.validationError.number.status = true;
      this.validationError.number.message = 'Классы с 1-го по 11-ый';
    } else {
      this.validationError.number.status = false;
    }
    if(letterMark && letterMark.length > 1) {
      this.validationError.letterMark.status = true;
      this.validationError.letterMark.message = 'Отметка класса состоит из одного знака';
    } else if(!/^[А-Яа-я']{0,1}$/g.test(letterMark)) {
      this.validationError.letterMark.status = true;
      this.validationError.letterMark.message = 'Отметка класса - это одна большая кириллическая буква либо штрих "\'"';
    } else {
      this.validationError.letterMark.status = false;
    }
  }

  isNewClassInfoValid() {
    return !this.validationError.number.status &&
        !this.validationError.letterMark.status;
  }

  onFormSubmit() {
    this.validateNewClassInfo();
    if(!this.isNewClassInfoValid()) return;
    this.classesService.createClass(this.currentClass)
      .then(clazz => {
        this.restoreState();
      })
      .catch((err) => {
        if(err.status === 500) {
          this.errorMessage = 'Извините, ошибка на сервере';
        } else {
          this.errorMessage = err._body;
        }
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
      })
      .catch((err) => {
        if(err.status === 500) {
          this.errorMessage = 'Извините, ошибка на сервере';
        } else {
          this.errorMessage = err._body;
        }
      });
  }

  getClassTeacherOfSelectedClass() {
    let teacher = this.teachers.find(teacher => teacher.classId == this.selectedClass.classId);
    return teacher ? this.teachersService.getTeacherFullName(teacher) : 'Не назначен'
  }

  validateClassForPupil() {
    const classForPupilId = this.classForPupil.classId;
    if(!classForPupilId) {
      this.validationError.classId.status = true;
      this.validationError.classId.message = 'Выберите класс';
    } else if(!this.classes.find(clazz => clazz.classId === +classForPupilId)) {
      this.validationError.classId.status = true;
      this.validationError.classId.message = 'Такого класса не существует';
    } else{
      this.validationError.classId.status = false;
    }
  }

  isClassForPupilValid() {
    return !this.validationError.classId.status;
  }

  onClassForPupilFormSubmit() {
    this.validateClassForPupil();
    if(!this.isClassForPupilValid()) return;
    if(!this.selectedPupilWithoutClass && !this.classForPupil.classId) return;
    this.pupilsService.movePupilToAnotherClass(this.selectedPupilWithoutClass.userId, +this.classForPupil.classId)
      .then(() => {
        this.restoreState();
        this.closeClassForPupilModal();
        this.closeClassForPupilModal();
      })
      .catch((err) => {
        if(err.status === 500) {
          this.errorMessage = 'Извините, ошибка на сервере';
        } else {
          this.errorMessage = err._body;
        }
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

  validateClassTeacher() {
    const classTeacherId = this.classTeacher;
    if(!classTeacherId || classTeacherId == -1) {
      this.validationError.classTeacher.status = true;
      this.validationError.classTeacher.message = 'Выберите классного руководителя';
    } else if(!this.teachers.find(teacher => teacher.userId === +classTeacherId)) {
      this.validationError.classTeacher.status = true;
      this.validationError.classTeacher.message = 'Нет такого учителя';
    } else{
      this.validationError.classTeacher.status = false;
    }
  }

  isClassTeacherValid() {
    return !this.validationError.classTeacher.status;
  }

  onClassTeacherFormSubmit() {
    this.validateClassTeacher();
    if(!this.isClassTeacherValid()) return;
    this.teachersService.setAsClassTeacher(this.classTeacher, this.selectedClass.classId)
      .then(() => {
        this.restoreState();
      })
      .catch((err) => {
        if(err.status === 500) {
          this.errorMessage = 'Извините, ошибка на сервере';
        } else {
          this.errorMessage = err._body;
        }
      });
  }

  closeClassTeacherModal() {
    this.classTeacherModal.hide();
    this.classTeacher = -1;
  }

  setActiveTab(index: number): void {
    this.tabs[index].active = true;
  }

  savePDF() {
    this.docsService.download(`/getClassPupilsList/${this.selectedClass.classId}/pdf`,
      'application/pdf');
  }

  saveXLS() {
    this.docsService.download(`/getClassPupilsList/${this.selectedClass.classId}/xls`,
      'application/xls');
  }

  saveCSV() {
    this.docsService.download(`/getClassPupilsList/${this.selectedClass.classId}/csv`,
      'text/csv');
  }
}
