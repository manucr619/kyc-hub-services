import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAPIAuditLog, NewAPIAuditLog } from '../api-audit-log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAPIAuditLog for edit and NewAPIAuditLogFormGroupInput for create.
 */
type APIAuditLogFormGroupInput = IAPIAuditLog | PartialWithRequiredKeyOf<NewAPIAuditLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAPIAuditLog | NewAPIAuditLog> = Omit<T, 'eventTime'> & {
  eventTime?: string | null;
};

type APIAuditLogFormRawValue = FormValueOf<IAPIAuditLog>;

type NewAPIAuditLogFormRawValue = FormValueOf<NewAPIAuditLog>;

type APIAuditLogFormDefaults = Pick<NewAPIAuditLog, 'id' | 'eventTime'>;

type APIAuditLogFormGroupContent = {
  id: FormControl<APIAuditLogFormRawValue['id'] | NewAPIAuditLog['id']>;
  eventTime: FormControl<APIAuditLogFormRawValue['eventTime']>;
  action: FormControl<APIAuditLogFormRawValue['action']>;
  statusCode: FormControl<APIAuditLogFormRawValue['statusCode']>;
  message: FormControl<APIAuditLogFormRawValue['message']>;
  sourceIP: FormControl<APIAuditLogFormRawValue['sourceIP']>;
  initiatedBy: FormControl<APIAuditLogFormRawValue['initiatedBy']>;
  endpointAccessed: FormControl<APIAuditLogFormRawValue['endpointAccessed']>;
  bank: FormControl<APIAuditLogFormRawValue['bank']>;
};

export type APIAuditLogFormGroup = FormGroup<APIAuditLogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class APIAuditLogFormService {
  createAPIAuditLogFormGroup(aPIAuditLog: APIAuditLogFormGroupInput = { id: null }): APIAuditLogFormGroup {
    const aPIAuditLogRawValue = this.convertAPIAuditLogToAPIAuditLogRawValue({
      ...this.getFormDefaults(),
      ...aPIAuditLog,
    });
    return new FormGroup<APIAuditLogFormGroupContent>({
      id: new FormControl(
        { value: aPIAuditLogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      eventTime: new FormControl(aPIAuditLogRawValue.eventTime, {
        validators: [Validators.required],
      }),
      action: new FormControl(aPIAuditLogRawValue.action, {
        validators: [Validators.required],
      }),
      statusCode: new FormControl(aPIAuditLogRawValue.statusCode),
      message: new FormControl(aPIAuditLogRawValue.message),
      sourceIP: new FormControl(aPIAuditLogRawValue.sourceIP),
      initiatedBy: new FormControl(aPIAuditLogRawValue.initiatedBy),
      endpointAccessed: new FormControl(aPIAuditLogRawValue.endpointAccessed),
      bank: new FormControl(aPIAuditLogRawValue.bank),
    });
  }

  getAPIAuditLog(form: APIAuditLogFormGroup): IAPIAuditLog | NewAPIAuditLog {
    return this.convertAPIAuditLogRawValueToAPIAuditLog(form.getRawValue() as APIAuditLogFormRawValue | NewAPIAuditLogFormRawValue);
  }

  resetForm(form: APIAuditLogFormGroup, aPIAuditLog: APIAuditLogFormGroupInput): void {
    const aPIAuditLogRawValue = this.convertAPIAuditLogToAPIAuditLogRawValue({ ...this.getFormDefaults(), ...aPIAuditLog });
    form.reset(
      {
        ...aPIAuditLogRawValue,
        id: { value: aPIAuditLogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): APIAuditLogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      eventTime: currentTime,
    };
  }

  private convertAPIAuditLogRawValueToAPIAuditLog(
    rawAPIAuditLog: APIAuditLogFormRawValue | NewAPIAuditLogFormRawValue,
  ): IAPIAuditLog | NewAPIAuditLog {
    return {
      ...rawAPIAuditLog,
      eventTime: dayjs(rawAPIAuditLog.eventTime, DATE_TIME_FORMAT),
    };
  }

  private convertAPIAuditLogToAPIAuditLogRawValue(
    aPIAuditLog: IAPIAuditLog | (Partial<NewAPIAuditLog> & APIAuditLogFormDefaults),
  ): APIAuditLogFormRawValue | PartialWithRequiredKeyOf<NewAPIAuditLogFormRawValue> {
    return {
      ...aPIAuditLog,
      eventTime: aPIAuditLog.eventTime ? aPIAuditLog.eventTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
