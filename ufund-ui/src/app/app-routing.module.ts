import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NeedsComponent } from './needs/needs.component';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { NeedSearchComponent } from './need-search/need-search.component';
import { LoginComponent } from './login/login.component';
import { BasketComponent } from './basket/basket.component';
import { SettingsComponent } from './settings/settings.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { AboutComponent } from './about/about.component';

const routes: Routes = [
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'about', component: AboutComponent},
  {path: 'needs', component: NeedsComponent},
  {path: 'search', component: NeedSearchComponent},
  {path: 'login', component: LoginComponent},
  {path: 'detail/:id', component: NeedDetailComponent},
  {path: 'basket', component: BasketComponent},
  {path: 'about', component: AboutComponent},
  {path: 'basket', component: BasketComponent},
  {path: 'settings', component: SettingsComponent},
  {path: 'forgot-password', component: ForgotPasswordComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
