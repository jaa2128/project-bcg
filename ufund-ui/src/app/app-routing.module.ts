import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NeedsComponent } from './needs/needs.component';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { NeedSearchComponent } from './need-search/need-search.component';

const routes: Routes = [
  {path: '', redirectTo: '/needs', pathMatch: 'full'},
  {path: 'needs', component: NeedsComponent},
  {path: 'search', component: NeedSearchComponent},
  {path: 'detail/:id', component: NeedDetailComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
