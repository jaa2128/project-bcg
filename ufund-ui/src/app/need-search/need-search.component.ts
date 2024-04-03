import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';

import { Observable, Subject } from 'rxjs';

import {
   debounceTime, distinctUntilChanged, switchMap
 } from 'rxjs/operators';
import { UserService } from '../user.service';
import { User } from '../user';

@Component({
  selector: 'app-need-search',
  templateUrl: './need-search.component.html',
  styleUrl: './need-search.component.css'
})
export class NeedSearchComponent implements OnInit {
  needs$!: Observable<Need[]>;
  currentUser: User | null = null;
  showButton:number=-1;
  selectedOption = '';
  constructor(private needService: NeedService, 
  private userService: UserService) {}
  availability: Boolean[] = [];
  errorMessage = 'Narrow down the list with search filters!'

  logOut(): void {
    this.userService.logOut();
  }

  search(keyword: string, type: string, min: string, max:string): void {
    this.needs$ = this.needService.searchNeeds(keyword, type, min, max, this.availability);
  if(min.trim()!='' && max.trim() != ''){
    if(min>=max){
      this.errorMessage = 'Make sure minimum value is lower than maximum value!';
    }
    else{
      this.errorMessage = 'Try filtering your search';
    }
  }
  }

  ngOnInit(): void {
    this.userService.validate();
    this.currentUser = this.userService.getCurrentUser();
    this.availability = (this.currentUser as User).availability;
    this.userService.validate();
    this.userService.saveToLocalStorage(this.userService.pageKey, "search");
  }

  confirm(id: number): void {
    this.showButton = id;
  }
  deny(): void {
    this.showButton = -1;
  }
  /*
  delete(need: Need): void {
    this.needs$ = this.needs$.filter(n => n !== need);
    this.needService.deleteNeed(need.id).subscribe();
    this.showButton = -1;
  }
  */
}
