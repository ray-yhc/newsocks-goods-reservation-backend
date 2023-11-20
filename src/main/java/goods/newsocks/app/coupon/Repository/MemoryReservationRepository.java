package goods.newsocks.app.coupon.Repository;

import goods.newsocks.app.coupon.Goods;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MemoryReservationRepository implements ReservationRepository {
    private final List<Goods> goods = new ArrayList<>();
    private long generatedId = 0L;

    @Override

    public Optional<Goods> findById(Long couponId) {
        return goods.stream()
                .filter(goods -> goods.getId().equals(couponId))
                .findFirst();
    }

    @Override
    public void save(Goods goods) {
        goods.setId(++generatedId);
        this.goods.add(goods);
    }
}
