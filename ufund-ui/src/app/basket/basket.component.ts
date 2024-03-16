import { Component } from '@angular/core';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { User } from '../user';
import { Need } from '../need';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrl: './basket.component.css'
})

export class BasketComponent {
  basket: Need[] = [];
  currentUser: User | null = null;

  constructor(private userService: UserService,
    private router: Router) { }

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    this.userService.validate();
    if(this.currentUser?.admin) {
      this.router.navigateByUrl("needs");
    }
  }
}
