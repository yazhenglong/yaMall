package com.infosys.ymall.utils;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PmsUploadUtil {


    public static String uploadImage(MultipartFile multipartFile){
        String imageUrl="http://192.168.159.129";

        //fdfs的全局链接地址
        String tracker = PmsUploadUtil.class.getResource("/tracker.conf").getPath();

        try {
            ClientGlobal.init(tracker);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TrackerClient trackerClient = new TrackerClient();

        //获得一个trackerService的实例
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //通过tracker获得一个storage链接客户端
        StorageClient storageClient = new StorageClient(trackerServer,null);  //不知道storage数量不指定storage

        try {
            byte[] bytes = multipartFile.getBytes();

            String originalFilename = multipartFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);

            String[] uploadInfos = storageClient.upload_file(bytes,extName,null);


            for (String uploadInfo : uploadInfos) {
                imageUrl += "/"+uploadInfo;
            }

            return imageUrl;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageUrl;
    }



}
