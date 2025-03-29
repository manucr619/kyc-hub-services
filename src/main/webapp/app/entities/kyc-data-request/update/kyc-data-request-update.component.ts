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
import { IKycConsent } from 'app/entities/kyc-consent/kyc-consent.model';
import { KycConsentService } from 'app/entities/kyc-consent/service/kyc-consent.service';
import { RequestStatus } from 'app/entities/enumerations/request-status.model';
import { KycDataRequestService } from '../service/kyc-data-request.service';
import { IKycDataRequest } from '../kyc-data-request.model';
import { KycDataRequestFormGroup, KycDataRequestFormService } from './kyc-data-request-form.service';

@Component({
  selector: 'jhi-kyc-data-request-update',
  templateUrl: './kyc-data-request-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class KycDataRequestUpdateComponent implements OnInit {
  isSaving = false;
  kycDataRequest: IKycDataRequest | null = null;
  requestStatusValues = Object.keys(RequestStatus);

  customersSharedCollection: ICustomer[] = [];
  banksSharedCollection: IBank[] = [];
  kycConsentsSharedCollection: IKycConsent[] = [];

  protected kycDataRequestService = inject(KycDataRequestService);
  protected kycDataRequestFormService = inject(KycDataRequestFormService);
  protected customerService = inject(CustomerService);
  protected bankService = inject(BankService);
  protected kycConsentService = inject(KycConsentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: KycDataRequestFormGroup = this.kycDataRequestFormService.createKycDataRequestFormGroup();

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareBank = (o1: IBank | null, o2: IBank | null): boolean => this.bankService.compareBank(o1, o2);

  compareKycConsent = (o1: IKycConsent | null, o2: IKycConsent | null): boolean => this.kycConsentService.compareKycConsent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kycDataRequest }) => {
      this.kycDataRequest = kycDataRequest;
      if (kycDataRequest) {
        this.updateForm(kycDataRequest);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const kycDataRequest = this.kycDataRequestFormService.getKycDataRequest(this.editForm);
    if (kycDataRequest.id !== null) {
      this.subscribeToSaveResponse(this.kycDataRequestService.update(kycDataRequest));
    } else {
      this.subscribeToSaveResponse(this.kycDataRequestService.create(kycDataRequest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKycDataRequest>>): void {
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

  protected updateForm(kycDataRequest: IKycDataRequest): void {
    this.kycDataRequest = kycDataRequest;
    this.kycDataRequestFormService.resetForm(this.editForm, kycDataRequest);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      kycDataRequest.customer,
    );
    this.banksSharedCollection = this.bankService.addBankToCollectionIfMissing<IBank>(
      this.banksSharedCollection,
      kycDataRequest.fromBank,
      kycDataRequest.toBank,
    );
    this.kycConsentsSharedCollection = this.kycConsentService.addKycConsentToCollectionIfMissing<IKycConsent>(
      this.kycConsentsSharedCollection,
      kycDataRequest.consent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.kycDataRequest?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));

    this.bankService
      .query()
      .pipe(map((res: HttpResponse<IBank[]>) => res.body ?? []))
      .pipe(
        map((banks: IBank[]) =>
          this.bankService.addBankToCollectionIfMissing<IBank>(banks, this.kycDataRequest?.fromBank, this.kycDataRequest?.toBank),
        ),
      )
      .subscribe((banks: IBank[]) => (this.banksSharedCollection = banks));

    this.kycConsentService
      .query()
      .pipe(map((res: HttpResponse<IKycConsent[]>) => res.body ?? []))
      .pipe(
        map((kycConsents: IKycConsent[]) =>
          this.kycConsentService.addKycConsentToCollectionIfMissing<IKycConsent>(kycConsents, this.kycDataRequest?.consent),
        ),
      )
      .subscribe((kycConsents: IKycConsent[]) => (this.kycConsentsSharedCollection = kycConsents));
  }
}
