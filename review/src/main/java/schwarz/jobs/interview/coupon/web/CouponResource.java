package schwarz.jobs.interview.coupon.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.web.dto.ApplicationRequestDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class CouponResource {
    /*
     * The CouponResource class has been refactorized in different ways that are
     * described on each method, but one of the general changes is the usage
     * of @ApiOperation annotation, included in every endpoint in order to have a
     * better understanding of their purpose.
     * 
     * Also, javadoc has been generated for each endpoint.
     */

    private final CouponService couponService;

    /**
     * @param applicationRequestDTO
     * @return A Basket ResponseEntity according to the process status, if the
     *         status is successful, the new Basket is returned in the response
     *         body.
     */
    @ApiOperation(value = "Apply a coupon to a basket - Version 2", notes = "Applies a specified coupon to a basket based on the provided ApplicationRequestDTO. "
            + "The coupon is applied only if the basket value meets the minimum required for the discount. "
            + "If the coupon is applied successfully, the updated basket is returned. "
            + "If the coupon cannot be applied, an HTTP status code is returned depending on the error case.")
    @PostMapping(value = "/apply")
    /*
     * The apply() method has been updated to work with the Optional return type
     * from couponService.apply(),
     * instead of returning applicationRequestDTO.getBasket() directly, the
     * updatedBasket returned from couponService.apply() is used.
     */
    public ResponseEntity<Basket> apply(@RequestBody @Valid final ApplicationRequestDTO applicationRequestDTO) {
        log.info("Applying coupon");

        // Here, the coupon is applied and the updatedBasket is returned.
        Optional<Basket> updatedBasket = couponService.apply(applicationRequestDTO.getBasket(),
                applicationRequestDTO.getCode().toLowerCase());

        // If the coupon could not be applied, 404 Not Found error is returned.
        if (updatedBasket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // If the coupon application was not successfull, 409 Conflict error is
        // returned.
        Basket basket = updatedBasket.get();
        if (!basket.isApplicationSuccessful()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        log.info("Applied coupon");

        // If everything works properly, the updated basket is returned.
        return ResponseEntity.ok().body(basket);
    }

    /**
     * @param couponDTO
     * @return A CouponDTO ResponseEntity according to the process status, if the
     *         status is successful, the created coupon is returned in the response
     *         body.
     */
    @ApiOperation(value = "Create a new coupon - Version 2", notes = "Creates a new coupon with the specified details in the CouponDTO. "
            + "The coupon code must be unique and non-null. "
            + "On successful creation, the created coupon data is returned in the response body.")
    @PostMapping("/create")
    /*
     * Now, the create() function logs the attempts of a coupon creation, also the
     * successful and failed states.
     * 
     * Also, now instead of returning just a state, now, the created coupon is also
     * returned in the body of the response, making use of the CouponDTO.
     */
    public ResponseEntity<CouponDTO> create(@RequestBody @Valid final CouponDTO couponDTO) {
        log.info("Attempting to create a new coupon with code: {}", couponDTO.getCode());

        // Added comprobation for a negative discount
        if (couponDTO.getDiscount().compareTo(BigDecimal.ZERO) < 0) {
            log.error("Can't add a negative discount.");
            return ResponseEntity.badRequest().build();
        }

        // Added comprobation for a minBasketValue which would cause a negative Basket
        // value
        if (couponDTO.getMinBasketValue().compareTo(couponDTO.getDiscount()) < 0) {
            log.error("The given minBasketValue would cause a negative basket value for the given discount.");
            return ResponseEntity.badRequest().build();
        }

        try {
            Coupon coupon = couponService.createCoupon(couponDTO);

            // The coupon entity gets mapped into a coupon DTO in order to be returned in
            // the body of the successful case.
            CouponDTO responseDTO = CouponDTO.builder()
                    .code(coupon.getCode())
                    .discount(coupon.getDiscount())
                    .minBasketValue(coupon.getMinBasketValue())
                    .build();

            log.info("Coupon created successfully with id: {}", coupon.getId());

            // If there are no errors, the request ends sending a status of creation and the
            // coupon DTO in its body.
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (IllegalArgumentException e) {
            log.error("Failed to create coupon: {}", e.getMessage());
            // In the case where there is any problem with the coupon creation, a 400 Bad
            // Request error is returned.
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * @param codes
     * @return Returns a List<CouponDTO> ResponseEntity according to the process
     *         status, if the status is successful, the obtained coupons are
     *         returned in the response body.
     */
    @ApiOperation(value = "Retrieve a list of coupons by codes - Version 2", notes = "Fetches a list of coupons based on the provided list of coupon codes. "
            + "Each coupon includes basic information such as code, discount, and minimum basket value. "
            + "If no coupons are found for the given codes, a 404 Not Found status is returned.")
    @GetMapping("/coupons")
    /*
     * getCoupons() has been updated to work similar to the other requests, the type
     * has been changed to a ResponseEntity type in order to be able to return a
     * Http status code, also,
     * the List item type has been changed to CouponDTO, in order no to expose
     * unrequired information.
     */
    public ResponseEntity<List<CouponDTO>> getCoupons(@RequestParam List<String> codes) {
        log.info("Fetching coupons for {} codes", codes.size());

        // Each coupon is mapped to its coupon DTO.
        List<CouponDTO> coupons = couponService.getCoupons(CouponRequestDTO.builder().codes(codes.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList()))
                .build())
                .stream()
                .map(coupon -> CouponDTO.builder()
                        .code(coupon.getCode())
                        .discount(coupon.getDiscount())
                        .minBasketValue(coupon.getMinBasketValue())
                        .build())
                .collect(Collectors.toList());

        log.info("Found {} coupons for requested codes", coupons.size());

        // If no coupons are found, a 404 Not Found error is returned.
        if (coupons.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // If everything works fine, the coupons are returned in a list of their DTOs.
        return ResponseEntity.ok(coupons);
    }
}
