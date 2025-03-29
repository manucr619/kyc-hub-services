import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { IBank } from 'app/entities/bank/bank.model';
import { TransactionStatus } from 'app/entities/enumerations/transaction-status.model';

export interface ITransaction {
  id: number;
  transactionRef?: string | null;
  amount?: number | null;
  currency?: string | null;
  timestamp?: dayjs.Dayjs | null;
  status?: keyof typeof TransactionStatus | null;
  notes?: string | null;
  originator?: Pick<ICustomer, 'id'> | null;
  beneficiary?: Pick<ICustomer, 'id'> | null;
  originatorBank?: Pick<IBank, 'id'> | null;
  beneficiaryBank?: Pick<IBank, 'id'> | null;
}

export type NewTransaction = Omit<ITransaction, 'id'> & { id: null };
