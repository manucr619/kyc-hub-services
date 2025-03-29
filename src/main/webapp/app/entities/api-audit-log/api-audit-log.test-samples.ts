import dayjs from 'dayjs/esm';

import { IAPIAuditLog, NewAPIAuditLog } from './api-audit-log.model';

export const sampleWithRequiredData: IAPIAuditLog = {
  id: 413,
  eventTime: dayjs('2025-03-29T10:15'),
  action: 'fruitful',
};

export const sampleWithPartialData: IAPIAuditLog = {
  id: 23302,
  eventTime: dayjs('2025-03-29T02:30'),
  action: 'immediately or fully',
  endpointAccessed: 'instruction climb wallop',
};

export const sampleWithFullData: IAPIAuditLog = {
  id: 24269,
  eventTime: dayjs('2025-03-29T00:26'),
  action: 'drat uh-huh shakily',
  statusCode: 11896,
  message: 'despite',
  sourceIP: 'cap solemnly around',
  initiatedBy: 'upside-down oh',
  endpointAccessed: 'astonishing weight',
};

export const sampleWithNewData: NewAPIAuditLog = {
  eventTime: dayjs('2025-03-29T03:48'),
  action: 'reborn implode aged',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
