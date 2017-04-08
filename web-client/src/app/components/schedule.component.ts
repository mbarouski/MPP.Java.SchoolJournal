import {Component, AfterViewInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {ScheduleService} from "../services/schedule.service";
import {PupilsService} from "../services/pupils.service";
import DAYS from "./constants/schedule.constants";

declare let moment: any;

@Component({
  moduleId: module.id,
  selector: 'schedule-component',
  templateUrl: './templates/schedule.component.html',
  styleUrls: ['./styles/schedule.component.css']
})

export class ScheduleComponent implements AfterViewInit{

  items = [
    'ggg', 'gg', 'f'
  ];

  schedule: any;
  days: any;

  constructor(private authService: AuthService,
              private router: Router,
              private scheduleService: ScheduleService,
              private pupilsService: PupilsService) {
    this.days = DAYS;
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
              });
          });
        break;
      case 'teacher':
      case 'director_of_studies':
      case 'director':
        this.scheduleService.fetchTeacherSchedule()
          .then(schedule => {
            debugger;
          });
        break;
      default:
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
