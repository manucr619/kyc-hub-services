import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IKycDataRequest, NewKycDataRequest } from '../kyc-data-request.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IKycDataRequest for edit and NewKycDataRequestFormGroupInput for create.
 */
type KycDataRequestFormGroupInput = IKycDataRequest | PartialWithRequiredKeyOf<NewKycDataRequest>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IKycDataRequest | NewKycDataRequest> = Omit<T, 'requestDate' | 'respondedAt'> & {
  requestDate?: string | null;
  respondedAt?: string | null;
};

type KycDataRequestFormRawValue = FormValueOf<IKycDataRequest>;

type NewKycDataRequestFormRawValue = FormValueOf<NewKycDataRequest>;

type KycDataRequestFormDefaults = Pick<NewKycDataRequest, 'id' | 'requestDate' | 'respondedAt'>;

type KycDataRequestFormGroupContent = {
  id: FormControl<KycDataRequestFormRawValue['id'] | NewKycDataRequest['id']>;
  requestRef: FormControl<KycDataRequestFormRawValue['requestRef']>;
  requestDate: FormControl<KycDataRequestFormRawValue['requestDate']>;
  status: FormControl<KycDataRequestFormRawValue['status']>;
  requestPurpose: FormControl<KycDataRequestFormRawValue['requestPurpose']>;
  encryptedResponseData: FormControl<KycDataRequestFormRawValue['encryptedResponseData']>;
  respondedAt: FormControl<KycDataRequestFormRawValue['respondedAt']>;
  customer: FormControl<KycDataRequestFormRawValue['customer']>;
  fromBank: FormControl<KycDataRequestFormRawValue['fromBank']>;
  toBank: FormControl<KycDataRequestFormRawValue['toBank']>;
  consent: FormControl<KycDataRequestFormRawValue['consent']>;
};

export type KycDataRequestFormGroup = FormGroup<KycDataRequestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class KycDataRequestFormService {
  createKycDataRequestFormGroup(kycDataRequest: KycDataRequestFormGroupInput = { id: null }): KycDataRequestFormGroup {
    const kycDataRequestRawValue = this.convertKycDataRequestToKycDataRequestRawValue({
      ...this.getFormDefaults(),
      ...kycDataRequest,
    });
    return new FormGroup<KycDataRequestFormGroupContent>({
      id: new FormControl(
        { value: kycDataRequestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      requestRef: new FormControl(kycDataRequestRawValue.requestRef, {
        validators: [Validators.required],
      }),
      requestDate: new FormControl(kycDataRequestRawValue.requestDate, {
        validators: [Validators.required],
      }),
      status: new FormControl(kycDataRequestRawValue.status, {
        validators: [Validators.required],
      }),
      requestPurpose: new FormControl(kycDataRequestRawValue.requestPurpose),
      encryptedResponseData: new FormControl(kycDataRequestRawValue.encryptedResponseData),
      respondedAt: new FormControl(kycDataRequestRawValue.respondedAt),
      customer: new FormControl(kycDataRequestRawValue.customer),
      fromBank: new FormControl(kycDataRequestRawValue.fromBank),
      toBank: new FormControl(kycDataRequestRawValue.toBank),
      consent: new FormControl(kycDataRequestRawValue.consent),
    });
  }

  getKycDataRequest(form: KycDataRequestFormGroup): IKycDataRequest | NewKycDataRequest {
    return this.convertKycDataRequestRawValueToKycDataRequest(
      form.getRawValue() as KycDataRequestFormRawValue | NewKycDataRequestFormRawValue,
    );
  }

  resetForm(form: KycDataRequestFormGroup, kycDataRequest: KycDataRequestFormGroupInput): void {
    const kycDataRequestRawValue = this.convertKycDataRequestToKycDataRequestRawValue({ ...this.getFormDefaults(), ...kycDataRequest });
    form.reset(
      {
        ...kycDataRequestRawValue,
        id: { value: kycDataRequestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): KycDataRequestFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      requestDate: currentTime,
      respondedAt: currentTime,
    };
  }

  private convertKycDataRequestRawValueToKycDataRequest(
    rawKycDataRequest: KycDataRequestFormRawValue | NewKycDataRequestFormRawValue,
  ): IKycDataRequest | NewKycDataRequest {
    return {
      ...rawKycDataRequest,
      requestDate: dayjs(rawKycDataRequest.requestDate, DATE_TIME_FORMAT),
      respondedAt: dayjs(rawKycDataRequest.respondedAt, DATE_TIME_FORMAT),
    };
  }

  private convertKycDataRequestToKycDataRequestRawValue(
    kycDataRequest: IKycDataRequest | (Partial<NewKycDataRequest> & KycDataRequestFormDefaults),
  ): KycDataRequestFormRawValue | PartialWithRequiredKeyOf<NewKycDataRequestFormRawValue> {
    return {
      ...kycDataRequest,
      requestDate: kycDataRequest.requestDate ? kycDataRequest.requestDate.format(DATE_TIME_FORMAT) : undefined,
      respondedAt: kycDataRequest.respondedAt ? kycDataRequest.respondedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
