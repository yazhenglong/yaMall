package com.infosys.ymall.manage;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YamallManageWebApplicationTests {

	@Test
	public void contextLoads() throws IOException, MyException {
        //fdfs的全局链接地址
        String tracker = YamallManageWebApplicationTests.class.getResource("/tracker.conf").getPath();

        ClientGlobal.init(tracker);

        TrackerClient trackerClient = new TrackerClient();

        //获得一个trackerService的实例
        TrackerServer trackerServer = trackerClient.getConnection();

        //通过tracker获得一个storage链接客户端
        StorageClient storageClient = new StorageClient(trackerServer,null);  //不知道storage数量不指定storage

        String[] uploadInfo = storageClient.upload_file("f:/abc.jpg", "jpg", null);
        System.out.println("================================================");
        for (String str : uploadInfo){
            System.out.println(str);
        }
        System.out.println("================================================");
	}

}
