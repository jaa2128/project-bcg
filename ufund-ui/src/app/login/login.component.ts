import { Component, NgModule } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { Observable } from 'rxjs';
import { HttpResponse, HttpStatusCode } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from '../app.component';
import { Router } from '@angular/router';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  formData = {
      username: '',
      password: ''
  };
  submitted: boolean = false;
  statusMessage: string = 'Please log in or sign up!';
  isLogin: boolean = false;   //set to true when login is clicked and false when sign up is clicked

  onSubmit(formData: { username: string, password: string }): void {
    this.submitted = true;
    if(this.isLogin) {
      this.login(formData.username, formData.password);
    }
    else {
      this.signup(formData.username, formData.password);
    }
  }

  onLoginClick(): void {
    this.isLogin = true;
  }

  onSignupClick(): void {
    this.isLogin = false;
  }

  constructor(private userService: UserService,
    private router: Router
    ) { }
  
  login(username: string, password: string): void {
    console.log(username);
    console.log(password);
    
    if(username.trim().length == 0 || password.trim().length == 0) {
      this.statusMessage = 'Username or password is blank!';
      return;
    }

    this.userService.getUser(username, password).subscribe(
      (response: HttpResponse<User>) => {
        this.userService.setCurrentUser(response.body);
        this.statusMessage = 'Success! Logging in...'
        this.router.navigateByUrl("needs");
      },
      (error) => {
        if(error.status == 404) { //not found
          this.statusMessage = 'Username not found!';
        }
        else if(error.status == 401) { //unauthorized
          this.statusMessage = 'Password is incorrect!';
        }
        else if(error.status == 400) { //bad request
          this.statusMessage = 'Username or password is blank!';
        }
        else { //internal server error
          this.statusMessage = 'There was a server error!';
        }
      }
    );
  }

  signup(username: string, password: string): void {
    this.userService.createUser(username, password).subscribe(
      (response: HttpResponse<User>) => {
        this.userService.setCurrentUser(response.body);
        this.statusMessage = 'Success! Signing up...s'
        this.router.navigateByUrl("needs");
      },
      (error) =>
      {
        if(error.status == 400) { //bad request
          this.statusMessage = 'Username or password is blank!'; 
        }
        else if(error.status == 409) { //conflict
          this.statusMessage = 'Username is already in use!';
        }
        else {
          this.statusMessage = 'There was a server error!';
        }
      }
    )
  }
}
