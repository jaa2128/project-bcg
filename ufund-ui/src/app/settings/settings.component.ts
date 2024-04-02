import { Component, NgModule } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { HttpResponse, HttpStatusCode } from '@angular/common/http';
import { Router } from '@angular/router';
import { NeedService } from '../need.service';
import { Need } from '../need';


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

  needs: Need[] = [];

  // administrator statistics
  totalNeeds: number = 0;
  totalMoneyNeeds: number = 0;
  totalGoodsNeeds: number = 0;
  totalVolunteerNeeds: number = 0;
  totalOtherNeeds: number = 0;
  totalMoneyRaised: number = 0;
  totalNeedsFulfilled: number = 0;
  averageFulfillmentPercentage: number = 0;


statusMessage: string = 'Please log in or sign up!';

  constructor(private userService: UserService,
    private needService: NeedService,
    private router: Router) { }

  logOut(): void {
    this.userService.logOut();
  }

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    this.userService.validate();
    this.getNeeds();
    this.calculateStatistics(this.needs);
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

  changeAvailability(sundayAvailability: Boolean, mondayAvailability: Boolean, tuesdayAvailability: Boolean, wednesdayAvailability: Boolean, thursdayAvailability: Boolean, fridayAvailability: Boolean, saturdayAvailability: Boolean): void {
    let newAvailability: Array<Boolean> = [sundayAvailability, mondayAvailability, tuesdayAvailability, wednesdayAvailability, thursdayAvailability, fridayAvailability, saturdayAvailability];
    if(this.currentUser != null){
      this.userService.changeAvailability(this.currentUser.username, newAvailability).subscribe(
        (response: HttpResponse<any>) => {
          return;
        }
      );
    } this.statusMessage = 'There was a server error!'
  }

  getNeeds(): void {
    this.needService.getNeeds()
        .subscribe(needs => this.needs = needs);
  }

  calculateStatistics(needs: Need[]): void {
    needs.forEach(need => {
      this.totalNeeds++;
      if(need.type == "Money"){ this.totalMoneyNeeds++; this.totalMoneyRaised += need.currentQuantity; }
      if(need.type == "Physical Goods"){ this.totalGoodsNeeds++; }
      if(need.type == "Volunteer Hours"){ this.totalVolunteerNeeds++; }
      if(need.type != "Money" && need.type != "Volunteer Hours" && need.type != "Physical Goods"){ this.totalOtherNeeds++; }
      if(need.currentQuantity >= need.targetQuantity){ this.totalNeedsFulfilled++; }
      this.averageFulfillmentPercentage += (need.currentQuantity / need.targetQuantity);
    });
    if(this.needs.length != 0){  this.averageFulfillmentPercentage /= this.needs.length; } else { this.averageFulfillmentPercentage = 0; }
  }

}
