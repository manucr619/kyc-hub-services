import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICountryRegulation } from 'app/entities/country-regulation/country-regulation.model';
import { CountryRegulationService } from 'app/entities/country-regulation/service/country-regulation.service';
import { CustomerType } from 'app/entities/enumerations/customer-type.model';
import { CustomerService } from '../service/customer.service';
import { ICustomer } from '../customer.model';
import { CustomerFormGroup, CustomerFormService } from './customer-form.service';

@Component({
  selector: 'jhi-customer-update',
  templateUrl: './customer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CustomerUpdateComponent implements OnInit {
  isSaving = false;
  customer: ICustomer | null = null;
  customerTypeValues = Object.keys(CustomerType);

  countryRegulationsSharedCollection: ICountryRegulation[] = [];

  protected customerService = inject(CustomerService);
  protected customerFormService = inject(CustomerFormService);
  protected countryRegulationService = inject(CountryRegulationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustomerFormGroup = this.customerFormService.createCustomerFormGroup();

  compareCountryRegulation = (o1: ICountryRegulation | null, o2: ICountryRegulation | null): boolean =>
    this.countryRegulationService.compareCountryRegulation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.customer = customer;
      if (customer) {
        this.updateForm(customer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customer = this.customerFormService.getCustomer(this.editForm);
    if (customer.id !== null) {
      this.subscribeToSaveResponse(this.customerService.update(customer));
    } else {
      this.subscribeToSaveResponse(this.customerService.create(customer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(customer: ICustomer): void {
    this.customer = customer;
    this.customerFormService.resetForm(this.editForm, customer);

    this.countryRegulationsSharedCollection = this.countryRegulationService.addCountryRegulationToCollectionIfMissing<ICountryRegulation>(
      this.countryRegulationsSharedCollection,
      customer.countryRegulation,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.countryRegulationService
      .query()
      .pipe(map((res: HttpResponse<ICountryRegulation[]>) => res.body ?? []))
      .pipe(
        map((countryRegulations: ICountryRegulation[]) =>
          this.countryRegulationService.addCountryRegulationToCollectionIfMissing<ICountryRegulation>(
            countryRegulations,
            this.customer?.countryRegulation,
          ),
        ),
      )
      .subscribe((countryRegulations: ICountryRegulation[]) => (this.countryRegulationsSharedCollection = countryRegulations));
  }
}
