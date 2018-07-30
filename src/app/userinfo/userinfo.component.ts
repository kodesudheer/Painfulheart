import { Component, OnInit } from '@angular/core';

import {ServerService} from '../server.servie';

import {Response} from '@angular/http';
@Component({
  selector: 'app-userinfo',
  templateUrl: './userinfo.component.html',
  styleUrls: ['./userinfo.component.css']
})
export class UserinfoComponent implements OnInit {

  constructor(private serverservice:ServerService) { }

  ngOnInit() {
    this.loader=false;
    this.sucess_submit=false;
    this.failure_submit=false;
  }

  NameValue:string='';
  AddressValue:string='';
  PhoneValue:string='';
  EmailValue:string='';
  LastuseValue:string='';
  AmountValue:string='';
  Quote6checked:boolean=false;
  Quote12checked:boolean=false;
  Quote24checked:boolean=false;
  Quote36checked:boolean=false;
  QuoteValue:string='';

  /*error message variable*/
  uname_req:boolean =false;
  phone_req:boolean = false;
  email_req:boolean= false;
  luse_req:boolean =false;
  amt_req:boolean=false;
   quote_req:boolean=false;
   sucess_submit:boolean=false;
   failure_submit:boolean=false;
  loader:boolean=false;
  amount_numberonly:boolean=false;
  usage_numberonly:boolean=false;


  numberOnly(event):boolean{
    const charCode=(event.which) ? event.which : event.KeyCode;
    if(charCode >31 && (charCode < 48 || charCode >57)){

      return false;
    }
    return true;

  }

  click6() {
    console.log("entered inside function");
    if (this.Quote6checked) {
      this.Quote12checked = false;
      this.Quote24checked = false;
      this.Quote36checked = false;
      this.QuoteValue='6';
      this.quote_req=false;
    }
  }
  click12() {
    if (this.Quote12checked) {
      this.Quote6checked = false;
      this.Quote24checked = false;
      this.Quote36checked = false;
      this.QuoteValue='12';
      this.quote_req=false;
    }
  }
  click24() {
    if (this.Quote24checked) {
      this.Quote6checked = false;
      this.Quote12checked = false;
      this.Quote36checked = false;
      this.QuoteValue='24';
      this.quote_req=false;
    }
  }
  click36() {
    if (this.Quote36checked) {
      this.Quote6checked = false;
      this.Quote12checked = false;
      this.Quote24checked = false;
      this.QuoteValue='36';
      this.quote_req=false;
    }
  }
  /*Below function is to disappear errors when you entered some input vale*/
  inputed(inputvalue:Event){

    /*console.log("Entered value is" + (<HTMLInputElement>inputvalue.target).value);*/

    if(this.NameValue !=''){
      this.uname_req= false;
    }
    if(this.PhoneValue !=''){
      this.phone_req =false;
    }
    if(this.EmailValue !=''){
      this.email_req=false;
    }
    if(this.LastuseValue!=''){
      this.luse_req=false;
    }
    if(this.AmountValue!=''){
      this.amt_req=false;
    }
    this.loader=false;
  }
  /*Method after submitting form*/
  submitForm(){
    console.log("entered in submit function");
    this.loader =true;
    this.failure_submit=false;
    this.sucess_submit=false;
    if(this.NameValue ==''){
      this.uname_req=true;
      this.loader=false;
    }
    if(this.PhoneValue ==''){
      this.phone_req=true;
      this.loader=false;
    }
    if(this.EmailValue == ''){
      this.email_req=true;
      this.loader=false;
    }
    if(this.LastuseValue == ''){
      this.luse_req=true;
      this.loader=false;
    }
    if(this.AmountValue == ''){
      this.amt_req = true;
      this.loader=false;
    }
    if(!(this.Quote6checked||this.Quote12checked||this.Quote24checked||this.Quote36checked)){

      this.quote_req=true;
      this.loader=false;
    }
    if((this.NameValue !='' && this.NameValue !=null)&&(this.PhoneValue!='' && this.PhoneValue!=null)&&(this.EmailValue!='' && this.EmailValue !=null)&&(this.LastuseValue!='' && this.LastuseValue!=null)&&(this.AmountValue!=''&&this.AmountValue!=null)&&(this.Quote6checked||this.Quote12checked||this.Quote24checked||this.Quote36checked)){
      console.log("Selected quote is:" + this.QuoteValue );
      /*calling the rest call*/
      this.serverservice.sendQuoteinPdf(this.NameValue,this.AddressValue,this.PhoneValue,this.EmailValue,this.LastuseValue,this.AmountValue,this.QuoteValue).subscribe(
        (response:Response)=>{


            console.log("Success email sent!!!!");
            console.log("Test is:" + response.text());
          this.loader=false;
          this.failure_submit=false;
            this.sucess_submit=true;
            this.NameValue='';
            this.AddressValue='';
            this.PhoneValue='';
            this.EmailValue='';
            this.LastuseValue='';
            this.AmountValue='';
            this.QuoteValue='';
           this.Quote6checked = false;
           this.Quote24checked = false;
           this.Quote36checked = false;
           this.Quote12checked = false;


            console.log("Test is:" + response.text());

          console.log(response);
        },
        (error)=>{
          console.log(error);
          this.loader=false;
          this.sucess_submit=false;
          this.failure_submit=true;
          this.NameValue='';
          this.AddressValue='';
          this.PhoneValue='';
          this.EmailValue='';
          this.LastuseValue='';
          this.AmountValue='';
          this.QuoteValue='';
          this.Quote6checked = false;
          this.Quote24checked = false;
          this.Quote36checked = false;
          this.Quote12checked = false;
        }
      );
    }
  }



}
