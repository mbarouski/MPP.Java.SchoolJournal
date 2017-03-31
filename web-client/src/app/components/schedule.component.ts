import {Component, AfterViewInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {ScheduleService} from "../services/schedule.service";

@Component({
  moduleId: module.id,
  selector: 'schedule-component',
  templateUrl: './templates/schedule.component.html',
  styleUrls: ['./styles/schedule.component.css']
})
export class ScheduleComponent implements AfterViewInit{

  constructor(private authService: AuthService, private router: Router, private scheduleService: ScheduleService) {

  }

  ngAfterViewInit() {
    if(!this.authService.isLogged()) this.router.navigate(['/login']);
    this.loadSchedule();
  }

  loadSchedule() {
    switch(this.authService.role) {
      case 'teacher':
      case 'pupil':
        this.scheduleService.fetchSchedule(this.authService.role)
          .then(schedule => {
            debugger;
          });
    }
  }

}
