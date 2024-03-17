import { Component, OnInit } from '@angular/core';
import { NeedService } from '../need.service';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { User } from '../user';
import { Need } from '../need';
import { HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrl: './basket.component.css'
})

export class BasketComponent implements OnInit {
  basket: Need[] = [];
  contributions: number[] = [];
  currentUser: User | null = null;

  constructor(private userService: UserService,
    private needService: NeedService,
    private router: Router) { }

  getNeeds(): void {
    this.userService.getUserNeeds((this.currentUser as User).username)
      .subscribe((response: HttpResponse<number[]>) =>
      {
        response.body?.forEach(element => 
            this.needService.getNeed(element).subscribe((
              need => this.basket.push(need))))
      }
      )
    }

    getContributions(): void {
      this.userService.getUserContributions((this.currentUser as User).username)
        .subscribe((response: HttpResponse<number[]>) =>
        {
          response.body?.forEach(element => this.contributions.push(element));
        })
      }

  removeNeed(id: number): void {
    this.userService.removeNeed(id).subscribe(
      (response: HttpResponse<any>) => {
        this.basket = [];
        this.contributions = [];
        this.getNeeds();
        this.getContributions();
      }
    )
  }

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    this.userService.validate();
    if (this.currentUser?.admin) {
      this.router.navigateByUrl("needs");
    }
    this.getNeeds();
    this.getContributions();
  }
}
