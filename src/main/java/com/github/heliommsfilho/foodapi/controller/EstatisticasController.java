package com.github.heliommsfilho.foodapi.controller;

import com.github.heliommsfilho.foodapi.domain.filter.VendaDiariaFilter;
import com.github.heliommsfilho.foodapi.domain.model.dto.VendaDiaria;
import com.github.heliommsfilho.foodapi.domain.service.VendaQueryService;
import com.github.heliommsfilho.foodapi.domain.service.VendasReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/estatisticas")
public class EstatisticasController {
    
    private VendaQueryService vendaQueryService;
    private VendasReportService vendasReportService;

    @Autowired
    public EstatisticasController(final VendaQueryService vendaQueryService, final VendasReportService vendasReportService) {
        this.vendaQueryService = vendaQueryService;
        this.vendasReportService = vendasReportService;
    }
    
    @GetMapping(value = "/vendas-diarias", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
        return vendaQueryService.consultarVendasDiarias(filtro, timeOffset);
    }

    @GetMapping(value = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> consultarVendasDiariasPdf(VendaDiariaFilter filtro, @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
        final byte[] bytesPdf = vendasReportService.emitirVendasDiarias(filtro, timeOffset);

        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diaras.pdf");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .headers(headers)
                .body(bytesPdf);
    }
}
