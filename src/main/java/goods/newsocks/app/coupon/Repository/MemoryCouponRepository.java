package goods.newsocks.app.coupon.Repository;

import goods.newsocks.app.coupon.Coupon;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MemoryCouponRepository implements CouponRepository {
    private final List<Coupon> coupons = new ArrayList<>();
    private long generatedId = 0L;

    @Override

    public Optional<Coupon> findById(Long couponId) {
        return coupons.stream()
                .filter(coupon -> coupon.getId().equals(couponId))
                .findFirst();
    }

    @Override
    public void save(Coupon coupon) {
        coupon.setId(++generatedId);
        coupons.add(coupon);
    }
}
