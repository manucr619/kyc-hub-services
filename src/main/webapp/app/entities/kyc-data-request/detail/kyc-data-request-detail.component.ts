import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IKycDataRequest } from '../kyc-data-request.model';

@Component({
  selector: 'jhi-kyc-data-request-detail',
  templateUrl: './kyc-data-request-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class KycDataRequestDetailComponent {
  kycDataRequest = input<IKycDataRequest | null>(null);

  previousState(): void {
    window.history.back();
  }
}
