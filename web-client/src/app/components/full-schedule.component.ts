import {Component, AfterViewInit, ViewChild} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {ScheduleService} from "../services/schedule.service";
import DAY from "./constants/schedule.constants";
import {SchoolInfoService} from "../services/school-info.service";
import * as _ from "lodash";
import {ContextMenuComponent, ContextMenuService} from "angular2-contextmenu";
import {ModalDirective} from "ng2-bootstrap";

declare let moment: any;
declare let $: JQueryStatic;

@Component({
  moduleId: module.id,
  selector: 'full-schedule-component',
  templateUrl: './templates/full-schedule.component.html',
  styleUrls: ['./styles/full-schedule.component.css']
})

export class FullScheduleComponent implements AfterViewInit{

  schedule: any;
  dividedOnTeachersSchedule: any;
  times = [];

  @ViewChild(ContextMenuComponent) public addMenu: ContextMenuComponent;
  @ViewChild(ContextMenuComponent) public deleteMenu: ContextMenuComponent;
  @ViewChild('subjectModal') public subjectModal: ModalDirective;

  constructor(private authService: AuthService,
              private router: Router,
              private scheduleService: ScheduleService,
              private schoolInfoService: SchoolInfoService,
              private contextMenuService: ContextMenuService) {
    this.times = schoolInfoService.timesForSubjects;
  }

  ngAfterViewInit() {
    if(!this.authService.isLogged()) this.router.navigate(['/login']);
    this.loadSchedule();
  }

  loadSchedule() {
    this.scheduleService.fetchFullSchedule()
      .then(schedule => {
        this.schedule = schedule;
        this.dividedOnTeachersSchedule = this.divideOnTeachersAndDays(schedule);
      });
  }

  divideOnTeachersAndDays(schedule) {
    let result = {};
    schedule.forEach(subject => {
      const fullName = `${subject.teacher.firstName} ${subject.teacher.pathronymic} ${subject.teacher.lastName}`;
      if(!result[fullName]) {
        result[fullName] = {};
      }
      if(!result[fullName][subject.dayOfWeek]) {
        result[fullName][subject.dayOfWeek] = {};
      }
      result[fullName][subject.dayOfWeek][this.decorateTime(subject.beginTime)] = (subject);
    });
    return result;
  }

  decorateTime(strTime) {
    return moment(strTime, 'HH:mm:ss').format('HH:mm');
  }

  getSubjectForDayAndTime(subjects, dayTime) {
    return _.get(subjects, `${dayTime.day.short}.${dayTime.time}`, undefined);
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

  addSubject() {

  }

  openModalForAddSubject(event) {
    event.event.preventDefault();
    this.subjectModal.show();
  }

  openModalForEditSubject(event) {
    event.event.preventDefault();
    debugger;
  }

}
