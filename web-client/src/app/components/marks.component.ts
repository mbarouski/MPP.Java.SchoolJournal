import {Component, AfterViewInit, OnInit, ViewChild} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router, Params, ActivatedRoute} from "@angular/router";
import {MarksService} from "../services/marks.service";
import {PupilsService} from "../services/pupils.service";
import {TeachersService} from "../services/teachers.service";
import {ReplaySubject} from "rxjs";
import {SubjectsService} from "../services/subjects.service";
import {ScheduleService} from "../services/schedule.service";
import {Mark} from "../models/Mark";
import {SchoolInfoService} from "../services/school-info.service";
import MARK_TYPES from "./constants/marks.constants";
import {ModalDirective} from "ng2-bootstrap";

declare let moment: any;

@Component({
  moduleId: module.id,
  selector: 'schedule-component',
  templateUrl: './templates/marks.component.html',
  styleUrls: ['./styles/marks.component.css']
})
export class MarksComponent implements OnInit{

  marks = [];
  subject: any;
  subjects = [];
  pupils = [];
  currentMark: Mark;
  times = [];
  lessons = [];
  term: any;
  currentTerm: number;
  markTypes = MARK_TYPES;
  selectedDate: any;

  datePickerOptions = {
    dateFormat: 'yyyy-mm-dd',
  };

  @ViewChild('markModal') public markModal: ModalDirective;

  constructor(private authService: AuthService,
              private router: Router,
              private marksService: MarksService,
              private pupilsService: PupilsService,
              private scheduleService: ScheduleService,
              private activatedRoute: ActivatedRoute,
              private schoolInfoService: SchoolInfoService) {
    marksService.marksSubject.subscribe(marks => {
      this.marks = marks;
      scheduleService.getSubjectsForTeacherClassSubject(this.subject.teacher.userId,
        this.subject.clazz.classId,
        this.subject.subject.subjectId)
        .then((subjects: any) => {
          this.subjects = subjects;
          this.generateLessons();
        });
    });
    schoolInfoService.timesSubject.subscribe(times => {
      this.times = times;
    });
    schoolInfoService.termSubject.subscribe(term => {
      this.term = term;
      this.currentTerm = term.number;
    });
  }

  generateLessons() {
    let lessonDates = [];
    let date = moment(this.term.start);
    let endDate = moment(this.term.end);
    let days = this.subjects.map(subject => subject.dayOfWeek);
    while(!date.isAfter(endDate)) {
      let day = date.format('ddd').toLowerCase();
      if(days.includes(day)) {
        lessonDates.push(date.format('DD.MM'));
      }
      date = date.add(1, 'days');
    }
    this.lessons = lessonDates;
  }

  ngOnInit() {
    this.activatedRoute.params.subscribe((params: Params) => {
      if(params['id']) {
        this.scheduleService.fetchSubject(params['id'])
          .then((subject: any) => {
            this.subject = subject;
            this.pupilsService.fetchPupilsByClass(subject.clazz.classId)
              .then((pupils: any) => {
                this.pupils = pupils;
              });
            this.marksService.fetchMarksForSubjectInClass(subject.subject.subjectId, subject.clazz.classId);
          });
      }
    });
  }

  getMarkForLessonAndPupil(date, pupilId) {
    let mark = this.marks.find(mark => {
      return mark.pupil.userId == pupilId &&
          moment(mark.date).format('DD.MM') === date;
    });
    return mark ? mark.value : '';
  }

  openMarkModal() {
    this.markModal.show();
  }

  getPupilByFullName(fullname) {
    return this.pupils.find(pupil => {
      return this.pupilsService.getPupilFullName(pupil) === fullname;
    });
  }

  cellForEdit: any;

  setCurrentMark(mark, event) {
    this.cellForEdit = $(event.currentTarget);
    if(!mark) {
      mark = new Mark(0);
      mark.subjectId = this.subject.subject.subjectId;
      mark.teacherId = this.subject.teacher.userId;
      mark.pupilId = this.getPupilByFullName($(event.currentTarget.parentElement.parentElement).children()[0].innerText.trim()).userId;
      debugger;
    }
    this.currentMark = mark;
    this.openMarkModal();
  }

  clearCurrentMark() {
    // this.currentMark = null;
  }

  submitMarkForm() {
    this.currentMark.date = moment(this.selectedDate).format('YYYY-MM-DD');
    this.marksService.setMark(this.currentMark)
      .then((mark: any) => {
        this.cellForEdit.text(mark.value);
      });
  }

}
