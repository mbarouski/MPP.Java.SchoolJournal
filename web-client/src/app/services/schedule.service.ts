import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http, Headers, RequestOptions, URLSearchParams} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";
import {AuthService} from "./auth.service";
import {HttpUtil} from "./http.util";
import {SubjectInSchedule} from "../models/SubjectInSchedule";


@Injectable()
export class ScheduleService {

  constructor(@Inject(APP_CONFIG) private config: any, private http: Http, private authService: AuthService){  }

  fetchPupilSchedule(classId: number) {
    if(!classId) return Promise.reject({});
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.get(`${this.config.apiEndpoint}/schedule/class/${classId}`, {search: params})
        .map(res => res.json())
        .subscribe((schedule) => {
          resolve(schedule);
        });
    });
  }

  addSubject(subject: SubjectInSchedule) {
    return new Promise((resolve, reject) => {
      debugger;
      return this.http.post(`${this.config.apiEndpoint}/schedule?token=${this.authService.token}`, subject, HttpUtil.REQUEST_OPTIONS_WITH_CONTENT_TYPE_JSON)
        .map(res => {
          return res.json();
        })
        .subscribe((data) => {
          resolve(data);
        });
    });
  }

  fetchTeacherSchedule() {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.get(`${this.config.apiEndpoint}/schedule/teacher`, {search: params})
        .map(res => {
          debugger;
          return res.json();
        })
        .subscribe((schedule) => {
          debugger;
          resolve(schedule);
        });
    });
  }

  fetchFullSchedule() {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.get(`${this.config.apiEndpoint}/schedule`, {search: params})
        .map(res => res.json())
        .subscribe((schedule) => {
          resolve(schedule);
        });
    });
  }

  deleteSubjectInSchedule(id: number) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.delete(`${this.config.apiEndpoint}/schedule/${id}`, {search: params})
        .subscribe((response) => {
          resolve({});
        });
    });
  }
}
