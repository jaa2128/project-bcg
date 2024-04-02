import { Component, NgModule } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { Observable } from 'rxjs';
import { HttpResponse, HttpStatusCode } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from '../app.component';
import { Router } from '@angular/router';
@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {
  formData = {
      username: '',
      password: '',
      confirm: ''
  };
  currentUser: User | null = null;
  statusMessage: string = 'Enter your username and new password!';

  constructor(private userService: UserService,
    private router: Router
    ) { }

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
  }

  onSubmit(formData: { username: string, password: string, confirm: string}): void {
    this.submitPassword(formData.username, formData.password, formData.confirm);
  }

  submitPassword(username: string, newPassword: string, confirmPassword: string): void {
    console.log(newPassword);
    
    if(username.trim().length == 0) {
      this.statusMessage = "Username is blank!";
      return;
    }
    if(newPassword.trim().length == 0 || confirmPassword.trim().length == 0) {
      this.statusMessage = "Password is blank!";
      return;
    }
    if(!(confirmPassword === newPassword)) {
      this.statusMessage = "Passwords do not match!";
      return;
    }

    this.userService.changePassword(username, newPassword).subscribe(
      (response: HttpResponse<any>) => {
        this.statusMessage = "Password changed successfully! Going back to login page...";
        setTimeout(() => {
          this.router.navigateByUrl("login");
        }, 1000);
      },
      (error) => {
        if(error.status == 400) { //bad request
          this.statusMessage = 'Username or password is invalid!';
        }
        else { //internal server error
          this.statusMessage = 'There was a server error!';
        }
      }
    );
  }
}
