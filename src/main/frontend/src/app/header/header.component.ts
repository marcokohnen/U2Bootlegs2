import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService, UserRoleEnum} from "../authentication/authentication.service";
import {HttpClient} from "@angular/common/http";
import "rxjs/add/operator/finally";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {

    credentials = {
        useremail: '',
        userpassword: ''
    };

    constructor(private router: Router, private authService: AuthenticationService, private httpClient: HttpClient) {
    }

    ngOnInit() {
    }

    authenticated(): boolean {
        return this.authService.authenticated;
    }

    logout() {
        //send logout request to back-end
        this.httpClient.post('/logout', {}, {headers : this.authService.httpHeaders}).finally(() => {
            this.authService.authenticated = false;
            this.credentials.useremail = '';
            this.credentials.userpassword = '';
            this.router.navigate(['home']);
        }).subscribe();
    }

    login() {
        this.authService.authenticate(this.credentials, () => {this.router.navigateByUrl('home')});
        return false;
    }

    getUserName(): string {
        return this.authService.userName;
    }

    onSearchSubmit(searchValue: string, searchParam: string) {
        if (searchValue.length > 0 && searchParam.length > 0) {
            //console.log("searchValue =    " + searchValue);
            //console.log("searchParam =    " + searchParam);
            this.router.navigate(['searchconcert', searchParam, searchValue]);
        }
    };
}
