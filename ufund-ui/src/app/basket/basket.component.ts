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
  numOfTimesCheckoutClicked = 0;

  statusMessage = '';

  constructor(private userService: UserService,
    private needService: NeedService,
    private router: Router) { }

  logOut(): void {
    this.userService.logOut();
  }

  getNeeds(): void {
    this.userService.getUserNeeds((this.currentUser as User).username)
      .subscribe((response: HttpResponse<number[]>) =>
      {
        response.body?.forEach(element => 
            this.needService.getNeed(element).subscribe((
              need => this.basket.push(need))))
        if(response.body?.length == 0) {
          this.statusMessage = 'Your basket is empty!';
        }
        else {
          this.statusMessage = 'Click on \'Checkout\' to contribute!'
        }
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

  checkoutConfirmation(): void {
    // If the user has clicked Checkout less than twice
    if (this.numOfTimesCheckoutClicked < 2){
      this.numOfTimesCheckoutClicked++;
    }
  }

  checkout(): void {
    let noErrors: boolean = true;
    this.checkoutConfirmation();
    if(this.numOfTimesCheckoutClicked == 2){
      this.numOfTimesCheckoutClicked = 0;
      for(let i = 0; i < this.basket.length; i++) {
        let need: Need = this.basket[i];
        let quantity: number = this.contributions[i];
        if(quantity == null){ this.statusMessage = "You cannot leave a contribution empty!"; return; }
        if(quantity <= 0){ this.statusMessage = "You must contribute a nonzero value!"; return; }
        this.userService.editNeed(need.id, quantity).subscribe(
        (error) => {
          noErrors = false;
          if(error.status == 400) { //bad request
            this.statusMessage = 'You cannot contribute a nonpositive value!';
          }
          else if(error.status == 404) { //not found
            this.statusMessage = 'A need in your basket does not exist!';
          }
          else if(error.status == 500) { //internal server error
            this.statusMessage = 'There was a server error!';
          }
        }
      );
      this.needService.contribute(need.id, quantity).subscribe(
          (error) => {
            noErrors = false;
            if(error.status == 400) { //bad request
              this.statusMessage = 'You cannot contribute a nonpositive value!';
            }
            else if(error.status == 404) { //not found
              this.statusMessage = 'A need in your basket does not exist!';
            }
            else if(error.status == 500) { //internal server error
              this.statusMessage = 'There was a server error!';
            }
          }
        )
        if(!noErrors) { break; }
      }
      if(noErrors) {
        this.statusMessage = 'Success! Clearing basket...'
        this.clearBasket();
      }
    }
    
  }

  clearBasket(): void {
    this.userService.clearBasket((this.currentUser as User).username).subscribe(
      (response: HttpResponse<any>) => {
        this.statusMessage = 'Success! Going back to listing...';
        this.router.navigateByUrl("needs");
      },
      (error) => {
        if(error.status == 404) { //not found
          this.statusMessage = 'User does not exist!';
        }
        else if(error.status == 500) {
          this.statusMessage = 'There was a server error!';
        }
      }
    )
  }

  ngOnInit(): void {
    this.userService.validate();
    this.currentUser = this.userService.getCurrentUser();
    if (this.currentUser?.admin) {
      this.router.navigateByUrl("needs");
    }
    this.getNeeds();
    this.getContributions();
    this.numOfTimesCheckoutClicked = 0;
  }
}
