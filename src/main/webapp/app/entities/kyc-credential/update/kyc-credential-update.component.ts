import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBank } from 'app/entities/bank/bank.model';
import { BankService } from 'app/entities/bank/service/bank.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { CredentialStatus } from 'app/entities/enumerations/credential-status.model';
import { KycCredentialService } from '../service/kyc-credential.service';
import { IKycCredential } from '../kyc-credential.model';
import { KycCredentialFormGroup, KycCredentialFormService } from './kyc-credential-form.service';

@Component({
  selector: 'jhi-kyc-credential-update',
  templateUrl: './kyc-credential-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class KycCredentialUpdateComponent implements OnInit {
  isSaving = false;
  kycCredential: IKycCredential | null = null;
  credentialStatusValues = Object.keys(CredentialStatus);

  banksSharedCollection: IBank[] = [];
  customersSharedCollection: ICustomer[] = [];

  protected kycCredentialService = inject(KycCredentialService);
  protected kycCredentialFormService = inject(KycCredentialFormService);
  protected bankService = inject(BankService);
  protected customerService = inject(CustomerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: KycCredentialFormGroup = this.kycCredentialFormService.createKycCredentialFormGroup();

  compareBank = (o1: IBank | null, o2: IBank | null): boolean => this.bankService.compareBank(o1, o2);

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kycCredential }) => {
      this.kycCredential = kycCredential;
      if (kycCredential) {
        this.updateForm(kycCredential);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const kycCredential = this.kycCredentialFormService.getKycCredential(this.editForm);
    if (kycCredential.id !== null) {
      this.subscribeToSaveResponse(this.kycCredentialService.update(kycCredential));
    } else {
      this.subscribeToSaveResponse(this.kycCredentialService.create(kycCredential));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKycCredential>>): void {
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

  protected updateForm(kycCredential: IKycCredential): void {
    this.kycCredential = kycCredential;
    this.kycCredentialFormService.resetForm(this.editForm, kycCredential);

    this.banksSharedCollection = this.bankService.addBankToCollectionIfMissing<IBank>(this.banksSharedCollection, kycCredential.issuer);
    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      kycCredential.customer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bankService
      .query()
      .pipe(map((res: HttpResponse<IBank[]>) => res.body ?? []))
      .pipe(map((banks: IBank[]) => this.bankService.addBankToCollectionIfMissing<IBank>(banks, this.kycCredential?.issuer)))
      .subscribe((banks: IBank[]) => (this.banksSharedCollection = banks));

    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.kycCredential?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }
}
