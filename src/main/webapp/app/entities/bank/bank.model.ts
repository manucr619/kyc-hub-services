export interface IBank {
  id: number;
  name?: string | null;
  bicCode?: string | null;
  country?: string | null;
  isActive?: boolean | null;
}

export type NewBank = Omit<IBank, 'id'> & { id: null };
