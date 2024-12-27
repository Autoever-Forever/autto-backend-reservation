package com.autto.back_reservation.service;

import com.autto.back_reservation.dto.SaveReservationDto;
import com.autto.back_reservation.entity.SeatInfo;
import com.autto.back_reservation.repository.SeatsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.autto.back_reservation.entity.SeatInfo.SeatStatus.AVAILABLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
@Testcontainers
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReservationLockTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SeatsRepository seatsRepository;


    // redis test-container 설정
    private static final String REDIS_IMAGE = "redis:latest";
    private static final int REDIS_PORT = 6379;
    private static final GenericContainer REDIS_CONTAINER;

    static {
        REDIS_CONTAINER = new GenericContainer(REDIS_IMAGE)
                .withExposedPorts(REDIS_PORT)
                .withReuse(true);
        REDIS_CONTAINER.start();
    }

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(REDIS_PORT)
                .toString());
    }

    /**
    * Feature : 쿠폰 차감 동시성 테스트
    * Background
     *  Given SEAT_1 좌석이 100개 남아있음
     *
     * <p>
     * Scenario : 100개의 좌석에 100명의 사용자가 동시에 접근하여 예약 시도
     *            Lock의 이름은 좌석 이름으로 설정
     * </p>
     * Then 사용자들의 요청만큼 좌석이 증가해야 함
    * */

    @Test
    void 좌석증가_분산락_적용_동시성100명_테스트() throws InterruptedException {

        //given
        UUID userId = UUID.randomUUID();
        UUID seatId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        SeatInfo seat = new SeatInfo(seatId, productId, new Date(),0, 100, AVAILABLE);
        seatsRepository.save(seat);
        SaveReservationDto saveReservationDto = new SaveReservationDto(userId.toString(), productId.toString(), seatId.toString(), 1);

        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        //when
        for(int i = 0; i < numberOfThreads; i++){
            executorService.submit(() -> {
                try {
                    reservationService.createReservation(saveReservationDto);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        SeatInfo persistSeat = seatsRepository.findById(seat.getId())
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertThat(persistSeat.getReservedSeats(), is(100));
        assertThat(persistSeat.getStatus(), is(SeatInfo.SeatStatus.FULL));
    }
}



