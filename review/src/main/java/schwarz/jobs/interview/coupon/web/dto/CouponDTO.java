package schwarz.jobs.interview.coupon.web.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
/*
 * The changes in the CouponDTO class have been making the discount, code and
 * minBasketValue attributes final, so that the class is immutable, since that
 * is what a DTO should be in order to maintain data integrity.
 */
@ApiModel(description = "DTO representing a Coupon.")
public class CouponDTO {

    private final BigDecimal discount;

    private final String code;

    private final BigDecimal minBasketValue;

}
