package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import service.AdminService;
import service.CouponService;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private CouponService couponService;

    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> getReport() {
        Map<String, Object> report = adminService.getReport();
        return new ResponseEntity<Map<String, Object>>(report, HttpStatus.OK);
    }

    @PostMapping("/generate-coupon")
    public ResponseEntity<ApiResponse> generateCoupon() {
        adminService.generateCoupon();
        return new ResponseEntity<>(new ApiResponse(true, "A new coupon code has been generated."), HttpStatus.CREATED);
    }
}
