package schwarz.jobs.interview.coupon.core.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
/*
 * @Slf4j annotation added in order to be able to use Logger functions
 */
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;

    /**
     * @param code
     * @return A Optional<Coupon> found by its code.
     */
    public Optional<Coupon> getCoupon(final String code) {
        return couponRepository.findByCode(code);
    }

    /**
     * @param basket
     * @param code
     * @return A Basket with the applied discount.
     */
    /*
     * apply() has been refactorized in order to optimize the if statement usage it
     * had, the initial >= comprobation was unnecesary since there is already a
     * comprobation of > and =.
     * 
     * Also, the usage of doubleValue() has been removed since basket.getValue()
     * returns a BigDecimal,
     * in order to work with a better precision, the BigDecimal compareTo() function
     * has been selected.
     * 
     * Finally, the exception handling has been modified too, instead of using
     * System.out.println() for logging, the class Logger will be used, this helps
     * with the log monitoring. Also,
     * the RuntimeException was not the best practice in this case since it does not
     * really describe that much of the problem, in this case,
     * IllegalArgumentException will be used
     * in order to report more accurately the origin of the exception.
     */
    public Optional<Basket> apply(final Basket basket, final String code) {
        return getCoupon(code).map(coupon -> {
            if (basket.getValue().compareTo(BigDecimal.ZERO) > 0) {
                return basket.applyDiscount(coupon.getDiscount());
            } else if (basket.getValue().compareTo(BigDecimal.ZERO) == 0) {
                return basket;
            } else {
                log.error("Attempted to apply discount to a basket with negative value: {}", basket.getValue());
                throw new IllegalArgumentException("Can't apply negative discounts.");
            }
        });
    }

    /**
     * @param couponDTO
     * @return A Coupon created from the CouponDTO attributes.
     */
    /*
     * The change applied on createCoupon() has been removing the try-catch
     * statements since it was already detected that the problem was the possible
     * nullability of
     * the couponDTO.getCode() function, so, in this case, using an initial
     * comprobation would save us from a possible NullPointerException.
     */
    public Coupon createCoupon(final CouponDTO couponDTO) {
        if (couponDTO.getCode() == null) {
            log.error("Attempt to create a coupon with a null code.");
            throw new IllegalArgumentException("Coupon code cannot be null.");
        }

        Coupon coupon = Coupon.builder()
                .code(couponDTO.getCode().toLowerCase())
                .discount(couponDTO.getDiscount())
                .minBasketValue(couponDTO.getMinBasketValue())
                .build();

        return couponRepository.save(coupon);
    }

    /**
     * @param couponRequestDTO
     * @return A List<Coupon> with the found coupons by their code.
     */
    /*
     * The getCoupons() function has been redefined in order to take advantage of
     * the Optional type that the couponRequestDTO.getCodes() has, now, it can
     * filter by those coupons
     * without a value, getting rid of them before the return.
     */
    public List<Coupon> getCoupons(final CouponRequestDTO couponRequestDTO) {
        return couponRequestDTO.getCodes().stream()
                .map(couponRepository::findByCode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
