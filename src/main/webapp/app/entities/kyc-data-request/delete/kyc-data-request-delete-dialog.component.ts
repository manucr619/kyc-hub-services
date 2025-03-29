import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IKycDataRequest } from '../kyc-data-request.model';
import { KycDataRequestService } from '../service/kyc-data-request.service';

@Component({
  templateUrl: './kyc-data-request-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class KycDataRequestDeleteDialogComponent {
  kycDataRequest?: IKycDataRequest;

  protected kycDataRequestService = inject(KycDataRequestService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.kycDataRequestService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
