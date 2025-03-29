import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICountryRegulation } from '../country-regulation.model';
import { CountryRegulationService } from '../service/country-regulation.service';

@Component({
  templateUrl: './country-regulation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CountryRegulationDeleteDialogComponent {
  countryRegulation?: ICountryRegulation;

  protected countryRegulationService = inject(CountryRegulationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.countryRegulationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
