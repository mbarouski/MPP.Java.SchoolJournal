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

  validationError = {
    name: {
      status: false,
      message: '',
    },
    description: {
      status: false,
      message: '',
    },
  };

  errorMessage = '';

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

  validateSubject() {
    const name = this.currentSubject.name;
    const description = this.currentSubject.description;
    if(!name || (name && name.length < 2)) {
      this.validationError.name.status = true;
      this.validationError.name.message = 'Название предмета не менее двух символов';
    } else {
      this.validationError.name.status = false;
    }
    if(!description) {
      this.validationError.description.status = true;
      this.validationError.description.message = 'Введите описание';
    } else {
      this.validationError.description.status = false;
    }
  }

  isSubjectValid() {
    return !this.validationError.name.status &&
        !this.validationError.description.status;
  }

  onSubjectFormSubmit() {
    this.validateSubject();
    if(!this.isSubjectValid()) return;
    if(this.isEdit) {
      this.subjectsService.updateSubject(this.currentSubject)
        .then(subject => {
          this.restoreState();
        })
        .catch((err) => {
          if(err.status === 500) {
            this.errorMessage = 'Извините, ошибка на сервере';
          } else {
            this.errorMessage = err;
          }
        });
    } else {
      this.subjectsService.addSubject(this.currentSubject)
        .then(subject => {
          this.restoreState();
        })
        .catch((err) => {
          if(err.status === 500) {
            this.errorMessage = 'Извините, ошибка на сервере';
          } else {
            this.errorMessage = err;
          }
        });
    }
  }

  deleteSubject() {
    this.subjectsService.deleteSubject(this.selectedSubject.subjectId)
      .then(() => {
        this.restoreState();
      })
      .catch((err) => {
        if(err.status === 500) {
          this.errorMessage = 'Извините, ошибка на сервере';
        } else {
          this.errorMessage = err;
        }
      });
  }

}
