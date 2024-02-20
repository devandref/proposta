package io.github.devandref.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "proposal")
public class ProposalEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String customer;
    @Column(name = "price_tone")
    private BigDecimal priceTonne;
    private Integer tonnes;
    private String country;
    @Column(name = "proposal_validity_days")
    private Integer proposalValidityDays;
    
}
