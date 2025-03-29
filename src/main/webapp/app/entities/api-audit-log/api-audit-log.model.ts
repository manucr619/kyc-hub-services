import dayjs from 'dayjs/esm';
import { IBank } from 'app/entities/bank/bank.model';

export interface IAPIAuditLog {
  id: number;
  eventTime?: dayjs.Dayjs | null;
  action?: string | null;
  statusCode?: number | null;
  message?: string | null;
  sourceIP?: string | null;
  initiatedBy?: string | null;
  endpointAccessed?: string | null;
  bank?: Pick<IBank, 'id'> | null;
}

export type NewAPIAuditLog = Omit<IAPIAuditLog, 'id'> & { id: null };
