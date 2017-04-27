import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http, URLSearchParams} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";
import {AuthService} from "./auth.service";
import {User} from "../models/User";


@Injectable()
export class UsersService {
  usersSubject: ReplaySubject<any> = new ReplaySubject(1);

  constructor(@Inject(APP_CONFIG) private config: any,
              private http: Http,
              private authService: AuthService){

  }

  fetchUsers() {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.get(`${this.config.apiEndpoint}/users`, {search: params})
        .map(res => res.json())
        .catch((err) => {
          this.usersSubject.next([]);
          reject(err);
          return Observable.throw(err);
        })
        .subscribe((users) => {
          this.usersSubject.next(users);
          resolve(users);
        });
    });
  }

  addUser(user: User) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.post(`${this.config.apiEndpoint}/users`, user, {search: params})
        .map(res => res.json())
        .catch((err) => {
          reject(err);
          return Observable.throw(err);
        })
        .subscribe((user) => {
          resolve(user);
        });
    });
  }

  updateUser(user: User) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.put(`${this.config.apiEndpoint}/users`, user, {search: params})
        .map(res => res.json())
        .catch((err) => {
          reject(err);
          return Observable.throw(err);
        })
        .subscribe((user) => {
          resolve(user);
        });
    });
  }

  deleteUser(id: number) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.delete(`${this.config.apiEndpoint}/users/${id}`, {search: params})
        .catch((err) => {
          reject(err);
          return Observable.throw(err);
        })
        .subscribe((data) => {
          resolve({});
        });
    });
  }

  changeRole(userId, roleId) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.put(`${this.config.apiEndpoint}/users/changeRole?userId=${userId}&roleId=${roleId}`, {}, {search: params})
        .map(res => res.json())
        .catch((err) => {
          reject(err);
          return Observable.throw(err);
        })
        .subscribe((user) => {
          resolve(user);
        });
    });
  }

  fetchUser(id) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.get(`${this.config.apiEndpoint}/users/${id}`, {search: params})
        .map(res => res.json())
        .catch((err) => {
          reject(err);
          return Observable.throw(err);
        })
        .subscribe((user) => {
          resolve(user);
        });
    });
  }

  changePassword(userId, password) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      params.append('password', password);
      this.http.put(`${this.config.apiEndpoint}/users/${userId}`, {}, {search: params})
        .map(res => res.json())
        .catch((err) => {
          reject(err);
          return Observable.throw(err);
        })
        .subscribe((user) => {
          resolve(user);
        });
    });
  }
}
