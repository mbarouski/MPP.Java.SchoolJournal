import { Component } from '@angular/core';
import {LoginData} from "../models/LoginData";
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";

@Component({
  moduleId: module.id,
  selector: 'login-component',
  templateUrl: './templates/login.component.html',
  styleUrls: ['./styles/login.component.css']
})
export class LoginComponent {
  loginData = new LoginData("", "");

  constructor(public router: Router, private authService: AuthService) {}

  onSubmit() {
    if(this.loginData.isValid()) {
      this.authService.login(this.loginData)
        .then(() => {
          this.router.navigate(['/profile']);
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }
}
