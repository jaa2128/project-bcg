import { Component } from '@angular/core';
import { NeedService } from '../need.service';
import { UserService } from '../user.service';
import { User } from '../user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {

  constructor(private userService: UserService,
    private needService: NeedService,
    private router: Router) { }
}
