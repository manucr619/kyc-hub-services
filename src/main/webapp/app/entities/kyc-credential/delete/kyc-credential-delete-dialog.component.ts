import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IKycCredential } from '../kyc-credential.model';
import { KycCredentialService } from '../service/kyc-credential.service';

@Component({
  templateUrl: './kyc-credential-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class KycCredentialDeleteDialogComponent {
  kycCredential?: IKycCredential;

  protected kycCredentialService = inject(KycCredentialService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.kycCredentialService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
