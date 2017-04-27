import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http, Headers, RequestOptions} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";
import {LoginData} from "../models/LoginData";
import {Token} from "../models/Token";
import {HttpUtil} from "./http.util";

@Injectable()
export class AuthService {
  private AUTH_ROUTE: string = `${this.config.apiEndpoint}/auth`;

  userSubject: ReplaySubject<any> = new ReplaySubject(1);

  private _user: any;

  get userData():any {
    return JSON.parse(localStorage.getItem('school-journal-user-data'));
  }

  set userData(value:any) {
    localStorage.setItem('school-journal-user-data', JSON.stringify(value));
  }

  get user():any {
    return this.userData ? this.userData.user : null;
  }

  get token():string {
    return this.userData ? this.userData.value : '';
  }

  get role():string {
    return this.userData ? this.userData.user.role.name.toLowerCase() : '';
  }

  constructor(@Inject(APP_CONFIG) private config: any, private http: Http){
    this.userSubject.next(this.user);
  }

  login(loginData: LoginData) {
    return new Promise((resolve, reject) => {
      return this.http.post(`${this.AUTH_ROUTE}/login`, loginData, HttpUtil.REQUEST_OPTIONS_WITH_CONTENT_TYPE_JSON)
        .map(res => {
          return res.json();
        })
        .catch((err) => {
          reject(err);
          return Observable.throw(err);
        })
        .subscribe((user) => {
          this.userData = user;
          this.userSubject.next(this.user);
          resolve();
        });
    });
  }

  logout() {
    return new Promise((resolve, reject) => {
      this.http.post(`${this.AUTH_ROUTE}/logout?token=${this.token}`, null, HttpUtil.REQUEST_OPTIONS_WITH_CONTENT_TYPE_JSON)
        .catch((err) => {
          reject(err);
          return Observable.throw(err);
        })
        .subscribe((res) => {
          this.userData = null;
          this.userSubject.next(null);
          resolve();
        });

    });
  }

  isLogged() {
    return !!this.token;
  }

}
