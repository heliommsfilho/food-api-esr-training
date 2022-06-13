package com.github.heliommsfilho.foodapi.infrastructure.service.report;

import com.github.heliommsfilho.foodapi.domain.filter.VendaDiariaFilter;
import com.github.heliommsfilho.foodapi.domain.service.VendaQueryService;
import com.github.heliommsfilho.foodapi.domain.service.VendasReportService;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;

@Service
public class VendasReportServicePdf implements VendasReportService {

    VendaQueryService vendaQueryService;

    @Autowired
    public VendasReportServicePdf(VendaQueryService vendaQueryService) {
        this.vendaQueryService = vendaQueryService;
    }

    @Override
    public byte[] emitirVendasDiarias(VendaDiariaFilter filter, String timeOffset) {
        try {
            var inputStream = this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper");

            var parametros = new HashMap<String, Object>();
            parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

            var vendasDiarias = vendaQueryService.consultarVendasDiarias(filter, timeOffset);
            var dataSource = new JRBeanCollectionDataSource(vendasDiarias);
            var jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new ReportException("Não foi possível emitir relatório de vendas diárias", e);
        }
    }
}
