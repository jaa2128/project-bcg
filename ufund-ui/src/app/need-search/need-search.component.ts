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
  constructor(private needService: NeedService, 
  private userService: UserService) {}
  private searchTerms : String[] = [];

  logOut(): void {
    this.userService.logOut();
  }

  search(keyword: string, type: string, min: string, max:string): void {
    this.needs$ = this.needService.searchNeeds(keyword, type, min, max)
  }

  ngOnInit(): void {
    this.userService.validate();
    this.currentUser = this.userService.getCurrentUser();
    this.userService.validate();
}
}
