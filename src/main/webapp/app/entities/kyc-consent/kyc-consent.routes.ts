import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import KycConsentResolve from './route/kyc-consent-routing-resolve.service';

const kycConsentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/kyc-consent.component').then(m => m.KycConsentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/kyc-consent-detail.component').then(m => m.KycConsentDetailComponent),
    resolve: {
      kycConsent: KycConsentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/kyc-consent-update.component').then(m => m.KycConsentUpdateComponent),
    resolve: {
      kycConsent: KycConsentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/kyc-consent-update.component').then(m => m.KycConsentUpdateComponent),
    resolve: {
      kycConsent: KycConsentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default kycConsentRoute;
