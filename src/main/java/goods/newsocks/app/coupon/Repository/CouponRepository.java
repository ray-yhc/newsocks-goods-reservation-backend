package goods.newsocks.app.coupon.Repository;

import goods.newsocks.app.coupon.Coupon;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CouponRepository {
    Optional<Coupon> findById(Long couponId);

    void save(Coupon coupon);

}
