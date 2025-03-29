import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBank } from 'app/entities/bank/bank.model';
import { BankService } from 'app/entities/bank/service/bank.service';
import { IAPIAuditLog } from '../api-audit-log.model';
import { APIAuditLogService } from '../service/api-audit-log.service';
import { APIAuditLogFormGroup, APIAuditLogFormService } from './api-audit-log-form.service';

@Component({
  selector: 'jhi-api-audit-log-update',
  templateUrl: './api-audit-log-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class APIAuditLogUpdateComponent implements OnInit {
  isSaving = false;
  aPIAuditLog: IAPIAuditLog | null = null;

  banksSharedCollection: IBank[] = [];

  protected aPIAuditLogService = inject(APIAuditLogService);
  protected aPIAuditLogFormService = inject(APIAuditLogFormService);
  protected bankService = inject(BankService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: APIAuditLogFormGroup = this.aPIAuditLogFormService.createAPIAuditLogFormGroup();

  compareBank = (o1: IBank | null, o2: IBank | null): boolean => this.bankService.compareBank(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPIAuditLog }) => {
      this.aPIAuditLog = aPIAuditLog;
      if (aPIAuditLog) {
        this.updateForm(aPIAuditLog);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aPIAuditLog = this.aPIAuditLogFormService.getAPIAuditLog(this.editForm);
    if (aPIAuditLog.id !== null) {
      this.subscribeToSaveResponse(this.aPIAuditLogService.update(aPIAuditLog));
    } else {
      this.subscribeToSaveResponse(this.aPIAuditLogService.create(aPIAuditLog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAPIAuditLog>>): void {
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

  protected updateForm(aPIAuditLog: IAPIAuditLog): void {
    this.aPIAuditLog = aPIAuditLog;
    this.aPIAuditLogFormService.resetForm(this.editForm, aPIAuditLog);

    this.banksSharedCollection = this.bankService.addBankToCollectionIfMissing<IBank>(this.banksSharedCollection, aPIAuditLog.bank);
  }

  protected loadRelationshipsOptions(): void {
    this.bankService
      .query()
      .pipe(map((res: HttpResponse<IBank[]>) => res.body ?? []))
      .pipe(map((banks: IBank[]) => this.bankService.addBankToCollectionIfMissing<IBank>(banks, this.aPIAuditLog?.bank)))
      .subscribe((banks: IBank[]) => (this.banksSharedCollection = banks));
  }
}
