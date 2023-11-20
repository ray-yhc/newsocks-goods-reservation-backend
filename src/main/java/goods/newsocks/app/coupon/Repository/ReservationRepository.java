package goods.newsocks.app.coupon.Repository;

import goods.newsocks.app.coupon.Goods;

import java.util.Optional;

public interface ReservationRepository {
    Optional<Goods> findById(Long couponId);

    void save(Goods goods);

}
