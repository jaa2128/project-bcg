import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';
@Component({
  selector: 'app-needs',
  templateUrl: './needs.component.html',
  styleUrl: './needs.component.css'
})

export class NeedsComponent implements OnInit {

  needs: Need[] = [];
  
  constructor(private needService: NeedService) { }

  getNeeds(): void {
    this.needService.getNeeds()
        .subscribe(needs => this.needs = needs);
  }

  createNeed(name:string, description:string, type:string, targetQuantity: number): void {
    this.needService.createNeed(name, description, type, targetQuantity)
  }

  ngOnInit(): void {
    this.getNeeds();
  }

}
