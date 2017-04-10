import {Component, AfterViewInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {MarksService} from "../services/marks.service";
import {PupilsService} from "../services/pupils.service";
import {TeachersService} from "../services/teachers.service";

@Component({
  moduleId: module.id,
  selector: 'schedule-component',
  templateUrl: './templates/marks.component.html',
  styleUrls: ['./styles/marks.component.css']
})
export class MarksComponent implements AfterViewInit{

  classes = [];
  subjects = [];
  teachers = [];
  pupilsWithoutClass = [];

  constructor(private authService: AuthService,
              private router: Router,
              private marksService: MarksService,
              private pupilsService: PupilsService,
              private teacherService: TeachersService) {

  }

  ngAfterViewInit() {
    if(!this.authService.isLogged()) return this.router.navigate(['/login']);
    if(this.authService.role !== 'director_of_Studies') return this.router.navigate(['/profile']);
    this.loadData();
  }

  loadData() {

  }

  fetchMarks() {
    switch(this.authService.role) {
      case 'pupil':
        this.pupilsService.fetchPupil(this.authService.user.userId)
          .then((pupil: any) => {
            this.marksService.fetchMarksForSubjectInClass(1, pupil.classId)
              .then(marks => {
                debugger;
              });
          });
        break;
    }
  }

}
