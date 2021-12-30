package com.dun.controller;

import cn.hutool.core.util.IdUtil;
import com.dun.common.lang.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
@RestController
public class FileController {
    /**
     * 文件上传

     * @return
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile picture) {
        System.out.println(picture);
        System.out.println("您已进入图片上传服务");
        //获取文件在服务器的储存位置
        String path = "D://Code//learningpass//learningpass-spring//src//main//resources//static//uploadImg";
        File filePath = new File(path);
        System.out.println("文件的保存路径：" + path);
        if (!filePath.exists() && !filePath.isDirectory()) {
            System.out.println("目录不存在，创建目录:" + filePath);
            filePath.mkdir();
        }

        //获取原始文件名称(包含格式)
        String originalFileName = picture.getOriginalFilename();
        System.out.println("原始文件名称：" + originalFileName);

        //获取文件类型，以最后一个`.`为标识
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        System.out.println("文件类型：" + type);
        //获取文件名称（不包含格式）
        String name = originalFileName.substring(0, originalFileName.lastIndexOf("."));

        String fileName = IdUtil.simpleUUID()  + "." + type;
        System.out.println("新文件名称：" + fileName);

        //在指定路径下创建一个文件
        File targetFile = new File(path, fileName);
        System.out.println("图片地址："+path+"/"+fileName);
        //将文件保存到服务器指定位置
        try {
            picture.transferTo(targetFile);
            System.out.println("上传成功");
            //将文件在服务器的存储路径返回
            return Result.succ(fileName);
        } catch (IOException e) {
            System.out.println("上传失败");
            e.printStackTrace();
            return Result.fail("上传失败");
        }
    }

    /**
     * 显示图片
     * getFeedBackPicture.do?picName=
     * @return
     */

    @RequestMapping("/viewPhoto/{photopath}")
    public void getFeedBackPicture(HttpServletResponse response, @PathVariable("photopath")String photopath) throws Exception{
        String realPath="D://Code//learningpass//learningpass-spring//src//main//resources//static//uploadImg/"+photopath;
        FileInputStream inputStream = new FileInputStream(realPath);
        int i = inputStream.available();
        //byte数组用于存放图片字节数据
        byte[] buff = new byte[i];
        inputStream.read(buff);
        //记得关闭输入流
        inputStream.close();
        //设置发送到客户端的响应内容类型
        response.setContentType("image/*");
        OutputStream out = response.getOutputStream();
        out.write(buff);
        //关闭响应输出流
        out.close();
    }


}
