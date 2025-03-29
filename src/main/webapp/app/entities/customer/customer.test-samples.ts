import dayjs from 'dayjs/esm';

import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 3366,
  fullName: 'slake splurge',
  customerType: 'INDIVIDUAL',
};

export const sampleWithPartialData: ICustomer = {
  id: 30079,
  fullName: 'modulo wildly',
  customerType: 'BUSINESS',
  email: 'Mireya80@hotmail.com',
  phone: '860-823-9786 x91214',
};

export const sampleWithFullData: ICustomer = {
  id: 4149,
  fullName: 'where known',
  dateOfBirth: dayjs('2025-03-28'),
  customerType: 'BUSINESS',
  nationalId: 'indeed astride outlandish',
  passportNumber: 'pantyhose',
  address: 'pro',
  email: 'Rachael_Brown58@yahoo.com',
  phone: '427-455-1210 x67260',
  country: 'Equatorial Guinea',
};

export const sampleWithNewData: NewCustomer = {
  fullName: 'thyme inasmuch',
  customerType: 'INDIVIDUAL',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
