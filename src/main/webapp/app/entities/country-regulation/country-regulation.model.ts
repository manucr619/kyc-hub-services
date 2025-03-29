export interface ICountryRegulation {
  id: number;
  countryCode?: string | null;
  countryName?: string | null;
  kycRequirements?: string | null;
  dataLocalization?: boolean | null;
  regulatorContact?: string | null;
}

export type NewCountryRegulation = Omit<ICountryRegulation, 'id'> & { id: null };
