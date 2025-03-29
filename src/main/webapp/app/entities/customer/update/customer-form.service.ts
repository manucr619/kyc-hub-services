import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICustomer, NewCustomer } from '../customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomer for edit and NewCustomerFormGroupInput for create.
 */
type CustomerFormGroupInput = ICustomer | PartialWithRequiredKeyOf<NewCustomer>;

type CustomerFormDefaults = Pick<NewCustomer, 'id'>;

type CustomerFormGroupContent = {
  id: FormControl<ICustomer['id'] | NewCustomer['id']>;
  fullName: FormControl<ICustomer['fullName']>;
  dateOfBirth: FormControl<ICustomer['dateOfBirth']>;
  customerType: FormControl<ICustomer['customerType']>;
  nationalId: FormControl<ICustomer['nationalId']>;
  passportNumber: FormControl<ICustomer['passportNumber']>;
  address: FormControl<ICustomer['address']>;
  email: FormControl<ICustomer['email']>;
  phone: FormControl<ICustomer['phone']>;
  country: FormControl<ICustomer['country']>;
  countryRegulation: FormControl<ICustomer['countryRegulation']>;
};

export type CustomerFormGroup = FormGroup<CustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerFormService {
  createCustomerFormGroup(customer: CustomerFormGroupInput = { id: null }): CustomerFormGroup {
    const customerRawValue = {
      ...this.getFormDefaults(),
      ...customer,
    };
    return new FormGroup<CustomerFormGroupContent>({
      id: new FormControl(
        { value: customerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fullName: new FormControl(customerRawValue.fullName, {
        validators: [Validators.required],
      }),
      dateOfBirth: new FormControl(customerRawValue.dateOfBirth),
      customerType: new FormControl(customerRawValue.customerType, {
        validators: [Validators.required],
      }),
      nationalId: new FormControl(customerRawValue.nationalId),
      passportNumber: new FormControl(customerRawValue.passportNumber),
      address: new FormControl(customerRawValue.address),
      email: new FormControl(customerRawValue.email),
      phone: new FormControl(customerRawValue.phone),
      country: new FormControl(customerRawValue.country),
      countryRegulation: new FormControl(customerRawValue.countryRegulation),
    });
  }

  getCustomer(form: CustomerFormGroup): ICustomer | NewCustomer {
    return form.getRawValue() as ICustomer | NewCustomer;
  }

  resetForm(form: CustomerFormGroup, customer: CustomerFormGroupInput): void {
    const customerRawValue = { ...this.getFormDefaults(), ...customer };
    form.reset(
      {
        ...customerRawValue,
        id: { value: customerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CustomerFormDefaults {
    return {
      id: null,
    };
  }
}
