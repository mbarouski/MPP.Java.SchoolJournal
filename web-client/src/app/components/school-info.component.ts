import {Component, ViewChild} from '@angular/core';
import {RolesService} from "../services/roles.service";
import {Subject, Observable} from 'rxjs';
import {TeachersService} from "../services/teachers.service";
import {SchoolInfoService} from "../services/school-info.service";
import {Term} from "../models/Term";
import {Lesson} from "../models/Lesson";
import {ModalDirective} from "ng2-bootstrap";

declare let moment: any;

@Component({
  moduleId: module.id,
  selector: 'school-info-component',
  templateUrl: './templates/school-info.component.html'
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

  @ViewChild('termModal') public termModal: ModalDirective;
  @ViewChild('lessonModal') public lessonModal: ModalDirective;

  constructor(private schoolInfoService: SchoolInfoService){
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
    this.currentTerm = new Term(this.selectedTerm.termId);
    this.currentTerm.number = this.selectedTerm.number;
    this.currentTerm.start = this.selectedTerm.start;
    this.currentTerm.end = this.selectedTerm.end;
    this.termModal.show();
  }

  openLessonModal() {
    this.currentLesson = new Lesson(this.selectedLesson.lessonId);
    this.currentLesson.number = this.selectedLesson.number;
    this.currentLesson.start = this.selectedLesson.startTime;
    this.currentLesson.end = this.selectedLesson.endTime;
    this.lessonModal.show();
  }

  submitForTermForm() {
    this.currentTerm.start = moment(this.startDate).format('YYYY-MM-DD');
    this.currentTerm.end = moment(this.endDate).format('YYYY-MM-DD');
    this.schoolInfoService.updateTerm(this.currentTerm)
      .then(term => {
        this.schoolInfoService.fetchTerms();
        this.closeModal(this.termModal);
      });
  }

  submitForLessonForm() {
    this.currentLesson.startTime = moment(this.startTime).format('HH:mm:ss');
    this.currentLesson.endTime = moment(this.endTime).format('HH:mm:ss');
    this.schoolInfoService.updateLesson(this.currentLesson)
      .then(lesson => {
        this.schoolInfoService.fetchLessonTimes();
        this.closeModal(this.lessonModal);
      });
  }
}
