package schwarz.jobs.interview.coupon.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import schwarz.jobs.interview.coupon.core.services.model.Basket;

@Data
@Builder
/*
 * The changes in the ApplicationRequestDTO class have been making both code
 * and basket attributes final, so that the class is immutable, since that is
 * what a DTO should be in order to maintain data integrity.
 */
@ApiModel(description = "DTO representing an ApplicationRequest.")
public class ApplicationRequestDTO {

    @NotBlank
    private final String code;

    @NotNull
    private final Basket basket;

}
