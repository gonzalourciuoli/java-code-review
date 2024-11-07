package schwarz.jobs.interview.coupon.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import schwarz.jobs.interview.coupon.core.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    /**
     * @param code
     * @return A found Coupon with a code according to the code param.
     */
    Optional<Coupon> findByCode(final String code);

}
