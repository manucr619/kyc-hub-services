import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IKycCredential } from '../kyc-credential.model';

@Component({
  selector: 'jhi-kyc-credential-detail',
  templateUrl: './kyc-credential-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class KycCredentialDetailComponent {
  kycCredential = input<IKycCredential | null>(null);

  previousState(): void {
    window.history.back();
  }
}
