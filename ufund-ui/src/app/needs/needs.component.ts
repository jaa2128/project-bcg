import { Component, OnInit } from '@angular/core';
import { Need } from '../need'; 
import { NeedService } from '../need.service';

@Component({
  selector: 'app-needs',
  templateUrl: './needs.component.html',
  styleUrl: './needs.component.css'
})

export class NeedsComponent {

  needs: Need[] = [];
  
  constructor(private needService: NeedService) { }

  getHeroes(): void {
    this.needService.getNeeds()
        .subscribe(needs => this.needs = needs);
  }

}
