package br.com.inovatech.powerguard.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EnergyDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Date timestamp;
    private Double tensaoa;
    private Double tensaob;
    private Double tensaoc;
    private Double correntea;
    private Double correnteb;
    private Double correntec;
    private Double potativaa;
    private Double potativab;
    private Double potativac;
    private Double potativatotal;
    private Double potreativaa;
    private Double potreativab;
    private Double potreativac;
    private Double potreativatotal;
    private Double potaparentea;
    private Double potaparenteb;
    private Double potaparentec;
    private Double potaparentetotal;
    private Double fatorpotenciaa;
    private Double fatorpotenciab;
    private Double fatorpotenciac;
    private Double fatorpotenciatotal;

    @JsonProperty("created_at")
    private Date createdAt;
}
