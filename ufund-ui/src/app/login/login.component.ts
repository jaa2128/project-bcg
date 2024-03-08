import { Component } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})


export class LoginComponent {
  constructor(private userService: UserService) { }

  getUser(username: string, password: string): HttpResponse<any> {
    return this.userService.getUser(username, password)
  }

  login(username: string, password: string): void {
    if (this.getUser(username, password) == )
  }
}
