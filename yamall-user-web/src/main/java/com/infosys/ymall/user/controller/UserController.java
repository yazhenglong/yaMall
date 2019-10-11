package com.infosys.ymall.user.controller;




import com.alibaba.dubbo.config.annotation.Reference;
import com.infosys.ymall.bean.UmsMember;
import com.infosys.ymall.bean.UmsMemberReceiveAddress;
import com.infosys.ymall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    @Reference
    UserService userService;



    @RequestMapping("getReceiveAddressByMemberId")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getReceiveAddressByUemberId(String MenberId){

        List<UmsMemberReceiveAddress> UmsMemberReceiveAddresses = userService.getReceiveAddressByUemberId(MenberId);

        return UmsMemberReceiveAddresses;
    }

    @RequestMapping("getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(){

        List<UmsMember> umsNumbers = userService.getAllUser();

        return umsNumbers;
    }


    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return "holle user";
    }

}
