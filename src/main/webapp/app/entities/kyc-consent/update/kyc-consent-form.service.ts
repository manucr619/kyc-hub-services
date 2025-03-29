import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IKycConsent, NewKycConsent } from '../kyc-consent.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IKycConsent for edit and NewKycConsentFormGroupInput for create.
 */
type KycConsentFormGroupInput = IKycConsent | PartialWithRequiredKeyOf<NewKycConsent>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IKycConsent | NewKycConsent> = Omit<T, 'consentGivenAt' | 'expiresAt' | 'revokedAt'> & {
  consentGivenAt?: string | null;
  expiresAt?: string | null;
  revokedAt?: string | null;
};

type KycConsentFormRawValue = FormValueOf<IKycConsent>;

type NewKycConsentFormRawValue = FormValueOf<NewKycConsent>;

type KycConsentFormDefaults = Pick<NewKycConsent, 'id' | 'consentGivenAt' | 'expiresAt' | 'isRevoked' | 'revokedAt'>;

type KycConsentFormGroupContent = {
  id: FormControl<KycConsentFormRawValue['id'] | NewKycConsent['id']>;
  consentToken: FormControl<KycConsentFormRawValue['consentToken']>;
  consentGivenAt: FormControl<KycConsentFormRawValue['consentGivenAt']>;
  expiresAt: FormControl<KycConsentFormRawValue['expiresAt']>;
  isRevoked: FormControl<KycConsentFormRawValue['isRevoked']>;
  revokedAt: FormControl<KycConsentFormRawValue['revokedAt']>;
  customer: FormControl<KycConsentFormRawValue['customer']>;
  issuerBank: FormControl<KycConsentFormRawValue['issuerBank']>;
  recipientBank: FormControl<KycConsentFormRawValue['recipientBank']>;
};

export type KycConsentFormGroup = FormGroup<KycConsentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class KycConsentFormService {
  createKycConsentFormGroup(kycConsent: KycConsentFormGroupInput = { id: null }): KycConsentFormGroup {
    const kycConsentRawValue = this.convertKycConsentToKycConsentRawValue({
      ...this.getFormDefaults(),
      ...kycConsent,
    });
    return new FormGroup<KycConsentFormGroupContent>({
      id: new FormControl(
        { value: kycConsentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      consentToken: new FormControl(kycConsentRawValue.consentToken, {
        validators: [Validators.required],
      }),
      consentGivenAt: new FormControl(kycConsentRawValue.consentGivenAt, {
        validators: [Validators.required],
      }),
      expiresAt: new FormControl(kycConsentRawValue.expiresAt),
      isRevoked: new FormControl(kycConsentRawValue.isRevoked),
      revokedAt: new FormControl(kycConsentRawValue.revokedAt),
      customer: new FormControl(kycConsentRawValue.customer),
      issuerBank: new FormControl(kycConsentRawValue.issuerBank),
      recipientBank: new FormControl(kycConsentRawValue.recipientBank),
    });
  }

  getKycConsent(form: KycConsentFormGroup): IKycConsent | NewKycConsent {
    return this.convertKycConsentRawValueToKycConsent(form.getRawValue() as KycConsentFormRawValue | NewKycConsentFormRawValue);
  }

  resetForm(form: KycConsentFormGroup, kycConsent: KycConsentFormGroupInput): void {
    const kycConsentRawValue = this.convertKycConsentToKycConsentRawValue({ ...this.getFormDefaults(), ...kycConsent });
    form.reset(
      {
        ...kycConsentRawValue,
        id: { value: kycConsentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): KycConsentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      consentGivenAt: currentTime,
      expiresAt: currentTime,
      isRevoked: false,
      revokedAt: currentTime,
    };
  }

  private convertKycConsentRawValueToKycConsent(
    rawKycConsent: KycConsentFormRawValue | NewKycConsentFormRawValue,
  ): IKycConsent | NewKycConsent {
    return {
      ...rawKycConsent,
      consentGivenAt: dayjs(rawKycConsent.consentGivenAt, DATE_TIME_FORMAT),
      expiresAt: dayjs(rawKycConsent.expiresAt, DATE_TIME_FORMAT),
      revokedAt: dayjs(rawKycConsent.revokedAt, DATE_TIME_FORMAT),
    };
  }

  private convertKycConsentToKycConsentRawValue(
    kycConsent: IKycConsent | (Partial<NewKycConsent> & KycConsentFormDefaults),
  ): KycConsentFormRawValue | PartialWithRequiredKeyOf<NewKycConsentFormRawValue> {
    return {
      ...kycConsent,
      consentGivenAt: kycConsent.consentGivenAt ? kycConsent.consentGivenAt.format(DATE_TIME_FORMAT) : undefined,
      expiresAt: kycConsent.expiresAt ? kycConsent.expiresAt.format(DATE_TIME_FORMAT) : undefined,
      revokedAt: kycConsent.revokedAt ? kycConsent.revokedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
