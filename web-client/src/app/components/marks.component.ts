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
import {ContextMenuComponent, ContextMenuService} from "angular2-contextmenu";

declare let moment: any;

@Component({
  moduleId: module.id,
  selector: 'schedule-component',
  templateUrl: './templates/marks.component.html',
  styleUrls: ['./styles/marks.component.css']
})
export class MarksComponent implements OnInit, AfterViewInit{
  marks = [];
  subject: any = null;
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
  @ViewChild(ContextMenuComponent) public deleteMenu: ContextMenuComponent;

  constructor(private authService: AuthService,
              private router: Router,
              private marksService: MarksService,
              private pupilsService: PupilsService,
              private scheduleService: ScheduleService,
              private activatedRoute: ActivatedRoute,
              private schoolInfoService: SchoolInfoService,
              private contextMenuService: ContextMenuService) {
    marksService.marksSubject.subscribe(marks => {
      this.marks = marks;
      if(!this.subject) {
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
        return;
      }
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

  ngAfterViewInit() {
    // this.activatedRoute.params.subscribe((params: Params) => {
    //   if(params['id']) {
    //     debugger;
    //     this.scheduleService.fetchSubject(params['id'])
    //       .then((subject: any) => {
    //         this.subject = subject;
    //         this.pupilsService.fetchPupilsByClass(subject.clazz.classId)
    //           .then((pupils: any) => {
    //             this.pupils = pupils;
    //           });
    //         this.marksService.fetchMarksForSubjectInClass(subject.subject.subjectId, subject.clazz.classId);
    //       });
    //   }
    // });
  }

  getMarkForLessonAndPupil(date, pupilId) {
    let mark = this.marks.find(mark => {
      return mark.pupil.userId == pupilId &&
          moment(mark.date).format('DD.MM') === date;
    });
    mark = mark ? mark : '';
    return mark;
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
    if(!['director', 'director_of_studies', 'teacher'].includes(this.authService.role)) return;
    if(mark) return;
    this.cellForEdit = $(event.currentTarget);
    let currentMark = new Mark(0);
    currentMark.subjectId = this.subject.subject.subjectId;
    currentMark.teacherId = this.subject.teacher.userId;
    currentMark.pupilId = this.getPupilByFullName($(event.currentTarget.parentElement.parentElement).children()[0].innerText.trim()).userId;

    this.currentMark = currentMark;
    this.openMarkModal();
  }

  submitMarkForm() {
    this.currentMark.date = moment(this.selectedDate).format('YYYY-MM-DD');
    this.marksService.setMark(this.currentMark)
      .then((mark: any) => {
        this.closeMarkModal();
        this.currentMark = null;
        this.cellForEdit.text(mark.value);
        this.cellForEdit.addClass(this.generateClass(mark));
      });
  }

  generateClass(mark) {
    if(!mark || mark.type === 'simple') return '';
    return `${mark.type}-mark`;
  }

  decorateMark(mark) {
    if(!mark) return '';
    switch(mark.type) {
      case 'apsent':
        return 'Н';
      default:
        return mark.value;
    }
  }

  closeMarkModal() {
    this.markModal.hide();
  }

  openContextMenu(event, markId) {
    if(!markId) return;
    this.cellForEdit = $(event.currentTarget);
    let mark = this.marks.find(mark => mark.markId == markId);
    this.contextMenuService.show.next({
      contextMenu: this.deleteMenu,
      event,
      item: mark ? mark : {}
    });
    event.preventDefault();
  }

  deleteMark(event) {
    this.marksService.deleteMark(event.item.markId)
      .then(() => {
        this.cellForEdit.text('');
      });
  }

}
