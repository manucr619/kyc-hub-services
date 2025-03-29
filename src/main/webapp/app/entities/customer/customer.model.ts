import dayjs from 'dayjs/esm';
import { ICountryRegulation } from 'app/entities/country-regulation/country-regulation.model';
import { CustomerType } from 'app/entities/enumerations/customer-type.model';

export interface ICustomer {
  id: number;
  fullName?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  customerType?: keyof typeof CustomerType | null;
  nationalId?: string | null;
  passportNumber?: string | null;
  address?: string | null;
  email?: string | null;
  phone?: string | null;
  country?: string | null;
  countryRegulation?: Pick<ICountryRegulation, 'id'> | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
