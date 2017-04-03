import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http, Headers, RequestOptions, URLSearchParams} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";
import {AuthService} from "./auth.service";
import {HttpUtil} from "./http.util";


@Injectable()
export class ScheduleService {

  constructor(@Inject(APP_CONFIG) private config: any, private http: Http, private authService: AuthService){  }

  fetchPupilSchedule(classId: number) {
    if(!classId) return Promise.reject({});
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.get(`${this.config.apiEndpoint}/schedule/class/${classId}`, {search: params})
        .map(res => {
          return res.json();
        })
        .subscribe((schedule) => {
          resolve(schedule);
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


}
