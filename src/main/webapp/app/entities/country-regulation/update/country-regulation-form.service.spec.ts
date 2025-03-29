import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../country-regulation.test-samples';

import { CountryRegulationFormService } from './country-regulation-form.service';

describe('CountryRegulation Form Service', () => {
  let service: CountryRegulationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CountryRegulationFormService);
  });

  describe('Service methods', () => {
    describe('createCountryRegulationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCountryRegulationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            countryCode: expect.any(Object),
            countryName: expect.any(Object),
            kycRequirements: expect.any(Object),
            dataLocalization: expect.any(Object),
            regulatorContact: expect.any(Object),
          }),
        );
      });

      it('passing ICountryRegulation should create a new form with FormGroup', () => {
        const formGroup = service.createCountryRegulationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            countryCode: expect.any(Object),
            countryName: expect.any(Object),
            kycRequirements: expect.any(Object),
            dataLocalization: expect.any(Object),
            regulatorContact: expect.any(Object),
          }),
        );
      });
    });

    describe('getCountryRegulation', () => {
      it('should return NewCountryRegulation for default CountryRegulation initial value', () => {
        const formGroup = service.createCountryRegulationFormGroup(sampleWithNewData);

        const countryRegulation = service.getCountryRegulation(formGroup) as any;

        expect(countryRegulation).toMatchObject(sampleWithNewData);
      });

      it('should return NewCountryRegulation for empty CountryRegulation initial value', () => {
        const formGroup = service.createCountryRegulationFormGroup();

        const countryRegulation = service.getCountryRegulation(formGroup) as any;

        expect(countryRegulation).toMatchObject({});
      });

      it('should return ICountryRegulation', () => {
        const formGroup = service.createCountryRegulationFormGroup(sampleWithRequiredData);

        const countryRegulation = service.getCountryRegulation(formGroup) as any;

        expect(countryRegulation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICountryRegulation should not enable id FormControl', () => {
        const formGroup = service.createCountryRegulationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCountryRegulation should disable id FormControl', () => {
        const formGroup = service.createCountryRegulationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
