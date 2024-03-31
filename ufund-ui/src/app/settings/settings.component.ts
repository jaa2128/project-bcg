import { Component, NgModule } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { HttpResponse, HttpStatusCode } from '@angular/common/http';
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

  submitUsername(newUsername: string): void {
    console.log(newUsername);
    
    if(newUsername.trim().length == 0) {
      return;
    }
    if(this.currentUser == null) {
      return;
    }

    this.userService.changeUsername(this.currentUser.username, newUsername).subscribe(
      (response: HttpResponse<any>) => {
        this.userService.getUser(newUsername, (this.currentUser?.password) as string).subscribe(
          (response: HttpResponse<any>) => {
            const newUser: User = response.body;
            this.userService.setCurrentUser(newUser);
            this.currentUser = this.userService.getCurrentUser();
            this.statusMessage = "Username changed successfully!"
          }
        )
      },
      (error) => {
        if(error.status == 401) { //trying to set to admin
          this.statusMessage = 'Cannot set username to admin!';
        } else if(error.status == 409) { //user already exists
          this.statusMessage = 'User with this name already exists!'
        } else if(error.status == 400) { //bad request
          this.statusMessage = 'Username or password is blank!';
        } else { //internal server error
          this.statusMessage = 'There was a server error!';
        }
      }
    );
  }

  submitPassword(newPassword: string): void {
    console.log(newPassword);
    
    if(newPassword.trim().length == 0) {
      return;
    }
    if(this.currentUser == null) {
      return;
    }

    this.userService.changePassword(this.currentUser.username, newPassword).subscribe(
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
