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

  // calls backend getNeed() method
  getNeed(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.needService.getNeed(id)
      .subscribe(need => this.need = need);
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
        if(error.status == 406) { //not acceptable
          this.adminStatusMessage = 'There is an invalid field!';
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

}
