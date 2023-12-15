package service;

import entity.Coupon;
import entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.CouponRepository;
import repository.OrderRepository;

import java.util.List;
import java.util.Random;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private OrderRepository orderRepository;

    private static final int NTH_ORDER = 5;

    public static final double COUPON_PERCENTAGE = 10.0;

    private static final int COUPON_LENGTH = 8;

    private static final String COUPON_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final Random random = new Random();

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon getCouponByCode(String code) {
        return couponRepository.findById(code).orElse(null);
    }

    public void generateCouponIfEligible(Order order) {
        long orderCount = orderRepository.count();
        if (orderCount % NTH_ORDER == 0) {
            String code = generateCouponCode();
            Coupon coupon = new Coupon();
            coupon.setCode(code);
            coupon.setPercentage(COUPON_PERCENTAGE);
            coupon.setValid(true);
            couponRepository.save(coupon);
        }
    }

    public void validateCoupon(String code) {
        Coupon coupon = getCouponByCode(code);
        if (coupon != null) {
            coupon.setValid(false);
            couponRepository.save(coupon);
        }
    }

    String generateCouponCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < COUPON_LENGTH; i++) {
            int index = random.nextInt(COUPON_CHARS.length());
            sb.append(COUPON_CHARS.charAt(index));
        }
        return sb.toString();
    }
}
