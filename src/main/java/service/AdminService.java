package service;

import entity.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.CouponRepository;
import repository.OrderRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired
    private OrderRepository orderRepository;

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
                .mapToDouble(order -> order.getDiscount())
                .sum();
        report.put("totalDiscount", totalDiscount);
        return report;
    }
}
