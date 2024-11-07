package schwarz.jobs.interview.coupon.core.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
/*
 * Deleted @Data annotation since its usage is not recommended with the @Entity
 * annotation, besides, the @Getter and @Setter annotations were included.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    /* 
     * Added @GeneratedValue annotation, in order to autogenerate the id when inserting to the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /*
     * Added @NotNull annotation, this marks code as required to be not null,
     * as defined in the database.
     */
    @NotNull
    @Column(name = "code")
    private String code;

    /*
     * Added @NotNull annotation, this marks discount as required to be not
     * null, as defined in the database.
     */
    @NotNull
    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(name = "minBasketValue", precision = 10, scale = 2)
    private BigDecimal minBasketValue;

}
