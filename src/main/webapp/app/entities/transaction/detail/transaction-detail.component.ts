import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ITransaction } from '../transaction.model';

@Component({
  selector: 'jhi-transaction-detail',
  templateUrl: './transaction-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class TransactionDetailComponent {
  transaction = input<ITransaction | null>(null);

  previousState(): void {
    window.history.back();
  }
}
