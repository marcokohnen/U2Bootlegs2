import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

    @ViewChild("searchForm") private searchForm : NgForm;

    //searchValue : string = "vhgshfhsfhs";

  constructor(private router : Router) {}

  ngOnInit() {
  }

    // navHome() {
    //   this.router.navigate(['home']);
    // }
    //
    // navListTour() {
    //     //let activTourList = localStorage.getItem("tourList");
    //     this.router.navigate(['listtour']);
    // }
    onSearchSubmit(searchValue : string, searchParam : string) {
      if (searchValue.length > 0 && searchParam.length > 0){
          console.log("searchValue =    " + searchValue);
          console.log("searchParam =    " + searchParam);
          this.router.navigate(['searchconcert', searchParam, searchValue]);
       }
    };
}
