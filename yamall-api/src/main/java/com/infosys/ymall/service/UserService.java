package com.infosys.ymall.service;

import com.infosys.ymall.bean.UmsMember;
import com.infosys.ymall.bean.UmsMemberReceiveAddress;
import java.util.List;

public interface UserService {

    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByUemberId(String menberId);


}
