import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Need } from '../need'; 
import { NeedsComponent } from '../needs/needs.component';
import { NeedService } from '../need.service';

@Component({
  selector: 'app-need-detail',
  templateUrl: './need-detail.component.html',
  styleUrl: './need-detail.component.css'
})
export class NeedDetailComponent implements OnInit {
  
  need!: Need;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private needService: NeedService
  ) {}

  ngOnInit(): void {
    this.getNeed();
  }

  getNeed(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.needService.getNeed(id)
      .subscribe(need => this.need = need);
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.need) {
    this.needService.updateNeed(this.need)
        .subscribe(() => this.goBack());
    }
  }

}
