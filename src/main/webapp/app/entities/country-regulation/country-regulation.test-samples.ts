import { ICountryRegulation, NewCountryRegulation } from './country-regulation.model';

export const sampleWithRequiredData: ICountryRegulation = {
  id: 9764,
  countryCode: 'TZ',
  countryName: 'fooey beneath numeracy',
};

export const sampleWithPartialData: ICountryRegulation = {
  id: 4654,
  countryCode: 'CG',
  countryName: 'distinct limited',
  kycRequirements: 'like approximate',
  dataLocalization: false,
  regulatorContact: 'below',
};

export const sampleWithFullData: ICountryRegulation = {
  id: 12174,
  countryCode: 'GI',
  countryName: 'sleepily hope',
  kycRequirements: 'stir-fry through wry',
  dataLocalization: true,
  regulatorContact: 'deeply continually schlep',
};

export const sampleWithNewData: NewCountryRegulation = {
  countryCode: 'BI',
  countryName: 'livid cruelly the',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
