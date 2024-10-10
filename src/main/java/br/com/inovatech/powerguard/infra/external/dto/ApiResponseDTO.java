package br.com.inovatech.powerguard.infra.external.dto;

import br.com.inovatech.powerguard.dtos.EnergyDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO {

    private int count;
    private List<EnergyDTO> results;
}
