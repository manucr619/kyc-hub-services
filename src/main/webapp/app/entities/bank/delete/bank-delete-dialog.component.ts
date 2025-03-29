import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBank } from '../bank.model';
import { BankService } from '../service/bank.service';

@Component({
  templateUrl: './bank-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BankDeleteDialogComponent {
  bank?: IBank;

  protected bankService = inject(BankService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bankService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
