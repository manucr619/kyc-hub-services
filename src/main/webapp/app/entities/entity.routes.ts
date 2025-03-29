import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'kycNetworkApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'bank',
    data: { pageTitle: 'kycNetworkApp.bank.home.title' },
    loadChildren: () => import('./bank/bank.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'kycNetworkApp.customer.home.title' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'kyc-credential',
    data: { pageTitle: 'kycNetworkApp.kycCredential.home.title' },
    loadChildren: () => import('./kyc-credential/kyc-credential.routes'),
  },
  {
    path: 'transaction',
    data: { pageTitle: 'kycNetworkApp.transaction.home.title' },
    loadChildren: () => import('./transaction/transaction.routes'),
  },
  {
    path: 'country-regulation',
    data: { pageTitle: 'kycNetworkApp.countryRegulation.home.title' },
    loadChildren: () => import('./country-regulation/country-regulation.routes'),
  },
  {
    path: 'kyc-consent',
    data: { pageTitle: 'kycNetworkApp.kycConsent.home.title' },
    loadChildren: () => import('./kyc-consent/kyc-consent.routes'),
  },
  {
    path: 'kyc-data-request',
    data: { pageTitle: 'kycNetworkApp.kycDataRequest.home.title' },
    loadChildren: () => import('./kyc-data-request/kyc-data-request.routes'),
  },
  {
    path: 'api-audit-log',
    data: { pageTitle: 'kycNetworkApp.aPIAuditLog.home.title' },
    loadChildren: () => import('./api-audit-log/api-audit-log.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
