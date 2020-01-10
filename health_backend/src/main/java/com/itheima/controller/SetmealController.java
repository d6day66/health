package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private SetmealService setmealService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        try {
//获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            int lastIndexOf = originalFilename.lastIndexOf(".");
//获取文件后缀
            String suffix = originalFilename.substring(lastIndexOf - 1);
//            注意：别忘了在spring配置文件中配置文件上传组件
//            2.2.4 提交请求
//            当用户点击新增窗口中的确定按钮时发送ajax请求将数据提交到后台进行数据库操作。提
//            交到后台的数据分为两部分：套餐基本信息（对应的模型数据为formData）和检查组id
//            数组（对应的模型数据为checkgroupIds）。
//            为确定按钮绑定单击事件，对应的处理函数为handleAdd
//                    完善handleAdd方法
//使用UUID随机产生文件名称，防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
//图片上传成功
            Result result = new Result(true,
                    MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
            result.setData(fileName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
//图片上传失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal, checkgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
    }
}
