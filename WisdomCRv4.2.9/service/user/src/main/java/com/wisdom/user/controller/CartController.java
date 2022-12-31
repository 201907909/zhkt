package com.wisdom.user.controller;

import com.wisdom.base.util.R;
import com.wisdom.user.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
@Api(tags = "购物车接口")
@CrossOrigin
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 查询所有购物车
     */
    @GetMapping("/getCouIdByStuId")
    @ApiOperation(value = "查询所有购物车")
    public R getAllCart(@ApiIgnore HttpSession session){
        Long stuId = (Long) session.getAttribute("stuId");
        return R.data(cartService.getCourseByCouId(stuId));
    }

    /**
     * 删除购物车
     */
    @PostMapping("/deleteCartByCouId")
    @ApiOperation(value = "删除购物车")
    public R deleteCartByCouId(@ApiIgnore HttpSession session,
                               @RequestParam("couId") Long couId){
        Long stuId = (Long) session.getAttribute("stuId");
        cartService.deletedCartByCouId(stuId, couId);
        return R.ok();
    }

    /**
     * 确认购买
     */
    @PostMapping("/buyCartByCouId")
    @ApiOperation(value = "确认购买")
    public R buyCartByCouId(@ApiIgnore HttpSession session,
                            @RequestParam("couId") List<String> couId) {
        Long stuId = (Long) session.getAttribute("stuId");
        List<Long> cId = new ArrayList<>();
        for (int i=0;i<couId.size();i++) {
            cId.add(Long.valueOf(couId.get(i)));
        }
        return cartService.buyCartByCouId(stuId, cId);
    }

    /**
     * 点击查看详情
     */
    @GetMapping("/cartDetailByCouId")
    @ApiOperation(value = "点击查购物车详情")
    public R cartDetailByCouId(@RequestParam("couId") String couId) {
        return R.data(cartService.cartDetailByCouId(Long.valueOf(couId)));
    }

    /**
     * 加入购物车
     */
    @PostMapping("/add")
    @ApiOperation(value = "加入购物车")
    public R add(@ApiIgnore HttpSession session, @RequestParam("couId") String couId) {
        Long stuId = (Long) session.getAttribute("stuId");
        cartService.add(Long.valueOf(stuId), Long.valueOf(couId));
        return R.ok();
    }

//    /**
//     * 退款
//     */
//    @PostMapping("/refund")
//    @ApiOperation(value = "退款")
//    public R refund(@ApiIgnore HttpSession session, @RequestParam("couId") String couId) {
//        Long stuId = (Long) session.getAttribute("stuId");
//        cartService.refund(stuId, Long.valueOf(couId));
//        return R.ok();
//    }

    @GetMapping("/isAddCart")
    Integer isAddCart(@RequestParam("stuId") Long stuId, @RequestParam("couId") Long couId){
        return cartService.isAddCart(stuId, couId);
    }

    @GetMapping("/isBuy")
    Integer isBuy(@RequestParam("stuId") Long stuId, @RequestParam("couId") Long couId) {
        return cartService.isBuy(stuId, couId);
    }
}
