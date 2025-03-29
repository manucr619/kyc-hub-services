import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IBank } from 'app/entities/bank/bank.model';
import { BankService } from 'app/entities/bank/service/bank.service';
import { TransactionStatus } from 'app/entities/enumerations/transaction-status.model';
import { TransactionService } from '../service/transaction.service';
import { ITransaction } from '../transaction.model';
import { TransactionFormGroup, TransactionFormService } from './transaction-form.service';

@Component({
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;
  transaction: ITransaction | null = null;
  transactionStatusValues = Object.keys(TransactionStatus);

  customersSharedCollection: ICustomer[] = [];
  banksSharedCollection: IBank[] = [];

  protected transactionService = inject(TransactionService);
  protected transactionFormService = inject(TransactionFormService);
  protected customerService = inject(CustomerService);
  protected bankService = inject(BankService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransactionFormGroup = this.transactionFormService.createTransactionFormGroup();

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareBank = (o1: IBank | null, o2: IBank | null): boolean => this.bankService.compareBank(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.transaction = transaction;
      if (transaction) {
        this.updateForm(transaction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaction = this.transactionFormService.getTransaction(this.editForm);
    if (transaction.id !== null) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
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

  protected updateForm(transaction: ITransaction): void {
    this.transaction = transaction;
    this.transactionFormService.resetForm(this.editForm, transaction);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      transaction.originator,
      transaction.beneficiary,
    );
    this.banksSharedCollection = this.bankService.addBankToCollectionIfMissing<IBank>(
      this.banksSharedCollection,
      transaction.originatorBank,
      transaction.beneficiaryBank,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
            customers,
            this.transaction?.originator,
            this.transaction?.beneficiary,
          ),
        ),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));

    this.bankService
      .query()
      .pipe(map((res: HttpResponse<IBank[]>) => res.body ?? []))
      .pipe(
        map((banks: IBank[]) =>
          this.bankService.addBankToCollectionIfMissing<IBank>(banks, this.transaction?.originatorBank, this.transaction?.beneficiaryBank),
        ),
      )
      .subscribe((banks: IBank[]) => (this.banksSharedCollection = banks));
  }
}
