import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransaction, NewTransaction } from '../transaction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransaction for edit and NewTransactionFormGroupInput for create.
 */
type TransactionFormGroupInput = ITransaction | PartialWithRequiredKeyOf<NewTransaction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransaction | NewTransaction> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type TransactionFormRawValue = FormValueOf<ITransaction>;

type NewTransactionFormRawValue = FormValueOf<NewTransaction>;

type TransactionFormDefaults = Pick<NewTransaction, 'id' | 'timestamp'>;

type TransactionFormGroupContent = {
  id: FormControl<TransactionFormRawValue['id'] | NewTransaction['id']>;
  transactionRef: FormControl<TransactionFormRawValue['transactionRef']>;
  amount: FormControl<TransactionFormRawValue['amount']>;
  currency: FormControl<TransactionFormRawValue['currency']>;
  timestamp: FormControl<TransactionFormRawValue['timestamp']>;
  status: FormControl<TransactionFormRawValue['status']>;
  notes: FormControl<TransactionFormRawValue['notes']>;
  originator: FormControl<TransactionFormRawValue['originator']>;
  beneficiary: FormControl<TransactionFormRawValue['beneficiary']>;
  originatorBank: FormControl<TransactionFormRawValue['originatorBank']>;
  beneficiaryBank: FormControl<TransactionFormRawValue['beneficiaryBank']>;
};

export type TransactionFormGroup = FormGroup<TransactionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransactionFormService {
  createTransactionFormGroup(transaction: TransactionFormGroupInput = { id: null }): TransactionFormGroup {
    const transactionRawValue = this.convertTransactionToTransactionRawValue({
      ...this.getFormDefaults(),
      ...transaction,
    });
    return new FormGroup<TransactionFormGroupContent>({
      id: new FormControl(
        { value: transactionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      transactionRef: new FormControl(transactionRawValue.transactionRef, {
        validators: [Validators.required],
      }),
      amount: new FormControl(transactionRawValue.amount, {
        validators: [Validators.required],
      }),
      currency: new FormControl(transactionRawValue.currency, {
        validators: [Validators.required],
      }),
      timestamp: new FormControl(transactionRawValue.timestamp, {
        validators: [Validators.required],
      }),
      status: new FormControl(transactionRawValue.status, {
        validators: [Validators.required],
      }),
      notes: new FormControl(transactionRawValue.notes),
      originator: new FormControl(transactionRawValue.originator),
      beneficiary: new FormControl(transactionRawValue.beneficiary),
      originatorBank: new FormControl(transactionRawValue.originatorBank),
      beneficiaryBank: new FormControl(transactionRawValue.beneficiaryBank),
    });
  }

  getTransaction(form: TransactionFormGroup): ITransaction | NewTransaction {
    return this.convertTransactionRawValueToTransaction(form.getRawValue() as TransactionFormRawValue | NewTransactionFormRawValue);
  }

  resetForm(form: TransactionFormGroup, transaction: TransactionFormGroupInput): void {
    const transactionRawValue = this.convertTransactionToTransactionRawValue({ ...this.getFormDefaults(), ...transaction });
    form.reset(
      {
        ...transactionRawValue,
        id: { value: transactionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransactionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertTransactionRawValueToTransaction(
    rawTransaction: TransactionFormRawValue | NewTransactionFormRawValue,
  ): ITransaction | NewTransaction {
    return {
      ...rawTransaction,
      timestamp: dayjs(rawTransaction.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertTransactionToTransactionRawValue(
    transaction: ITransaction | (Partial<NewTransaction> & TransactionFormDefaults),
  ): TransactionFormRawValue | PartialWithRequiredKeyOf<NewTransactionFormRawValue> {
    return {
      ...transaction,
      timestamp: transaction.timestamp ? transaction.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
