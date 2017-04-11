import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http, URLSearchParams} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";
import {AuthService} from "./auth.service";
import {HttpUtil} from "./http.util";


@Injectable()
export class PupilsService {

  constructor(@Inject(APP_CONFIG) private config: any,
              private http: Http,
              private authService: AuthService){
  }

  fetchPupil(pupilId) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.get(`${this.config.apiEndpoint}/pupils/${pupilId}`, {search: params})
        .map(res => {
          return res.json();
        })
        .subscribe((pupil) => {
          resolve(pupil);
        });
    });
  }

  fetchPupilsByClass(classId) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      params.append('classId', classId);
      this.http.get(`${this.config.apiEndpoint}/pupils`, {search: params})
        .map(res => {
          return res.json();
        })
        .subscribe((pupils) => {
          resolve(pupils);
        });
    });
  }

  fetchPupilsWithoutClass() {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.get(`${this.config.apiEndpoint}/pupils/withoutClass`, {search: params})
        .map(res => {
          return res.json();
        })
        .subscribe((pupils) => {
          resolve(pupils);
        });
    });
  }

  getPupilFullName(pupil) {
    if (!pupil) return;
    return `${pupil.firstName} ${pupil.pathronymic} ${pupil.lastName}`;
  }

  removeFromClass(id) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      params.append('pupilId', id);
      this.http.put(`${this.config.apiEndpoint}/pupils/removeFromClass`, {}, {search: params})
        .map(res => {
          return res.json();
        })
        .subscribe((pupil) => {
          resolve(pupil);
        });
    });
  }

  movePupilToAnotherClass(pupilId, classId) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      params.append('pupilId', pupilId);
      params.append('classId', classId);
      this.http.put(`${this.config.apiEndpoint}/pupils`, {}, {search: params})
        .map(res => res.json())
        .subscribe((data) => {
          resolve({});
        });
    });
  }

  addPupil(pupil) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.post(`${this.config.apiEndpoint}/pupils`, pupil, {search: params})
        .map(res => res.json())
        .subscribe((pupil) => {
          resolve(pupil);
        });
    });
  }

  updatePupil(pupil) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.put(`${this.config.apiEndpoint}/pupils`, pupil, {search: params})
        .map(res => res.json())
        .subscribe((pupil) => {
          resolve(pupil);
        });
    });
  }

  deletePupil(id) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.delete(`${this.config.apiEndpoint}/pupils/${id}`, {search: params})
        .subscribe((data) => {
          resolve({});
        });
    });
  }

}
