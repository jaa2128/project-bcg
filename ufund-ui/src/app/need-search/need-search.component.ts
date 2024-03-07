import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';

import { Observable, Subject } from 'rxjs';

import {
   debounceTime, distinctUntilChanged, switchMap
 } from 'rxjs/operators';

@Component({
  selector: 'app-need-search',
  templateUrl: './need-search.component.html',
  styleUrl: './need-search.component.css'
})
export class NeedSearchComponent {
  needs$!: Observable<Need[]>;
  private searchTerms = new Subject<string>();

  constructor(private needService: NeedService) {}

  search(containsText: string): void {
    this.searchTerms.next(containsText);
  }

  ngOnInit(): void {
    this.needs$ = this.searchTerms.pipe(
      
      debounceTime(300),

      
      distinctUntilChanged(),

      
      switchMap((containsText: string) => this.needService.searchNeeds(containsText)),
    );
  }
}
