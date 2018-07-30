import {Injectable} from '@angular/core';
import {Headers,Http} from '@angular/http';
@Injectable()
export class ServerService {
  constructor(private http: Http) {

  }

  sendQuoteinPdf(Name:String,Address:String,Phone:String,Email:String,LastUse:String,LastAmount:String,ReqTerm:String){
    const headers = new Headers({'Access-Control-Allow-Origin':'*',
      'Accept':'text/plain',
      'Content-type':'text/plain'
    });

    return this.http.post('http://localhost:9010/sendemail?name='+Name+'&address='+Address+'&Phone='+Phone+'&email='+Email+'&last_month_usage='+LastUse+'&last_month_amount='+LastAmount+'&requested_term='+ReqTerm,{headers});
  }
}
