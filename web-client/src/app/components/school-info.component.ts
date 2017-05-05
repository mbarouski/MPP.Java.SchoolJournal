import {Component, ViewChild} from '@angular/core';
import {RolesService} from "../services/roles.service";
import {Subject, Observable} from 'rxjs';
import {TeachersService} from "../services/teachers.service";
import {SchoolInfoService} from "../services/school-info.service";
import {Term} from "../models/Term";
import {Lesson} from "../models/Lesson";
import {ModalDirective} from "ng2-bootstrap";
import {AuthService} from "../services/auth.service";

declare let moment: any;

@Component({
  moduleId: module.id,
  selector: 'school-info-component',
  templateUrl: './templates/school-info.component.html',
  styleUrls: ['./styles/school-info.component.css']
})
export class SchoolInfoComponent  {
  lessons = [];
  terms = [];
  currentTerm: any;
  selectedTerm: any;
  currentLesson: any;
  selectedLesson: any;
  startDate: any;
  startTime: any;
  endDate: any;
  endTime: any;

  validationError = Object.assign({}, ...[
    'startTime',
    'endTime',
    'startDate',
    'endDate'
  ].map((field) => {
    return {
      [field]: {
        status: false,
        message: '',
      },
    };
  }));

  errorMessage = '';

  @ViewChild('termModal') public termModal: ModalDirective;
  @ViewChild('lessonModal') public lessonModal: ModalDirective;

  constructor(private schoolInfoService: SchoolInfoService, private authService: AuthService){
    schoolInfoService.timesSubject.subscribe((times) => {
      this.lessons = times;
    });
    schoolInfoService.termsSubject.subscribe((terms) => {
      this.terms = terms;
    });
  }

  closeModal(modal) {
    modal.hide();
  }

  selectTerm(termId){
    this.selectedTerm = this.terms.find(term => term.termId == termId);
  }

  selectLesson(lessonId){
    this.selectedLesson = this.lessons.find(lesson => lesson.lessonId == lessonId);
  }

  openTermModal() {
    this.startDate = moment(this.selectedTerm.start, 'YYYY:MM:DD');
    this.endDate = moment(this.selectedTerm.end, 'YYYY:MM:DD');
    this.currentTerm = new Term(this.selectedTerm.termId);
    this.currentTerm.number = this.selectedTerm.number;
    this.currentTerm.start = this.selectedTerm.start;
    this.currentTerm.end = this.selectedTerm.end;
    this.termModal.show();
  }

  openLessonModal() {
    this.startTime = moment(this.selectedLesson.startTime, 'HH:mm:ss');
    this.endTime = moment(this.selectedLesson.endTime, 'HH:mm:ss');
    this.currentLesson = new Lesson(this.selectedLesson.lessonId);
    this.currentLesson.number = this.selectedLesson.number;
    this.currentLesson.start = this.selectedLesson.startTime;
    this.currentLesson.end = this.selectedLesson.endTime;
    this.lessonModal.show();
  }

  submitForTermForm() {
    this.currentTerm.start = moment(this.startDate).format('YYYY-MM-DD');
    this.currentTerm.end = moment(this.endDate).format('YYYY-MM-DD');
    this.validateTerm();
    if(!this.isTermValid()) return;
    this.schoolInfoService.updateTerm(this.currentTerm)
      .then(term => {
        this.schoolInfoService.fetchTerms();
        this.closeModal(this.termModal);
      })
      .catch((err) => {
        if(err.status === 500) {
          this.errorMessage = 'Извините, ошибка на сервере';
        } else {
          this.errorMessage = err._body;
        }
      });
  }

  isAvailable() {
    return ['director', 'director_of_studies'].includes(this.authService.role);
  }

  validateTerm() {
    const startDate = moment(this.startDate);
    const endDate = moment(this.endDate);
    if (startDate.isAfter(endDate)) {
      this.errorMessage = 'Дата конца меньше даты начала';
    } else {
      this.errorMessage = '';
    }
  }

  isTermValid() {
    return !this.validationError.startDate.status &&
      !this.validationError.endDate.status &&
      !this.errorMessage;
  }

  validateLesson() {
    const startTime = moment(this.startTime);
    const endTime = moment(this.endTime);
    if (startTime.isAfter(endTime)) {
      this.errorMessage = 'Время конца меньше времени начала';
    } else {
      this.errorMessage = '';
    }
  }

  isLessonValid() {
    return !this.validationError.startTime.status &&
        !this.validationError.endTime.status &&
        !this.errorMessage;
  }

  submitForLessonForm() {
    this.currentLesson.startTime = moment(this.startTime).format('HH:mm:ss');
    this.currentLesson.endTime = moment(this.endTime).format('HH:mm:ss');
    this.validateLesson();
    if(!this.isLessonValid()) return;
    this.schoolInfoService.updateLesson(this.currentLesson)
      .then(lesson => {
        this.schoolInfoService.fetchLessonTimes();
        this.closeModal(this.lessonModal);
      })
      .catch((err) => {
        if(err.status === 500) {
          this.errorMessage = 'Извините, ошибка на сервере';
        } else {
          this.errorMessage = err._body;
        }
      });
  }
}
