import { Component, NgModule } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { Observable } from 'rxjs';
import { HttpResponse, HttpStatusCode } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from '../app.component';
import { Router, UrlTree } from '@angular/router';
import { LocalStorageService } from '../local-storage.service';
import { OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  constructor(private userService: UserService,
    private router: Router,
    private localStorageService: LocalStorageService
    ) { 
    }

  ngOnInit(): void {
    this.userService.clearLocalStorage();
    this.userService.saveToLocalStorage(this.userService.pageKey, "home");
  }
}

      

