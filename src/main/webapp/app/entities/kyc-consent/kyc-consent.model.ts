import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { IBank } from 'app/entities/bank/bank.model';

export interface IKycConsent {
  id: number;
  consentToken?: string | null;
  consentGivenAt?: dayjs.Dayjs | null;
  expiresAt?: dayjs.Dayjs | null;
  isRevoked?: boolean | null;
  revokedAt?: dayjs.Dayjs | null;
  customer?: Pick<ICustomer, 'id'> | null;
  issuerBank?: Pick<IBank, 'id'> | null;
  recipientBank?: Pick<IBank, 'id'> | null;
}

export type NewKycConsent = Omit<IKycConsent, 'id'> & { id: null };
