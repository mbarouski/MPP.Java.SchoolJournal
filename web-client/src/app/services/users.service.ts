import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";


@Injectable()
export class UsersService {

  userSubject: ReplaySubject<any> = new ReplaySubject(1);

  constructor(@Inject(APP_CONFIG) private config: any, private http: Http){

  }



}
