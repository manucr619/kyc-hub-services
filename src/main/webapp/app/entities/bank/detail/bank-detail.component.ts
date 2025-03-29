import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IBank } from '../bank.model';

@Component({
  selector: 'jhi-bank-detail',
  templateUrl: './bank-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class BankDetailComponent {
  bank = input<IBank | null>(null);

  previousState(): void {
    window.history.back();
  }
}
