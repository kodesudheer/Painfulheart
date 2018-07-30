import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { UserinfoComponent } from './userinfo/userinfo.component';
import {ServerService} from './server.servie';

import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
@NgModule({
  declarations: [
    AppComponent,
    UserinfoComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [ServerService],
  bootstrap: [AppComponent]
})
export class AppModule { }
