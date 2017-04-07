import {Component, AfterViewInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {ScheduleService} from "../services/schedule.service";
import {PupilsService} from "../services/pupils.service";
import DAY from "./schedule.constants";

declare let moment: any;

@Component({
  moduleId: module.id,
  selector: 'schedule-component',
  templateUrl: './templates/schedule.component.html',
  styleUrls: ['./styles/schedule.component.css']
})
export class ScheduleComponent implements AfterViewInit{

  schedule: any;
  role: string;
  day: any;

  constructor(private authService: AuthService,
              private router: Router,
              private scheduleService: ScheduleService,
              private pupilsService: PupilsService) {
    this.day = DAY;
  }

  ngAfterViewInit() {
    if(!this.authService.isLogged()) this.router.navigate(['/login']);
    this.loadSchedule();
  }

  loadSchedule() {
    switch(this.authService.role) {
      case 'pupil':
        this.pupilsService.fetchPupil(this.authService.user.userId)
          .then((pupil: any) => {
            this.scheduleService.fetchPupilSchedule(pupil.classId)
              .then(schedule => {
                this.schedule = this.divideScheduleOnDays(schedule);
                debugger;
              });
          });
        break;
      case 'teacher':
        this.scheduleService.fetchTeacherSchedule()
          .then(schedule => {
            debugger;
          });
        break;
      case 'director_of_studies':
      case 'director':
        this.scheduleService.fetchFullSchedule()
          .then(schedule => {
            debugger;
          });
        break;
    }
  }

  divideScheduleOnDays(schedule) {
    let result = {};
    schedule.forEach(subject => {
      if (!result[subject.dayOfWeek]) {
        result[subject.dayOfWeek] = [];
      }
      result[subject.dayOfWeek].push(subject);
    });
    return result;
  }

  decorateTime(strTime) {
    return moment(strTime, 'HH:mm:ss').format('HH:mm');
  }
}
