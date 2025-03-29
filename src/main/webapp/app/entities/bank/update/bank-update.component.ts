import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBank } from '../bank.model';
import { BankService } from '../service/bank.service';
import { BankFormGroup, BankFormService } from './bank-form.service';

@Component({
  selector: 'jhi-bank-update',
  templateUrl: './bank-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BankUpdateComponent implements OnInit {
  isSaving = false;
  bank: IBank | null = null;

  protected bankService = inject(BankService);
  protected bankFormService = inject(BankFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BankFormGroup = this.bankFormService.createBankFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bank }) => {
      this.bank = bank;
      if (bank) {
        this.updateForm(bank);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bank = this.bankFormService.getBank(this.editForm);
    if (bank.id !== null) {
      this.subscribeToSaveResponse(this.bankService.update(bank));
    } else {
      this.subscribeToSaveResponse(this.bankService.create(bank));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBank>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(bank: IBank): void {
    this.bank = bank;
    this.bankFormService.resetForm(this.editForm, bank);
  }
}
