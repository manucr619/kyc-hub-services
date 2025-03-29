import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IKycConsent } from '../kyc-consent.model';

@Component({
  selector: 'jhi-kyc-consent-detail',
  templateUrl: './kyc-consent-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class KycConsentDetailComponent {
  kycConsent = input<IKycConsent | null>(null);

  previousState(): void {
    window.history.back();
  }
}
