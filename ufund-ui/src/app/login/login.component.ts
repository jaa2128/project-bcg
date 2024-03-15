import { Component, NgModule } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { Observable } from 'rxjs';
import { HttpResponse, HttpStatusCode } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from '../app.component';

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

  response: number = 0;
  user: User | null | undefined;

  onSubmit(formData: { username: string, password: string }): void {
    this.submitted = true;
    this.login(formData.username, formData.password);
  }

  constructor(private userService: UserService) { }
  
  login(username: string, password: string): void {
    console.log(username);
    console.log(password);
    
    this.userService.getUser(username, password).subscribe(
      (response: HttpResponse<User>) => {
        this.response = response.status;
        this.user = response.body;
      },
      (error) => {
        this.response = error.status;
      }
    );
  }
}
