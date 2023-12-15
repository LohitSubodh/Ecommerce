package service;

import entity.Coupon;
import entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.CouponRepository;
import repository.OrderRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static service.CouponService.COUPON_PERCENTAGE;

@Service
public class AdminService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private  CouponService couponService;
    @Autowired
    private CouponRepository couponRepository;

    public Map<String, Object> getReport() {
        Map<String, Object> report = new HashMap<>();
        // get the count of items purchased
        int itemCount = orderRepository.findAll()
                .stream()
                .mapToInt(order -> order.getItems().size())
                .sum();
        report.put("itemCount", itemCount);
        // get the total purchase amount
        double totalAmount = orderRepository.findAll()
                .stream()
                .mapToDouble(order -> order.getTotal() - order.getDiscount())
                .sum();
        report.put("totalAmount", totalAmount);
        // get the list of discount codes
        List<Coupon> coupons = couponRepository.findAll();
        report.put("coupons", coupons);
        // get the total discount amount
        double totalDiscount = orderRepository.findAll()
                .stream()
                .mapToDouble(Order::getDiscount)
                .sum();
        report.put("totalDiscount", totalDiscount);
        return report;
    }
    public void generateCoupon() {
        // create a new coupon object
        Coupon coupon = new Coupon();
        // generate a random coupon code
        String code = couponService.generateCouponCode();
        // set the coupon code
        coupon.setCode(code);
        // set the coupon percentage
        coupon.setPercentage(COUPON_PERCENTAGE);
        // set the coupon as valid
        coupon.setValid(true);
        // save the coupon to the database
        couponRepository.save(coupon);
    }
}
