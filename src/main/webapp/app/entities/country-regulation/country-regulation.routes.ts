import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CountryRegulationResolve from './route/country-regulation-routing-resolve.service';

const countryRegulationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/country-regulation.component').then(m => m.CountryRegulationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/country-regulation-detail.component').then(m => m.CountryRegulationDetailComponent),
    resolve: {
      countryRegulation: CountryRegulationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/country-regulation-update.component').then(m => m.CountryRegulationUpdateComponent),
    resolve: {
      countryRegulation: CountryRegulationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/country-regulation-update.component').then(m => m.CountryRegulationUpdateComponent),
    resolve: {
      countryRegulation: CountryRegulationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default countryRegulationRoute;
