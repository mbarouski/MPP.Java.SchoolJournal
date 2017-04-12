import {Component, AfterViewInit, OnInit} from '@angular/core';
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

}
