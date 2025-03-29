import dayjs from 'dayjs/esm';
import { IBank } from 'app/entities/bank/bank.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CredentialStatus } from 'app/entities/enumerations/credential-status.model';

export interface IKycCredential {
  id: number;
  issuedDate?: dayjs.Dayjs | null;
  expiryDate?: dayjs.Dayjs | null;
  status?: keyof typeof CredentialStatus | null;
  levelOfVerification?: string | null;
  schemaVersion?: string | null;
  credentialHash?: string | null;
  signature?: string | null;
  issuer?: Pick<IBank, 'id'> | null;
  customer?: Pick<ICustomer, 'id'> | null;
}

export type NewKycCredential = Omit<IKycCredential, 'id'> & { id: null };
