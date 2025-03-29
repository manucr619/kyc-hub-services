import { IBank, NewBank } from './bank.model';

export const sampleWithRequiredData: IBank = {
  id: 9928,
  name: 'amongst while deploy',
  bicCode: 'um boggle',
  country: 'New Caledonia',
};

export const sampleWithPartialData: IBank = {
  id: 10442,
  name: 'than aboard',
  bicCode: 'next even into',
  country: 'Belgium',
  isActive: true,
};

export const sampleWithFullData: IBank = {
  id: 26049,
  name: 'anticodon',
  bicCode: 'beneath',
  country: 'Israel',
  isActive: true,
};

export const sampleWithNewData: NewBank = {
  name: 'whether oh proud',
  bicCode: 'taxicab',
  country: 'India',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
