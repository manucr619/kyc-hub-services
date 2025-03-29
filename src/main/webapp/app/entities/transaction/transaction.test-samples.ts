import dayjs from 'dayjs/esm';

import { ITransaction, NewTransaction } from './transaction.model';

export const sampleWithRequiredData: ITransaction = {
  id: 3793,
  transactionRef: 'dearly but aha',
  amount: 24721.61,
  currency: 'weekly ham',
  timestamp: dayjs('2025-03-29T14:15'),
  status: 'FLAGGED',
};

export const sampleWithPartialData: ITransaction = {
  id: 9396,
  transactionRef: 'low so meager',
  amount: 16125.31,
  currency: 'until old-fashioned oof',
  timestamp: dayjs('2025-03-29T10:52'),
  status: 'COMPLETED',
};

export const sampleWithFullData: ITransaction = {
  id: 20285,
  transactionRef: 'cloudy',
  amount: 24229.04,
  currency: 'kissingly',
  timestamp: dayjs('2025-03-29T02:55'),
  status: 'FLAGGED',
  notes: 'at less worth',
};

export const sampleWithNewData: NewTransaction = {
  transactionRef: 'custom minty beneficial',
  amount: 5642.6,
  currency: 'complicated wisely meh',
  timestamp: dayjs('2025-03-29T08:42'),
  status: 'COMPLETED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
