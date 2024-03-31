import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Need } from '../need'; 
import { NeedsComponent } from '../needs/needs.component';
import { NeedService } from '../need.service';
import { User } from '../user';
import { UserService } from '../user.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-need-detail',
  templateUrl: './need-detail.component.html',
  styleUrl: './need-detail.component.css'
})
export class NeedDetailComponent implements OnInit {

  currentUser: User | null = null;
  
  need!: Need;

  contribution!: number;

  dropdownSelection!: string;
  otherType!: string;

/* this breaks stuff. idk why */
  isSundayChecked!: boolean;
  isMondayChecked!: boolean; 
  isTuesdayChecked!: boolean; 
  isWednesdayChecked!: boolean; 
  isThursdayChecked!: boolean; 
  isFridayChecked!: boolean;
  isSaturdayChecked!: boolean; 

  helperStatusMessage = 'Enter how much you want to contribute then hit \'Add\'!';
  adminStatusMessage = 'Edit your need then hit \'Save\'!';

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private needService: NeedService,
    private userService: UserService
  ) {}

  logOut(): void {
    this.userService.logOut();
  }

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    this.userService.validate();
    this.getNeed();
  }

  selectDropdown(): void {
    const basicTypes = ["Money", "Goods", "Volunteer"];
    if(basicTypes.includes(this.need.type)) {
      this.dropdownSelection = this.need.type;
    }
    else {
      this.dropdownSelection = "Other";
      this.otherType = this.need.type;
    }
  }

  // calls backend getNeed() method
  getNeed(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.needService.getNeed(id)
      .subscribe(need => {this.need = need; this.selectDropdown();});
  }

  goBack(): void {
    this.location.back();
  }

  // updates need and saves it back to needs.json, then goes back
  save(): void {
    if (this.need) {  
      this.needService.updateNeed(this.need).subscribe(
      (response: HttpResponse<Object>) => {
        this.adminStatusMessage = 'Success! Going back to listing...';
        this.goBack()
      },
      (error) => {
        if(error.error == "Name Field Empty") { //not acceptable
          this.adminStatusMessage = 'Name Field is empty!';
        }

        else if(error.error == "Description Field Empty") { //not acceptable
          this.adminStatusMessage = 'Description Field is empty!';
        }

        else if(error.error == "Type Field Empty") { //not acceptable
          this.adminStatusMessage = 'Type Field is empty!';
        }

        else if(error.error == "Goal Amount Field Empty") { //not acceptable
          this.adminStatusMessage = 'Goal Amount Field is empty!';
        }

        else if(error.status == 409) { //conflict
          this.adminStatusMessage = 'There is already a need with this name!';
        }
        else { //internal server error
          this.adminStatusMessage = 'There was a server error!';
        }
      });
    };
  }

  add(): void {
    this.userService.addNeed(this.need.id, this.contribution).subscribe(
      (response: HttpResponse<number>) => {
        this.helperStatusMessage = 'Success! Going back to listing...'
        this.goBack()
      },
      (error) => {
        if(error.status == 400) { //bad request
          this.helperStatusMessage = 'Please enter a positive value!';
        }
        else if(error.status == 404) { //not found
          this.helperStatusMessage = 'You are not signed in!';
        }
        else {
          this.helperStatusMessage = 'There was a server error!';
        }

      });
  }

  flipSunday(): void { this.isSundayChecked = !this.isSundayChecked; }
  flipMonday(): void { this.isMondayChecked = !this.isMondayChecked; }
  flipTuesday(): void { this.isTuesdayChecked = !this.isTuesdayChecked; }
  flipWednesday(): void { this.isWednesdayChecked = !this.isWednesdayChecked; }
  flipThursday(): void { this.isThursdayChecked = !this.isThursdayChecked; }
  flipFriday(): void { this.isFridayChecked = !this.isFridayChecked; }
  flipSaturday(): void { this.isSaturdayChecked = !this.isSaturdayChecked; }

}
