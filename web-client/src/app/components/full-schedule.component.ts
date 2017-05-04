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

  validationError = Object.assign({}, ...[
    'subject',
    'class',
    'teacher',
    'place',
    'day',
    'time'
  ].map((field) => {
    return {
      [field]: {
        status: false,
        message: '',
      },
    };
  }));

  errorMessage = '';

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
    // this.times = schoolInfoService.timesForSubjects;
    schoolInfoService.timesSubject.subscribe(times => this.times = times.map(time => this.decorateTime(time.startTime)));
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

  deleteSubject(subectInScheduleId) {
    this.scheduleService.deleteSubjectInSchedule(subectInScheduleId)
      .then(() => {
        // this.cellForEdit.removeClass('htooltip');
        // this.cellForEdit.empty();
        this.subjects = this.subjects.filter(s => s.subectInSchedule !== subectInScheduleId);
        this.times = this.times;
      })
      .catch((err) => {
        if(err.status === 500) {
          this.errorMessage = 'Извините, ошибка на сервере';
        } else {
          this.errorMessage = err._body;
        }
      });
  }

  addOrSaveSubject(subject: SubjectInSchedule, serviceMethod) {
    subject.beginTime = `${subject.time}:00`;
    serviceMethod(this.currentSubject)
      .then((data) => {
        this.closeSubjectModal();
        this.loadSchedule();
      })
      .catch((err) => {
        if(err.status === 500) {
          this.errorMessage = 'Извините, ошибка на сервере';
        } else {
          this.errorMessage = err._body;
        }
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
    subject.dayOfWeek = this.days[Math.floor((this.cellForEdit.index() - 1) / this.times.length)].short;
    subject.time = this.times[(this.cellForEdit.index() - 1) % this.times.length];
    this.currentSubject = subject;
    this.subjectModal.show();
  }

  openModalForEditSubject(item) {
    this.isEdit = true;
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

  yesCallbackForDeleteModalComponent = () => {};
  noCallbackForDeleteModalComponent = () => {};
  isDeleteModalComponentActive = false;

  showModalForDeleteSubject(subjectInScheduleId) {
    this.isDeleteModalComponentActive = true;
    this.yesCallbackForDeleteModalComponent = this.deleteSubject.bind(this, subjectInScheduleId);
    this.noCallbackForDeleteModalComponent = () => {
      this.isDeleteModalComponentActive = false;
    };
  }

  validateSubject() {
    this.validateTeacher();
    this.validateSubjectItem();
    this.validateClass();
    this.validatePlace();
    this.validateDay();
    this.validateTime();
  }

  validatePlace() {
    const place = this.currentSubject.place;
    if(!place) {
      this.validationError.place.status = true;
      this.validationError.place.message = 'Укажите место проведения урока';
    } else {
      this.validationError.place.status = false;
    }
  }

  validateTime() {
    const time = this.currentSubject.time;
    if(!time) {
      this.validationError.time.status = true;
      this.validationError.time.message = 'Укажите время начала занятия';
    } else if(!this.times.includes(time)) {
      this.validationError.time.status = true;
      this.validationError.time.message = 'В данное время не начинается урок';
    } else {
      this.validationError.time.status = false;
    }
  }

  validateDay() {
    const day = this.currentSubject.dayOfWeek;
    if(!day) {
      this.validationError.day.status = true;
      this.validationError.day.message = 'Укажите день недели';
    } else if(!this.days.find(d => d.short === day)) {
      this.validationError.day.status = true;
      this.validationError.day.message = 'В данный день не проводятся уроки';
    } else {
      this.validationError.day.status = false;
    }
  }

  validateTeacher() {
    const teacherId = this.currentSubject.teacherId;
    if(!teacherId) {
      this.validationError.teacher.status = true;
      this.validationError.teacher.message = 'Укажите учителя';
    } else if(!this.teachers.find(t => t.userId === +teacherId)) {
      this.validationError.teacher.status = true;
      this.validationError.teacher.message = 'Учитель не найден';
    } else {
      this.validationError.teacher.status = false;
    }
  }

  validateSubjectItem() {
    const subjectId = this.currentSubject.subjectId;
    if(!subjectId) {
      this.validationError.subject.status = true;
      this.validationError.subject.message = 'Укажите предмет';
    } else if(!this.subjects.find(s => s.subjectId === +subjectId)) {
      this.validationError.subject.status = true;
      this.validationError.subject.message = 'Предмет не найден';
    } else {
      this.validationError.subject.status = false;
    }
  }

  validateClass() {
    const classId = this.currentSubject.clazzId;
    if(!classId) {
      this.validationError.class.status = true;
      this.validationError.class.message = 'Укажите класс';
    } else if(!this.classes.find(c => c.classId === +classId)) {
      this.validationError.class.status = true;
      this.validationError.class.message = 'Класс не найден';
    } else {
      this.validationError.class.status = false;
    }
  }

  isSubjectValid() {
    return !this.validationError.day.status &&
      !this.validationError.subject.status &&
      !this.validationError.class.status &&
      !this.validationError.teacher.status &&
      !this.validationError.place.status &&
      !this.validationError.time.status;
  }

  onSubmit() {
    this.validateSubject();
    if(!this.isSubjectValid()) return;

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
