import {Component, AfterViewInit, ViewChild} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {ScheduleService} from "../services/schedule.service";
import DAY from "./constants/schedule.constants";
import {SchoolInfoService} from "../services/school-info.service";
import * as _ from "lodash";
import {ContextMenuComponent, ContextMenuService} from "angular2-contextmenu";

declare let moment: any;
declare let $: JQueryStatic;

@Component({
  providers: [ ContextMenuService ],
  moduleId: module.id,
  selector: 'full-schedule-component',
  templateUrl: './templates/full-schedule.component.html',
  styleUrls: ['./styles/full-schedule.component.css']
})

export class FullScheduleComponent implements AfterViewInit{

  items = [
    'ggg','gg'
  ];

  schedule: any;
  dividedOnTeachersSchedule: any;
  times = [];

  @ViewChild(ContextMenuComponent) public editMenu: ContextMenuComponent;

  constructor(private authService: AuthService,
              private router: Router,
              private scheduleService: ScheduleService,
              private schoolInfoService: SchoolInfoService) {
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

}
