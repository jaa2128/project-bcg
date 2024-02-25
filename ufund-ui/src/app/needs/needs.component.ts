import { Component } from '@angular/core';

@Component({
  selector: 'app-needs',
  templateUrl: './needs.component.html',
  styleUrl: './needs.component.css'
})

export class NeedsComponent implements OnInit {

  needs: Need[] = [];
  
  constructor(private needService: NeedService) { }

  getNeeds(): void {
    this.needService.getNeeds()
        .subscribe(needs => this.needs = needs);
  }

  ngOnInit(): void {
    this.getNeeds();
  }

}
