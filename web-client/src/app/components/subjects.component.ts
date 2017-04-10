import {Component, AfterViewInit, ViewChild} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {SubjectsService} from "../services/subjects.service";
import {Subject} from "../models/Subject";
import {ModalDirective} from "ng2-bootstrap";

@Component({
  moduleId: module.id,
  selector: 'subjects-component',
  templateUrl: './templates/subjects.component.html',
})
export class SubjectsComponent implements AfterViewInit{

  subjects = [];
  selectedSubject: any;
  currentSubject: Subject;
  isEdit: boolean;

  @ViewChild('subjectModal') public subjectModal: ModalDirective;

  constructor(private authService: AuthService,
              private router: Router,
              private subjectsService: SubjectsService) {
    subjectsService.subjectsSubject.subscribe(subjects => {
      this.subjects = subjects;
    });
  }

  ngAfterViewInit() {
    if(!this.authService.isLogged()) return this.router.navigate(['/login']);
    this.subjectsService.fetchSubjects();
  }

  selectSubject(id) {
    this.selectedSubject = this.subjects.find(subject => subject.subjectId == id);
  }

  closeSubjectModal() {
    this.subjectModal.hide();
  }

  openSubjectModalForEdit() {
    this.currentSubject = new Subject(this.selectedSubject.subjectId);
    this.currentSubject.name = this.selectedSubject.name;
    this.currentSubject.description = this.selectedSubject.description;
    this.isEdit = true;
    this.openSubjectModal();
  }

  openSubjectModalForAdd() {
    this.currentSubject = new Subject(0);
    this.isEdit = false;
    this.openSubjectModal();
  }

  openSubjectModal() {
    this.subjectModal.show();
  }

  restoreState() {
    this.closeSubjectModal();
    this.currentSubject = null;
    this.selectedSubject = null;
    this.subjectsService.fetchSubjects();
  }

  onSubjectFormSubmit() {
    if(this.isEdit) {
      this.subjectsService.updateSubject(this.currentSubject)
        .then(subject => {
          this.restoreState();
        });
    } else {
      this.subjectsService.addSubject(this.currentSubject)
        .then(subject => {
          this.restoreState();
        });
    }
  }

  deleteSubject() {
    this.subjectsService.deleteSubject(this.selectedSubject.subjectId)
      .then(() => {
        this.restoreState();
      });
  }

}
