import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AppComponent} from "../app.component";

//zie : https://spring.io/guides/tutorials/spring-security-and-angular-js/

@Injectable()
export class AuthenticationService {

  authenticated = false;
  httpHeaders : HttpHeaders;
  userRole : string;
  userName : string;

  constructor(private httpClient : HttpClient) {
  }

    authenticate(credentials, callback) {

         this.httpHeaders = new HttpHeaders(credentials ? {
            authorization : 'Basic ' + btoa(credentials.useremail + ':' + credentials.userpassword)
        } : {});

        this.httpClient.get(AppComponent.API_ROOT_LOGIN).subscribe(response => {
            if (response['authenticated']
                && response['principal']['accountNonExpired']
                && response['principal']['accountNonLocked']
                && response['principal']['credentialsNonExpired']
                && response['principal']['enabled']
                ) {
                this.userName = response['name'];
                if (response['authorities'].length == 1) {
                    this.userRole = response['authorities'][0]['authority'];
                }
                console.log("userRole = " + this.userRole);
                this.authenticated = true;
            } else {
                this.authenticated = false;
            }
            return callback && callback();
            },
            (error => {console.log("Errorresponse from api/user/login = " + JSON.stringify(error));
                       confirm("Invalid Username/Password !");}
            )
        );
    }
}
