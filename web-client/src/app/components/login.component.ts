import {Component, AfterViewInit} from '@angular/core';
import {LoginData} from "../models/LoginData";
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";

@Component({
  moduleId: module.id,
  selector: 'login-component',
  templateUrl: './templates/login.component.html',
  styleUrls: ['./styles/login.component.css']
})
export class LoginComponent implements AfterViewInit{
  loginData = new LoginData("", "");
  validationError = {
    username: {
      status: false,
      message: '',
    },
    password: {
      status: false,
      message: '',
    },
  };
  errorMessage = '';

  constructor(public router: Router, private authService: AuthService) {}

  onSubmit() {
    this.validate();
    if(this.isDataValid()) {
      this.authService.login(this.loginData)
        .then(() => {
          this.router.navigate(['/profile']);
        })
        .catch((err) => {
        if(err.status === 401) {
          this.errorMessage = 'Неправильный логин или пароль';
        } else if(err.status === 500) {
          this.errorMessage = 'Извините, ошибка на сервере';
        } else {
          this.errorMessage = err;
        }
      });
    }
  }

  validate() {
    const username = this.loginData.username;
    const password = this.loginData.password;
    if(!username) {
      this.validationError.username.status = true;
      this.validationError.username.message = 'Введите имя пользователя';
    } else if(username.length < 6 || username.length > 24) {
      this.validationError.username.status = true;
      this.validationError.username.message = 'Имя пользоваетля должно иметь не менее 6 и не более 24 символов';
    } else if(!/^[A-Za-z0-9_\.$]+/g.test(username)) {
      this.validationError.username.status = true;
      this.validationError.username.message = 'Имя пользователя может ' +
        'содержать буквы латинского алфавита (большие и маленькие), знак подчёркивания "_" и точку "."';
    } else {
      this.validationError.username.status = false;
    }
    if(!password) {
      this.validationError.password.status = true;
      this.validationError.password.message = 'Введите пароль';
    } else if(password.length < 6 || password.length > 24) {
      this.validationError.password.status = true;
      this.validationError.password.message = 'Пароль должен иметь не менее 6 и не более 24 символов';
    } else {
      this.validationError.password.status = false;
    }
  }

  isDataValid() {
    return !this.validationError.password.status && !this.validationError.username.status;
  }

  ngAfterViewInit() {
    if(this.authService.isLogged()) this.router.navigate(['/profile']);
  }
}
