package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    JedisPool jedisPool;
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){

//        生成验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
//        try {
////        发送验证码
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
//
//        } catch (ClientException e) {
//            e.printStackTrace();
//            return  new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
//        }
////        发送成功
////        将验证码存入redis
//       finally {//测试使用加个finally
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,3000,code.toString());
            System.out.println(code);
//        }telephone+ RedisMessageConstant.SENDTYPE_ORDER

        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
