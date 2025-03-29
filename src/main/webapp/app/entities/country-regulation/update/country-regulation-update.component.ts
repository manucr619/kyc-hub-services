import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICountryRegulation } from '../country-regulation.model';
import { CountryRegulationService } from '../service/country-regulation.service';
import { CountryRegulationFormGroup, CountryRegulationFormService } from './country-regulation-form.service';

@Component({
  selector: 'jhi-country-regulation-update',
  templateUrl: './country-regulation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CountryRegulationUpdateComponent implements OnInit {
  isSaving = false;
  countryRegulation: ICountryRegulation | null = null;

  protected countryRegulationService = inject(CountryRegulationService);
  protected countryRegulationFormService = inject(CountryRegulationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CountryRegulationFormGroup = this.countryRegulationFormService.createCountryRegulationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countryRegulation }) => {
      this.countryRegulation = countryRegulation;
      if (countryRegulation) {
        this.updateForm(countryRegulation);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const countryRegulation = this.countryRegulationFormService.getCountryRegulation(this.editForm);
    if (countryRegulation.id !== null) {
      this.subscribeToSaveResponse(this.countryRegulationService.update(countryRegulation));
    } else {
      this.subscribeToSaveResponse(this.countryRegulationService.create(countryRegulation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountryRegulation>>): void {
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

  protected updateForm(countryRegulation: ICountryRegulation): void {
    this.countryRegulation = countryRegulation;
    this.countryRegulationFormService.resetForm(this.editForm, countryRegulation);
  }
}
