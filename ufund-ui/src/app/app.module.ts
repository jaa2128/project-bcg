import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NeedsComponent } from './needs/needs.component';
import { FormsModule } from '@angular/forms';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { NeedSearchComponent } from './need-search/need-search.component';
import { LoginComponent } from './login/login.component';
import { BasketComponent } from './basket/basket.component';
import { SettingsComponent } from './settings/settings.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';

@NgModule({
  declarations: [
    AppComponent,
    NeedsComponent,
    NeedDetailComponent,
    NeedSearchComponent,
    LoginComponent,
    BasketComponent,
    SettingsComponent,
    ForgotPasswordComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
