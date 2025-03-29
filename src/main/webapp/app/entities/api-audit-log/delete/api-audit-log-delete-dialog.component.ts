import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAPIAuditLog } from '../api-audit-log.model';
import { APIAuditLogService } from '../service/api-audit-log.service';

@Component({
  templateUrl: './api-audit-log-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class APIAuditLogDeleteDialogComponent {
  aPIAuditLog?: IAPIAuditLog;

  protected aPIAuditLogService = inject(APIAuditLogService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aPIAuditLogService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
