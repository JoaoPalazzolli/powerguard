package br.com.inovatech.powerguard.infra.external.domain;

import br.com.inovatech.powerguard.dtos.EnergyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResponseEnergyMonitoringAPI {

    private List<EnergyDTO> results;

}
