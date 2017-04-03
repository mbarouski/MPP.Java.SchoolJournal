import {Component, AfterViewInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {ScheduleService} from "../services/schedule.service";
import {PupilsService} from "../services/pupils.service";

@Component({
  moduleId: module.id,
  selector: 'schedule-component',
  templateUrl: './templates/schedule.component.html',
  styleUrls: ['./styles/schedule.component.css']
})
export class ScheduleComponent implements AfterViewInit{

  constructor(private authService: AuthService,
              private router: Router,
              private scheduleService: ScheduleService,
              private pupilsService: PupilsService) {

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

}
