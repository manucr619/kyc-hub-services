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
import { KycConsentService } from '../service/kyc-consent.service';
import { IKycConsent } from '../kyc-consent.model';
import { KycConsentFormGroup, KycConsentFormService } from './kyc-consent-form.service';

@Component({
  selector: 'jhi-kyc-consent-update',
  templateUrl: './kyc-consent-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class KycConsentUpdateComponent implements OnInit {
  isSaving = false;
  kycConsent: IKycConsent | null = null;

  customersSharedCollection: ICustomer[] = [];
  banksSharedCollection: IBank[] = [];

  protected kycConsentService = inject(KycConsentService);
  protected kycConsentFormService = inject(KycConsentFormService);
  protected customerService = inject(CustomerService);
  protected bankService = inject(BankService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: KycConsentFormGroup = this.kycConsentFormService.createKycConsentFormGroup();

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareBank = (o1: IBank | null, o2: IBank | null): boolean => this.bankService.compareBank(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kycConsent }) => {
      this.kycConsent = kycConsent;
      if (kycConsent) {
        this.updateForm(kycConsent);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const kycConsent = this.kycConsentFormService.getKycConsent(this.editForm);
    if (kycConsent.id !== null) {
      this.subscribeToSaveResponse(this.kycConsentService.update(kycConsent));
    } else {
      this.subscribeToSaveResponse(this.kycConsentService.create(kycConsent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKycConsent>>): void {
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

  protected updateForm(kycConsent: IKycConsent): void {
    this.kycConsent = kycConsent;
    this.kycConsentFormService.resetForm(this.editForm, kycConsent);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      kycConsent.customer,
    );
    this.banksSharedCollection = this.bankService.addBankToCollectionIfMissing<IBank>(
      this.banksSharedCollection,
      kycConsent.issuerBank,
      kycConsent.recipientBank,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.kycConsent?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));

    this.bankService
      .query()
      .pipe(map((res: HttpResponse<IBank[]>) => res.body ?? []))
      .pipe(
        map((banks: IBank[]) =>
          this.bankService.addBankToCollectionIfMissing<IBank>(banks, this.kycConsent?.issuerBank, this.kycConsent?.recipientBank),
        ),
      )
      .subscribe((banks: IBank[]) => (this.banksSharedCollection = banks));
  }
}
