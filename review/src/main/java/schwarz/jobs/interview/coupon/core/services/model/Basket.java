package schwarz.jobs.interview.coupon.core.services.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;

/* 
 * In order to make a more explicit class, the @Data annotation has been removed since there is no usage of its other implicit annotations.
 * 
 * For this case, the @Getter annotation has been included, the @Setter annotation has been omitted 
 * since another of the changes has been making Basket an immutable class, with the purpose of making it more secure.
 */
@Getter
@Builder
public class Basket {

    @NotNull
    private final BigDecimal value;

    private final BigDecimal appliedDiscount;

    private final boolean applicationSuccessful;

    /**
     * @param discount
     * @return A Basket with its attributes set from the given parameter.
     */
    /*
     * In applyDiscount() the corrections have been consistent with the immutability
     * changes, making it return an instance of Basket with the setted parameters,
     * also, the previous implementation of applyDiscount() was always setting
     * applicationSuccessful to false, now, with the applied comprobations,
     * applicationSuccessful
     * will have a true or false state, depending if the process could or not be
     * done.
     */
    public Basket applyDiscount(final BigDecimal discount) {
        if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
            return Basket.builder()
                    .value(this.value)
                    .appliedDiscount(discount)
                    .applicationSuccessful(true)
                    .build();
        }
        return Basket.builder()
                .value(this.value)
                .appliedDiscount(this.appliedDiscount)
                .applicationSuccessful(false)
                .build();
    }

}
