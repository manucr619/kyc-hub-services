import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICountryRegulation } from '../country-regulation.model';

@Component({
  selector: 'jhi-country-regulation-detail',
  templateUrl: './country-regulation-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CountryRegulationDetailComponent {
  countryRegulation = input<ICountryRegulation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
