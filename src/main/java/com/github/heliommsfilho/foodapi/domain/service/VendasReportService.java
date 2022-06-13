package com.github.heliommsfilho.foodapi.domain.service;

import com.github.heliommsfilho.foodapi.domain.filter.VendaDiariaFilter;

public interface VendasReportService {

    byte[] emitirVendasDiarias(VendaDiariaFilter filter, String timeOffset);
}
