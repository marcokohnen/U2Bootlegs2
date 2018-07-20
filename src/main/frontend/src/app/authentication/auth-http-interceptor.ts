import {HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {AuthenticationService} from "./authentication.service";
import {Injectable} from "@angular/core";


// An HTTP interceptor is just an Angular service implementing a specific interface, the HttpInterceptor.
// this interface has only one method to override : intercept(req: HttpRequest<any>, next: HttpHandler)
// zie : https://angular.io/guide/http#intercepting-requests-and-responses
@Injectable()
export class AuthHttpInterceptor implements HttpInterceptor {

    constructor(private authService: AuthenticationService) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler) {
        // get header with credentials from AuthenticationService
        const httpHeaderWithCredentials = this.authService.httpHeaders;

        // Clone the request and replace the original headers with cloned headers, updated with the authorization.
        const modifiedReq = req.clone({
            setHeaders: {
                'X-Requested-With': 'XMLHttpRequest',
                'authorization': httpHeaderWithCredentials.get('authorization')
            }
        });
        return next.handle(modifiedReq);
    }
}
