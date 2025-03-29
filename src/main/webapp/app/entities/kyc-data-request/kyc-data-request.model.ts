import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { IBank } from 'app/entities/bank/bank.model';
import { IKycConsent } from 'app/entities/kyc-consent/kyc-consent.model';
import { RequestStatus } from 'app/entities/enumerations/request-status.model';

export interface IKycDataRequest {
  id: number;
  requestRef?: string | null;
  requestDate?: dayjs.Dayjs | null;
  status?: keyof typeof RequestStatus | null;
  requestPurpose?: string | null;
  encryptedResponseData?: string | null;
  respondedAt?: dayjs.Dayjs | null;
  customer?: Pick<ICustomer, 'id'> | null;
  fromBank?: Pick<IBank, 'id'> | null;
  toBank?: Pick<IBank, 'id'> | null;
  consent?: Pick<IKycConsent, 'id'> | null;
}

export type NewKycDataRequest = Omit<IKycDataRequest, 'id'> & { id: null };
