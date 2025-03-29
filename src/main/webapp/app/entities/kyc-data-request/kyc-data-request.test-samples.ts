import dayjs from 'dayjs/esm';

import { IKycDataRequest, NewKycDataRequest } from './kyc-data-request.model';

export const sampleWithRequiredData: IKycDataRequest = {
  id: 30838,
  requestRef: 'sign meh',
  requestDate: dayjs('2025-03-29T06:02'),
  status: 'DENIED',
};

export const sampleWithPartialData: IKycDataRequest = {
  id: 27661,
  requestRef: 'er version experience',
  requestDate: dayjs('2025-03-29T08:57'),
  status: 'REQUESTED',
  encryptedResponseData: 'zowie gosh ditch',
};

export const sampleWithFullData: IKycDataRequest = {
  id: 4075,
  requestRef: 'bobble dwell submitter',
  requestDate: dayjs('2025-03-29T19:29'),
  status: 'DENIED',
  requestPurpose: 'terrible hmph',
  encryptedResponseData: 'gosh but',
  respondedAt: dayjs('2025-03-29T18:01'),
};

export const sampleWithNewData: NewKycDataRequest = {
  requestRef: 'till circa under',
  requestDate: dayjs('2025-03-29T17:55'),
  status: 'DENIED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
