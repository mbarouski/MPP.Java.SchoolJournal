import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http, URLSearchParams} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";
import {AuthService} from "./auth.service";
import {HttpUtil} from "./http.util";


@Injectable()
export class RolesService {

  rolesSubject: ReplaySubject<any> = new ReplaySubject(1);

  constructor(@Inject(APP_CONFIG) private config: any, private http: Http, private authService: AuthService){
    this.updateRoles();
  }

  updateRoles(){
    let params = new URLSearchParams();
    params.append('token', this.authService.token);
    return this.http.get(`${this.config.apiEndpoint}/roles`, {search: params})
      .map(res => {
        return res.json();
      })
      .subscribe((roles) => {
        this.rolesSubject.next(roles);
      });
  }

}
