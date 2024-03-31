import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { UserService } from '../user.service';
import { User } from '../user';
import { Router } from '@angular/router';
@Component({
  selector: 'app-needs',
  templateUrl: './needs.component.html',
  styleUrl: './needs.component.css'
})

export class NeedsComponent implements OnInit {

  needs: Need[] = [];
  currentUser: User | null = null;
  adminStatusMessage = 'Click on \'Add Need\' to create a new need!';
  showButton:number=-1;
  selectedOption = '';
  otherType:string = '';
  
  constructor(private needService: NeedService,
    private userService: UserService,
    private router: Router) { }

  logOut(): void {
    this.userService.logOut();
  }

  getNeeds(): void {
    this.needService.getNeeds()
        .subscribe(needs => this.needs = needs);
  }

  createNeed(name:string, description:string, type:string, targetQuantity: number, availability: Array<boolean>): void {
    name = name.trim();
    description = description.trim();
    if(type === 'Other') {
      type = this.otherType.trim();
    }
    else {
      type = type.trim();
    }
    
    this.needService.createNeed({name, description, type, targetQuantity, availability} as Need).subscribe(need => {this.needs.push(need)},
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
    })

  }

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    this.userService.validate();
    this.getNeeds();
  }

  confirm(id: number): void {
    this.showButton = id;
  }
  deny(): void {
    this.showButton = -1;
  }

  delete(need: Need): void {
    this.needs = this.needs.filter(n => n !== need);
    this.needService.deleteNeed(need.id).subscribe();
    this.showButton = -1;
  }

  onSelected(value: string): void{
    this.selectedOption = value;
  }

  checkboxValBool(value: string): boolean{
    if(value == "on"){ return false; } else { return true; }
  }

}
