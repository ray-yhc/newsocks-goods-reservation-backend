package goods.newsocks.app.coupon;

import goods.newsocks.app.coupon.Repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponDecreaseService {
    private final CouponRepository couponRepository;

    // todo : 분산 락 적용
    public boolean couponDecrease(String lockName, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(IllegalArgumentException::new);

        try {
            coupon.decrease();
        } catch (IllegalArgumentException e) {
            System.out.println("굿즈 구매 예약이 모두 마감되었습니다.");
            return false;
        }

        System.out.println("예약 신청이 완료되었습니다 / 수령 코드 : " + getRandomCode() + "  /  (잔여 수량 : " + coupon.getAvailableStock() + "개)");
        return true;
    }

    String getRandomCode() {
        String randomString = "";
        String[] randomStringArray = new String[10];
        for (int i = 0; i < 10; i++) {
            randomStringArray[i] = String.valueOf((char) ((Math.random() * 26) + 97));
            randomString += randomStringArray[i];
        }
        return randomString;
    }
}