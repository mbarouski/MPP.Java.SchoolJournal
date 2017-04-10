import {Component, AfterViewInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {MarksService} from "../services/marks.service";
import {PupilsService} from "../services/pupils.service";
import {TeachersService} from "../services/teachers.service";
import {ClassesService} from "../services/classes.service";
import {SubjectsService} from "../services/subjects.service";

@Component({
  moduleId: module.id,
  selector: 'director-of-studies-panel',
  templateUrl: './templates/director-of-studies-panel.component.html',
  // styleUrls: ['./styles/director-of-studies-panel.component.css']
})
export class DirectorOfStudiesPanelComponent implements AfterViewInit{

  classes = [];
  subjects = [];
  teachers = [];
  pupilsWithoutClass = [];

  constructor(private authService: AuthService,
              private router: Router,
              private pupilsService: PupilsService,
              private teachersService: TeachersService,
              private classesService: ClassesService,
              private subjectsService: SubjectsService) {

  }

  ngAfterViewInit() {
    if(!this.authService.isLogged()) return this.router.navigate(['/login']);
    if(this.authService.role !== 'director_of_Studies') return this.router.navigate(['/profile']);
    this.loadData();
  }

  loadData() {

  }

}
