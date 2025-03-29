import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Bank',
    route: '/bank',
    translationKey: 'global.menu.entities.bank',
  },
  {
    name: 'Customer',
    route: '/customer',
    translationKey: 'global.menu.entities.customer',
  },
  {
    name: 'KycCredential',
    route: '/kyc-credential',
    translationKey: 'global.menu.entities.kycCredential',
  },
  {
    name: 'Transaction',
    route: '/transaction',
    translationKey: 'global.menu.entities.transaction',
  },
  {
    name: 'CountryRegulation',
    route: '/country-regulation',
    translationKey: 'global.menu.entities.countryRegulation',
  },
  {
    name: 'KycConsent',
    route: '/kyc-consent',
    translationKey: 'global.menu.entities.kycConsent',
  },
  {
    name: 'KycDataRequest',
    route: '/kyc-data-request',
    translationKey: 'global.menu.entities.kycDataRequest',
  },
  {
    name: 'APIAuditLog',
    route: '/api-audit-log',
    translationKey: 'global.menu.entities.aPIAuditLog',
  },
];
