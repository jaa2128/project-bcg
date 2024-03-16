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
  
  constructor(private needService: NeedService,
    private userService: UserService,
    private router: Router) { }

  getNeeds(): void {
    this.needService.getNeeds()
        .subscribe(needs => this.needs = needs);
  }

  createNeed(name:string, description:string, type:string, targetQuantity: number): void {
    name = name.trim();
    description = description.trim();
    type = type.trim();
    this.needService.createNeed({name, description, type, targetQuantity} as Need).subscribe(need => {this.needs.push(need)})

  }

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    this.userService.validate();
    this.getNeeds();
  }

}
