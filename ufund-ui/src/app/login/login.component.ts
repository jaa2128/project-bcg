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
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  formData = {
      username: '',
      password: ''
  };
  statusMessage: string = 'Please log in or sign up!';
  isLogin: boolean = false;   //set to true when login is clicked and false when sign up is clicked

  onSubmit(formData: { username: string, password: string }): void {
    if(this.isLogin) {
      this.login(formData.username, formData.password);
    }
    else {
      this.signup(formData.username, formData.password);
    }
  }

  onLoginClick(): void {
    this.isLogin = true;
    this.userService.saveToLocalStorage(this.userService.nameKey, this.formData.username);
    this.userService.saveToLocalStorage(this.userService.passKey, this.formData.password);
    this.userService.saveToLocalStorage(this.userService.pageKey, "about");
  }

  onSignupClick(): void {
    this.isLogin = false;
  }

  constructor(private userService: UserService,
    private router: Router,
    private localStorageService: LocalStorageService
    ) { 
    }
  
  login(username: string, password: string): void {
    console.log(username);
    console.log(password);
    
    if(username.trim().length == 0 || password.trim().length == 0) {
      this.statusMessage = 'Username or password is blank!';
      return;
    }
    if(this.userService.retrieveFromLocalStorage(this.userService.pageKey) === "home"){
      return;
    }

    this.userService.getUser(username, password).subscribe(
      (response: HttpResponse<User>) => {
        this.userService.setCurrentUser(response.body);
        this.statusMessage = 'Success! Logging in...';
        if(this.userService.retrieveFromLocalStorage(this.userService.pageKey) == null){
          this.router.navigateByUrl("about");
        }
        else{
          this.router.navigateByUrl(this.userService.retrieveFromLocalStorage(this.userService.pageKey) as string);
        }
      },
      (error) => {
        if(error.status == 404) { //not found
          this.statusMessage = 'Username not found!';
        }
        else if(error.status == 401) { //unauthorized
          this.statusMessage = 'Password is incorrect!';
        }
        else if(error.status == 400) { //bad request
          this.statusMessage = 'Username or password is blank!';
        }
        else { //internal server error
          this.statusMessage = 'There was a server error!';
        }
      }
    );
  }

  signup(username: string, password: string): void {
    console.log(username);
    console.log(password);
    if(username.trim().length == 0 || password.trim().length == 0) {
      this.statusMessage = 'Username or password is blank!';
      return;
    }
    this.userService.createUser(username, password).subscribe(
      (response: HttpResponse<User>) => {
        this.userService.setCurrentUser(response.body);
        this.userService.saveToLocalStorage(this.userService.nameKey, username);
        this.userService.saveToLocalStorage(this.userService.passKey, password);
        this.statusMessage = 'Success! Signing up...'
        this.router.navigateByUrl("about");
      },
      (error) =>
      {
        if(error.status == 400) { //bad request
          this.statusMessage = 'Username or password is blank!'; 
        }
        else if(error.status == 409) { //conflict
          this.statusMessage = 'Username is already in use!';
        }
        else {
          this.statusMessage = 'There was a server error!';
        }
      }
    )
  }

  retrieveFromLocalStorage(key: string): string | null{
    return this.localStorageService.getItem(key);
  }

  onBackClick(): void {
    this.router.navigateByUrl("/home")
  }

  ngOnInit(): void {
    this.statusMessage = 'Please log in or sign up!';
    if(this.userService.storedUsername != null && this.userService.storedPassword != null){
      this.login(this.userService.storedUsername, this.userService.storedPassword); 
    }
    
    //this.userService.storedUsername = null; 
    //this.userService.storedPassword = null; 
  }

}
