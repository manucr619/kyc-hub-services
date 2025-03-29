import dayjs from 'dayjs/esm';

import { IKycCredential, NewKycCredential } from './kyc-credential.model';

export const sampleWithRequiredData: IKycCredential = {
  id: 1169,
  issuedDate: dayjs('2025-03-29T05:22'),
  expiryDate: dayjs('2025-03-29T17:20'),
  status: 'REVOKED',
  credentialHash: 'failing ew',
  signature: 'limping clear-cut nor',
};

export const sampleWithPartialData: IKycCredential = {
  id: 31072,
  issuedDate: dayjs('2025-03-29T17:50'),
  expiryDate: dayjs('2025-03-29T08:45'),
  status: 'VALID',
  levelOfVerification: 'fuzzy',
  schemaVersion: 'fully',
  credentialHash: 'clinking vibration hefty',
  signature: 'scramble',
};

export const sampleWithFullData: IKycCredential = {
  id: 6556,
  issuedDate: dayjs('2025-03-29T05:35'),
  expiryDate: dayjs('2025-03-29T06:36'),
  status: 'REVOKED',
  levelOfVerification: 'morning nauseate',
  schemaVersion: 'youthfully deployment',
  credentialHash: 'opposite',
  signature: 'hunt now',
};

export const sampleWithNewData: NewKycCredential = {
  issuedDate: dayjs('2025-03-29T05:35'),
  expiryDate: dayjs('2025-03-29T14:07'),
  status: 'EXPIRED',
  credentialHash: 'slushy minus',
  signature: 'wetly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
