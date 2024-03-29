import { Component, OnInit } from '@angular/core';
import { NeedService } from '../need.service';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { User } from '../user';
import { Need } from '../need';
import { HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrl: './about.component.css'
})
export class AboutComponent implements OnInit{
  currentUser: User | null = null;

    
  constructor(private needService: NeedService,
    private userService: UserService,
    private router: Router) { }


    ngOnInit(): void {
      this.currentUser = this.userService.getCurrentUser();
      this.userService.validate();
    }
}
