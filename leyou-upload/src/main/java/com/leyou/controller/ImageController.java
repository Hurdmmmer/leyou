package com.leyou.controller;

import com.leyou.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *  图片上传的控制器
 * @author shen youjian
 * @date 2018/7/21 15:36
 */
@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * 图片上传
     */
    @RequestMapping("image")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile multipartFile) {
        String url = this.imageService.upload(multipartFile);
        if (url == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(url);
    }

    /**
     * 删除图片
     */
    @DeleteMapping("/image")
    public ResponseEntity<Void> delete(@RequestParam("id") String id) {

        System.out.println("id = " + id);


        try {
           this.imageService.deleteImageById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }




}
