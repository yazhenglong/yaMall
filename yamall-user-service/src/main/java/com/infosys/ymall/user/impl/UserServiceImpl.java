package com.infosys.ymall.user.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.infosys.ymall.bean.UmsMember;
import com.infosys.ymall.bean.UmsMemberReceiveAddress;
import com.infosys.ymall.service.UserService;
import com.infosys.ymall.user.mapper.UmsMemberReceiveAddressMapper;
import com.infosys.ymall.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMember> getAllUser() {

        List<UmsMember> umsNumberList = userMapper.selectAll();
        return umsNumberList;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByUemberId(String menberId) {
       /* UmsMemberReceiveAddress e = new UmsMemberReceiveAddress();
        e.setMemberId(menberId);
        //List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.selectByExample(e);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.select(e);*/

        Example example = new Example(UmsMemberReceiveAddress.class);
        example.createCriteria().andEqualTo("memberId",menberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.selectByExample(example);

        return umsMemberReceiveAddresses;
    }


}
