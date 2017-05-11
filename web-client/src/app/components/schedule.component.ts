import {Component, AfterViewInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {ScheduleService} from "../services/schedule.service";
import {PupilsService} from "../services/pupils.service";
import DAYS from "./constants/schedule.constants";
import {DocsService} from "../services/docs.service";

declare let moment: any;

@Component({
  moduleId: module.id,
  selector: 'schedule-component',
  templateUrl: './templates/schedule.component.html',
  styleUrls: ['./styles/schedule.component.css']
})

export class ScheduleComponent implements AfterViewInit{

  schedule: any;
  days: any;

  errorMessage = '';

  constructor(private authService: AuthService,
              private router: Router,
              private scheduleService: ScheduleService,
              private pupilsService: PupilsService,
              private docsService: DocsService) {
    this.days = DAYS;
  }

  ngAfterViewInit() {
    if(!this.authService.isLogged()) this.router.navigate(['/login']);
    this.loadSchedule();
    this.errorMessage = '';
  }

  loadSchedule() {
    switch(this.authService.role) {
      case 'pupil':
        this.pupilsService.fetchPupil(this.authService.user.userId)
          .then((pupil: any) => {
            if(!pupil.classId) return this.errorMessage = 'Вы не состоите в классе';
            this.scheduleService.fetchPupilSchedule(pupil.classId)
              .then(schedule => {
                this.schedule = this.divideScheduleOnDays(schedule);
              })
              .catch((err) => {
                if(err.status === 500) {
                  this.errorMessage = 'Извините, ошибка на сервере';
                } else {
                  this.errorMessage = err._body;
                }
              });
          })
          .catch((err) => {
            if(err.status === 500) {
              this.errorMessage = 'Извините, ошибка на сервере';
            } else {
              this.errorMessage = err._body;
            }
          });
        break;
      case 'teacher':
      case 'director_of_studies':
      case 'director':
        this.scheduleService.fetchTeacherSchedule()
          .then(schedule => {
            this.schedule = this.divideScheduleOnDays(schedule);
          })
          .catch((err) => {
            if(err.status === 500) {
              this.errorMessage = 'Извините, ошибка на сервере';
            } else {
              this.errorMessage = err._body;
            }
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


  isPupilRole() {
    return this.authService.role === 'pupil';
  }

  savePDF() {
    this.docsService.download(`/${ this.isPupilRole() ? 'getClassSchedule' : 'getTeacherSchedule' }/${this.authService.user.userId}/pdf`,
      'application/pdf');
  }

  saveXLS() {
    this.docsService.download(`/${ this.isPupilRole() ? 'getClassSchedule' : 'getTeacherSchedule' }/${this.authService.user.userId}/xls`,
      'application/xls');
  }

  saveCSV() {
    this.docsService.download(`/${ this.isPupilRole() ? 'getClassSchedule' : 'getTeacherSchedule' }/${this.authService.user.userId}/csv`,
      'text/csv');
  }
}
