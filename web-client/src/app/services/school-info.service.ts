import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http, Headers, RequestOptions, URLSearchParams} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";
import {AuthService} from "./auth.service";
import {HttpUtil} from "./http.util";
import {Term} from "../models/Term";
import {Lesson} from "../models/Lesson";

@Injectable()
export class SchoolInfoService {

  timesSubject: ReplaySubject<any> = new ReplaySubject(1);
  termsSubject: ReplaySubject<any> = new ReplaySubject(1);
  termSubject: ReplaySubject<any> = new ReplaySubject(1);

  constructor(@Inject(APP_CONFIG) private config: any, private http: Http, private authService: AuthService){
    this.fetchCurrentTerm();
    this.fetchLessonTimes();
    this.fetchTerms();
  }

  timesForSubjects = [
    '08:00',
    '09:00',
    '10:00',
    '11:00',
    '12:00',
    '13:00',
    '14:00',
    '15:00'
  ];

  currentTerm = {
    number: 1
  };

  fetchLessonTimes() {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.get(`${this.config.apiEndpoint}/lessons`, {search: params})
        .map(res => res.json())
        .subscribe((lessons) => {
          this.timesSubject.next(lessons);
          resolve(lessons);
        });
    });
  }

  fetchCurrentTerm() {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.get(`${this.config.apiEndpoint}/terms/current`, {search: params})
        .map(res => res.json())
        .subscribe((term) => {
          this.termSubject.next(term);
          resolve(term);
        });
    });
  }

  fetchTerms() {
    let params = new URLSearchParams();
    params.append('token', this.authService.token);
    this.http.get(`${this.config.apiEndpoint}/terms`, {search: params})
      .map(res => res.json())
      .subscribe((terms) => {
        this.termsSubject.next(terms);
      });
  }

  updateTerm(term: Term) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.put(`${this.config.apiEndpoint}/terms`, term, {search: params})
        .map(res => res.json())
        .subscribe((term) => {
          resolve(term);
        });
    });
  }

  updateLesson(lesson: Lesson) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.put(`${this.config.apiEndpoint}/lessons`, lesson, {search: params})
        .map(res => res.json())
        .subscribe((lesson) => {
          resolve(lesson);
        });
    });
  }

}
