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
  private searchTerms = new Subject<string>();
  currentUser: User | null = null;

  constructor(private needService: NeedService, 
  private userService: UserService) {}

  logOut(): void {
    this.userService.logOut();
  }

  search(containsText: string): void {
    this.searchTerms.next(containsText);
  }

  ngOnInit(): void {
    this.userService.validate();
    this.currentUser = this.userService.getCurrentUser();
    this.userService.validate();
    this.userService.saveToLocalStorage(this.userService.pageKey, "search");
    this.needs$ = this.searchTerms.pipe(
      
      debounceTime(300),

      
      distinctUntilChanged(),

      
      switchMap((containsText: string) => this.needService.searchNeeds(containsText)),
    );
  }
}
