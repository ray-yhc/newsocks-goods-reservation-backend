package goods.newsocks;

import goods.newsocks.app.coupon.Goods;
import goods.newsocks.app.coupon.ReservationDecreaseService;
import goods.newsocks.app.coupon.Repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NewsocksApplicationTests {

    Goods goods;

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ReservationDecreaseService reservationDecreaseService;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void setUp() {
        goods = new Goods("NEWSOCKS_001", 100L);
        reservationRepository.save(goods);
    }

    /**
     * Feature: 굿즈 예약동시성 테스트
     * Background
     *     Given 굿즈 100개가 등록되어 있음
     * <p>
     * Scenario: 100개의 굿즈를 200명의 사용자가 동시에 접근해 예약 요청함
     * <p>
     * Then 1. 정확히 100개의 굿즈만 예약되어야 함
     *      2. 예약 완료순서가 일치되어야 함
     */
    @Test
    void 뉴삭스_굿즈_예약_동시성100명_테스트() throws InterruptedException {
        int numberOfThreads = 200;
        AtomicInteger reservedThreads = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    // 분산락 적용 메서드 호출 (락의 key는 쿠폰의 name으로 설정)
                    boolean success = reservationDecreaseService.couponDecrease(goods.getName(), goods.getId());
                    if (success) reservedThreads.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Goods persistGoods = reservationRepository.findById(goods.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(persistGoods.getAvailableStock()).isZero();
        assertThat(reservedThreads.get()).isEqualTo(100);
        System.out.println("잔여 굿즈 개수 = " + persistGoods.getAvailableStock());
    }

}
