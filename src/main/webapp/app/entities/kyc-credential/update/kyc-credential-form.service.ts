import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IKycCredential, NewKycCredential } from '../kyc-credential.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IKycCredential for edit and NewKycCredentialFormGroupInput for create.
 */
type KycCredentialFormGroupInput = IKycCredential | PartialWithRequiredKeyOf<NewKycCredential>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IKycCredential | NewKycCredential> = Omit<T, 'issuedDate' | 'expiryDate'> & {
  issuedDate?: string | null;
  expiryDate?: string | null;
};

type KycCredentialFormRawValue = FormValueOf<IKycCredential>;

type NewKycCredentialFormRawValue = FormValueOf<NewKycCredential>;

type KycCredentialFormDefaults = Pick<NewKycCredential, 'id' | 'issuedDate' | 'expiryDate'>;

type KycCredentialFormGroupContent = {
  id: FormControl<KycCredentialFormRawValue['id'] | NewKycCredential['id']>;
  issuedDate: FormControl<KycCredentialFormRawValue['issuedDate']>;
  expiryDate: FormControl<KycCredentialFormRawValue['expiryDate']>;
  status: FormControl<KycCredentialFormRawValue['status']>;
  levelOfVerification: FormControl<KycCredentialFormRawValue['levelOfVerification']>;
  schemaVersion: FormControl<KycCredentialFormRawValue['schemaVersion']>;
  credentialHash: FormControl<KycCredentialFormRawValue['credentialHash']>;
  signature: FormControl<KycCredentialFormRawValue['signature']>;
  issuer: FormControl<KycCredentialFormRawValue['issuer']>;
  customer: FormControl<KycCredentialFormRawValue['customer']>;
};

export type KycCredentialFormGroup = FormGroup<KycCredentialFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class KycCredentialFormService {
  createKycCredentialFormGroup(kycCredential: KycCredentialFormGroupInput = { id: null }): KycCredentialFormGroup {
    const kycCredentialRawValue = this.convertKycCredentialToKycCredentialRawValue({
      ...this.getFormDefaults(),
      ...kycCredential,
    });
    return new FormGroup<KycCredentialFormGroupContent>({
      id: new FormControl(
        { value: kycCredentialRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      issuedDate: new FormControl(kycCredentialRawValue.issuedDate, {
        validators: [Validators.required],
      }),
      expiryDate: new FormControl(kycCredentialRawValue.expiryDate, {
        validators: [Validators.required],
      }),
      status: new FormControl(kycCredentialRawValue.status, {
        validators: [Validators.required],
      }),
      levelOfVerification: new FormControl(kycCredentialRawValue.levelOfVerification),
      schemaVersion: new FormControl(kycCredentialRawValue.schemaVersion),
      credentialHash: new FormControl(kycCredentialRawValue.credentialHash, {
        validators: [Validators.required],
      }),
      signature: new FormControl(kycCredentialRawValue.signature, {
        validators: [Validators.required],
      }),
      issuer: new FormControl(kycCredentialRawValue.issuer),
      customer: new FormControl(kycCredentialRawValue.customer),
    });
  }

  getKycCredential(form: KycCredentialFormGroup): IKycCredential | NewKycCredential {
    return this.convertKycCredentialRawValueToKycCredential(form.getRawValue() as KycCredentialFormRawValue | NewKycCredentialFormRawValue);
  }

  resetForm(form: KycCredentialFormGroup, kycCredential: KycCredentialFormGroupInput): void {
    const kycCredentialRawValue = this.convertKycCredentialToKycCredentialRawValue({ ...this.getFormDefaults(), ...kycCredential });
    form.reset(
      {
        ...kycCredentialRawValue,
        id: { value: kycCredentialRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): KycCredentialFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      issuedDate: currentTime,
      expiryDate: currentTime,
    };
  }

  private convertKycCredentialRawValueToKycCredential(
    rawKycCredential: KycCredentialFormRawValue | NewKycCredentialFormRawValue,
  ): IKycCredential | NewKycCredential {
    return {
      ...rawKycCredential,
      issuedDate: dayjs(rawKycCredential.issuedDate, DATE_TIME_FORMAT),
      expiryDate: dayjs(rawKycCredential.expiryDate, DATE_TIME_FORMAT),
    };
  }

  private convertKycCredentialToKycCredentialRawValue(
    kycCredential: IKycCredential | (Partial<NewKycCredential> & KycCredentialFormDefaults),
  ): KycCredentialFormRawValue | PartialWithRequiredKeyOf<NewKycCredentialFormRawValue> {
    return {
      ...kycCredential,
      issuedDate: kycCredential.issuedDate ? kycCredential.issuedDate.format(DATE_TIME_FORMAT) : undefined,
      expiryDate: kycCredential.expiryDate ? kycCredential.expiryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
