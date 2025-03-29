import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICountryRegulation, NewCountryRegulation } from '../country-regulation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICountryRegulation for edit and NewCountryRegulationFormGroupInput for create.
 */
type CountryRegulationFormGroupInput = ICountryRegulation | PartialWithRequiredKeyOf<NewCountryRegulation>;

type CountryRegulationFormDefaults = Pick<NewCountryRegulation, 'id' | 'dataLocalization'>;

type CountryRegulationFormGroupContent = {
  id: FormControl<ICountryRegulation['id'] | NewCountryRegulation['id']>;
  countryCode: FormControl<ICountryRegulation['countryCode']>;
  countryName: FormControl<ICountryRegulation['countryName']>;
  kycRequirements: FormControl<ICountryRegulation['kycRequirements']>;
  dataLocalization: FormControl<ICountryRegulation['dataLocalization']>;
  regulatorContact: FormControl<ICountryRegulation['regulatorContact']>;
};

export type CountryRegulationFormGroup = FormGroup<CountryRegulationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CountryRegulationFormService {
  createCountryRegulationFormGroup(countryRegulation: CountryRegulationFormGroupInput = { id: null }): CountryRegulationFormGroup {
    const countryRegulationRawValue = {
      ...this.getFormDefaults(),
      ...countryRegulation,
    };
    return new FormGroup<CountryRegulationFormGroupContent>({
      id: new FormControl(
        { value: countryRegulationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      countryCode: new FormControl(countryRegulationRawValue.countryCode, {
        validators: [Validators.required],
      }),
      countryName: new FormControl(countryRegulationRawValue.countryName, {
        validators: [Validators.required],
      }),
      kycRequirements: new FormControl(countryRegulationRawValue.kycRequirements),
      dataLocalization: new FormControl(countryRegulationRawValue.dataLocalization),
      regulatorContact: new FormControl(countryRegulationRawValue.regulatorContact),
    });
  }

  getCountryRegulation(form: CountryRegulationFormGroup): ICountryRegulation | NewCountryRegulation {
    return form.getRawValue() as ICountryRegulation | NewCountryRegulation;
  }

  resetForm(form: CountryRegulationFormGroup, countryRegulation: CountryRegulationFormGroupInput): void {
    const countryRegulationRawValue = { ...this.getFormDefaults(), ...countryRegulation };
    form.reset(
      {
        ...countryRegulationRawValue,
        id: { value: countryRegulationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CountryRegulationFormDefaults {
    return {
      id: null,
      dataLocalization: false,
    };
  }
}
