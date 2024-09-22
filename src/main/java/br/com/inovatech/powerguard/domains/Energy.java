package br.com.inovatech.powerguard.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "energy")
public class Energy implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
}
