import { Component, NgModule } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { Observable } from 'rxjs';
import { HttpResponse, HttpStatusCode } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from '../app.component';
import { Router } from '@angular/router';


@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent {
  currentUser: User | null = null;
  formData = {
    newUsername: '',
  };
  formData2 = {
    newPassword: ''
  };

statusMessage: string = 'Please log in or sign up!';

  constructor(private userService: UserService,
    private router: Router) { }

  logOut(): void {
    this.userService.logOut();
  }

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    this.userService.validate();
  }

  changeUsername(formData: {newUsername: string }): void {
    this.submitUsername(formData.newUsername);
  }

  changePassword(formData2: {newPassword: string }): void {
    this.submitPassword(formData2.newPassword);
  }

  submitUsername(username: string): void {
    console.log(username);
    
    if(username.trim().length == 0) {
      return;
    }
    if(this.currentUser == null) {
      return;
    }

    this.userService.changeUsername(this.currentUser.username, username).subscribe(
      (response: HttpResponse<any>) => {
        this.userService.getUser(username, (this.currentUser?.password) as string).subscribe(
          (response: HttpResponse<any>) => {
            const newUser: User = response.body;
            this.userService.setCurrentUser(newUser);
            this.currentUser = this.userService.getCurrentUser();
            this.statusMessage = "Username changed successfully!"
          }
        )
      },
      (error) => {
        if(error.status == 400) { //bad request
          this.statusMessage = 'Username or password is blank!';
        }
        else { //internal server error
          this.statusMessage = 'There was a server error!';
        }
      }
    );
  }

  submitPassword(password: string): void {
    console.log(password);
    
    if(password.trim().length == 0) {
      return;
    }
    if(this.currentUser == null) {
      return;
    }

    this.userService.changePassword(this.currentUser.username, password).subscribe(
      (response: HttpResponse<any>) => {
        this.statusMessage = "Password changed successfully!"
      },
      (error) => {
        if(error.status == 400) { //bad request
          this.statusMessage = 'Username or password is blank!';
        }
        else { //internal server error
          this.statusMessage = 'There was a server error!';
        }
      }
    );
  }


}
