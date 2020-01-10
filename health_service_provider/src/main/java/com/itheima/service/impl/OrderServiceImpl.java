package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderSettingDao orderSettingDao;
    @Autowired
    OrderDao orderDao;

    @Override
    public Result order(Map map) throws Exception {
        /*
        *
        * 将前台传入的map数据
          这里进行业务逻辑处理
          然后返回一个order，取orderId返回给前台
          主要业务逻辑：
          1.判断预约日期是否已经设置

          2.判断预约日期预约人数是否大于设置人数
          3.判断是否重复预约，根据手机号码查询member表
          4.若第三步没有查出来就不是会员，不是会员就自动注册
          5.预约并更新预约人数

                            */
//        1.判断预约日期是否已经设置
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
//        2.判断预约日期预约人数是否大于设置人数
        int orderSettingNumber = orderSetting.getNumber();//预约设置人数
        int reservations = orderSetting.getReservations();//预约人数
        if (reservations >= orderSettingNumber) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }

//        3.判断是否重复预约（同一个手机号预约了同一天预约了同一个套餐），根据手机号码查询member表
        String telephone = (String) map.get("telephone");
//        String orderDate1 = (String) map.get("orderDate");
        String setmealId = (String) map.get("setmealId");
        Member member = memberDao.findByTelephone(telephone);

        if (member != null) {//根据手机号判断会员是否存在
            int memberId = member.getId();
            Order order = new Order(memberId, date, Integer.parseInt(setmealId));

            List<Order> byCondition = orderDao.findByCondition(order);
            if (byCondition != null && byCondition.size() > 0) {
                return new Result(false, MessageConstant.HAS_ORDERED);

            }

        }
//        预约人数+1；感觉这一句可以放在memeber的两个判断完之后
        orderSetting.setReservations(reservations + 1);
//         更新预约设置
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        //        4.若第三步没有查出来就不是会员，不是会员就自动注册

        if (member == null) {//不是会员
            member = new Member();//这个可以不用创建吧
            //name,telephone,validateCode,idCard,orderDate,sex,setmealId
            // private String fileNumber;//档案号
            //    private String name;//姓名
            //    private String sex;//性别
            //    private String idCard;//身份证号
            //    private String phoneNumber;//手机号
            //    private Date regTime;//注册时间
            //    private String password;//登录密码
            //    private String email;//邮箱
            //    private Date birthday;//出生日期
            //    private String remark;//备注
            String name = (String) map.get("name");
            String idCard = (String) map.get("idCard");
            String sex = (String) map.get("sex");
            member.setName(name);
            member.setPhoneNumber(telephone);
            member.setIdCard(idCard);
            member.setSex(sex);
            member.setRegTime(new Date());
            memberDao.add(member);
        }
//        private Integer memberId;//会员id
//        private Date orderDate;//预约日期
//        private String orderType;//预约类型 电话预约/微信预约
//        private String orderStatus;//预约状态（是否到诊）
//        private Integer setmealId;//体检套餐id
        Order order = new Order(member.getId(), date, (String) map.get("orderType"), Order.ORDERSTATUS_NO, Integer.parseInt(setmealId));
        orderDao.add(order);
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS, order.getId());
    }

    @Override
    public Result findById(Integer id) {
//                            <p>体检人：{{orderInfo.member}}</p> 通过order的memberId找member表
//                            <p>体检套餐：{{orderInfo.setmeal}}</p>通过order的setmealId找setmeal表
//                            <p>体检日期：{{orderInfo.orderDate}}</p>order表orderDate
//                            <p>预约类型：{{orderInfo.orderType}}</p>order表orderType

//        通过orderId获取memberId
        try {
            Map byId4Detail = orderDao.findById4Detail(id);//本来需要查询3个dao，但是这个orderDao中查询了3个表直接获得结果。

            return new Result(true, MessageConstant.ORDER_SUCCESS,byId4Detail);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
    }
}
