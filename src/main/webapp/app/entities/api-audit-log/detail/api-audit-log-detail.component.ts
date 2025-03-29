import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IAPIAuditLog } from '../api-audit-log.model';

@Component({
  selector: 'jhi-api-audit-log-detail',
  templateUrl: './api-audit-log-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class APIAuditLogDetailComponent {
  aPIAuditLog = input<IAPIAuditLog | null>(null);

  previousState(): void {
    window.history.back();
  }
}
