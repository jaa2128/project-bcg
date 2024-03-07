import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NeedsComponent } from './needs/needs.component';
import { FormsModule } from '@angular/forms';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { NeedSearchComponent } from './need-search/need-search.component';

@NgModule({
  declarations: [
    AppComponent,
    NeedsComponent,
    NeedDetailComponent,
    NeedSearchComponent
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
