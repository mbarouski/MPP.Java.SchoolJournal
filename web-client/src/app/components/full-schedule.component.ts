import {Component, AfterViewInit, ViewChild} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {ScheduleService} from "../services/schedule.service";
import DAY from "./constants/schedule.constants";
import {SchoolInfoService} from "../services/school-info.service";
import * as _ from "lodash";
import {ContextMenuComponent, ContextMenuService} from "angular2-contextmenu";
import {ModalDirective} from "ng2-bootstrap";
import {TeachersService} from "../services/teachers.service";
import {SubjectInSchedule} from "../models/SubjectInSchedule";
import DAYS from "./constants/schedule.constants";
import {ClassesService} from "../services/classes.service";
import {SubjectsService} from "../services/subjects.service";

declare let moment: any;
declare let $: JQueryStatic;

@Component({
  moduleId: module.id,
  selector: 'full-schedule-component',
  templateUrl: './templates/full-schedule.component.html',
  styleUrls: ['./styles/full-schedule.component.css']
})

export class FullScheduleComponent implements AfterViewInit{
  schedule = [];
  times = [];
  days = DAYS;
  currentSubject: SubjectInSchedule;
  teachers = [];
  classes = [];
  subjects = [];
  isEdit: boolean;

  @ViewChild(ContextMenuComponent) public addMenu: ContextMenuComponent;
  @ViewChild(ContextMenuComponent) public deleteMenu: ContextMenuComponent;
  @ViewChild('subjectModal') public subjectModal: ModalDirective;

  constructor(private authService: AuthService,
              private router: Router,
              private scheduleService: ScheduleService,
              private schoolInfoService: SchoolInfoService,
              private contextMenuService: ContextMenuService,
              private teachersService: TeachersService,
              private classesService: ClassesService,
              private subjectsService: SubjectsService) {
    this.times = schoolInfoService.timesForSubjects;
    teachersService.teachersSubject.subscribe(teachers => {
      this.teachers = teachers;
    });
    classesService.classesSubject.subscribe(classes => {
      this.classes = classes;
    });
    subjectsService.subjectsSubject.subscribe(subjects => {
      this.subjects = subjects;
    });
    scheduleService.scheduleSubject.subscribe(schedule => {
      this.schedule = schedule;
    });
  }

  ngAfterViewInit() {
    if(!this.authService.isLogged()) this.router.navigate(['/login']);
    this.loadSchedule();
  }

  loadSchedule() {
    this.scheduleService.fetchFullSchedule();
  }

  decorateTime(strTime) {
    return moment(strTime, 'HH:mm:ss').format('HH:mm');
  }

  subjectForTeacherAndDayAndTime;

  getSubjectForTeacherAndDayAndTime(teacher, dayTime) {
    let filtered = this.schedule.filter(subject => {

      return ((subject.teacher.userId === teacher.userId)
        && (subject.dayOfWeek === dayTime.day.short)
        && (this.decorateTime(subject.beginTime) === dayTime.time));
    });

    this.subjectForTeacherAndDayAndTime = filtered.length ? filtered[0] : undefined;
    return this.subjectForTeacherAndDayAndTime;
  }

  cellForEdit: any;

  openContextMenuForEdit(event, subject, contextMenu) {
    this.cellForEdit = $(event.currentTarget);
    this.contextMenuService.show.next({
      contextMenu,
      event,
      item: subject ? subject : {}
    });
    event.preventDefault();
  }

  deleteSubject(event) {
    event.event.preventDefault();
    this.scheduleService.deleteSubjectInSchedule(event.item.subectInScheduleId)
      .then(() => {
        this.cellForEdit.removeClass('htooltip');
        this.cellForEdit.empty();
      });
  }

  addOrSaveSubject(subject: SubjectInSchedule, serviceMethod) {
    subject.beginTime = `${subject.time}:00`;
    serviceMethod(this.currentSubject)
      .then((data) => {
        this.closeSubjectModal();
        this.loadSchedule();
      });
  }

  addSubject(subject: SubjectInSchedule) {
    this.addOrSaveSubject(subject, this.scheduleService.addSubject.bind(this.scheduleService));
  }

  saveSubject(subject: SubjectInSchedule) {
    this.addOrSaveSubject(subject, this.scheduleService.updateSubject.bind(this.scheduleService));
  }

  closeSubjectModal() {
    this.currentSubject = null;
    this.subjectModal.hide();
  }

  openModalForAddSubject(event) {
    event.event.preventDefault();
    this.isEdit = false;
    let teacherName = $(this.cellForEdit.parent().children()[0]).text();
    let subject = new SubjectInSchedule(0);
    subject.teacherId = this.teachersService.getTeacherByFullName(teacherName).userId;
    subject.dayOfWeek = this.days[Math.floor((this.cellForEdit.index() - 1) / 8)].short;
    subject.time = this.times[(this.cellForEdit.index() - 1) % 8];
    this.currentSubject = subject;
    this.subjectModal.show();
  }

  openModalForEditSubject(event) {
    event.event.preventDefault();
    this.isEdit = true;
    let item = event.item;
    let subject = new SubjectInSchedule(item.subectInScheduleId);
    subject.dayOfWeek = item.dayOfWeek;
    subject.time = this.decorateTime(item.beginTime);
    subject.place = item.place;
    subject.teacherId = item.teacher.userId;
    subject.subjectId = item.subject.subjectId;
    subject.clazzId = item.clazz.classId;
    this.currentSubject = subject;
    this.subjectModal.show();
  }

  onSubmit() {
    if (this.isEdit) {
      this.saveSubject(this.currentSubject);
    } else {
      this.addSubject(this.currentSubject);
    }
  }

  createLinkToMarks(subject) {
    return `/marks/${subject.subectInScheduleId}`;
  }

}
