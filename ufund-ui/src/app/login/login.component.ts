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

  //set to true when login is clicked and false when sign up is clicked
  isLogin: boolean = false;

  response: number = 0;
  user: User | null | undefined;

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
    
    this.userService.getUser(username, password).subscribe(
      (response: HttpResponse<User>) => {
        this.response = response.status;
        this.user = response.body;
        this.router.navigateByUrl("needs");
      },
      (error) => {
        this.response = error.status;
        this.user = null
      }
    );
  }

  signup(username: string, password: string): void {
    this.userService.createUser(username, password).subscribe(
      (response: HttpResponse<User>) => {
        this.response = response.status
        this.user = response.body
      },
      (error) =>
      {
        this.response = error.status;
        this.user = null
      }
    )
  }
}
