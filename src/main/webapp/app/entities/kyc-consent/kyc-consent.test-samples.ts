import dayjs from 'dayjs/esm';

import { IKycConsent, NewKycConsent } from './kyc-consent.model';

export const sampleWithRequiredData: IKycConsent = {
  id: 19404,
  consentToken: 'rightfully',
  consentGivenAt: dayjs('2025-03-29T12:41'),
};

export const sampleWithPartialData: IKycConsent = {
  id: 26423,
  consentToken: 'finished',
  consentGivenAt: dayjs('2025-03-29T03:41'),
  expiresAt: dayjs('2025-03-28T23:32'),
  revokedAt: dayjs('2025-03-29T11:21'),
};

export const sampleWithFullData: IKycConsent = {
  id: 28266,
  consentToken: 'disinherit',
  consentGivenAt: dayjs('2025-03-29T17:31'),
  expiresAt: dayjs('2025-03-29T06:56'),
  isRevoked: true,
  revokedAt: dayjs('2025-03-29T03:27'),
};

export const sampleWithNewData: NewKycConsent = {
  consentToken: 'whoever',
  consentGivenAt: dayjs('2025-03-29T19:18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
