package schwarz.jobs.interview.coupon.web.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
/*
 * The change in the CouponRequestDTO class has been making the codes attribute
 * final, so that the class is immutable, since that is what a DTO should be in order to maintain data integrity.
 */
@ApiModel(description = "DTO representing a CouponRequest.")
public class CouponRequestDTO {

    @NotNull
    private final List<String> codes;

}
