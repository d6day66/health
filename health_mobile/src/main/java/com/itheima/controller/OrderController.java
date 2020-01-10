package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map) {
//        验证码校验
//        前台验证码
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
//        Redis验证码
        String jedisValidateCode = jedisPool.getResource().get(telephone+ RedisMessageConstant.SENDTYPE_ORDER);
        System.out.println(jedisValidateCode);
//        判断验证码是否一致
        if (validateCode != null && jedisValidateCode != null && validateCode.equals(jedisValidateCode)) {
//            调用service，前台传过来map数据，后台需要返回的是order包含体检信息且是微信预约
            //        加入预约类型
            Result result = null;
            try {
                map.put("orderType", Order.ORDERTYPE_WEIXIN);
                result = orderService.order(map);
//                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, (String) map.get("telephone"), (String) map.get("orderDate"));
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
        } else {
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }

    }
    @RequestMapping("/findById")
    public Result findById(Integer id){
//        预定成功后前端跳转到成功页面并传递过来预定id，查询数据库返回数据给前端页面
//                            <p>体检人：{{orderInfo.member}}</p> 通过order的memberId找member表
//                            <p>体检套餐：{{orderInfo.setmeal}}</p>通过order的setmealId找setmeal表
//                            <p>体检日期：{{orderInfo.orderDate}}</p>order表orderDate
//                            <p>预约类型：{{orderInfo.orderType}}</p>order表orderType
       Result result = orderService.findById(id);
       return result;
    }
}
