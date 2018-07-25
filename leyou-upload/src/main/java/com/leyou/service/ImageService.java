package com.leyou.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.config.BaseUrlProperties;
import com.leyou.controller.ImageController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 *  图片上传的业务层
 * @author shen youjian
 * @date 2018/7/21 15:39
 */
@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    // 支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private BaseUrlProperties baseUrlProperties;

    /**
     *
     * @param file spring 接受的文件封装对象
     * @return 返回一个url地址, 如果为 null 表示出现错误
     */
    public String upload(MultipartFile file) {
        try {
            // 1、图片信息校验
            // 1)校验文件类型
            String type = file.getContentType();
            if (!suffixes.contains(type)) {
                logger.info("上传失败，文件类型不匹配：{}", type);
                return null;
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                logger.info("上传失败，文件内容不符合要求");
                return null;
            }
            // 图片上传到 fastDFS 分布式文件管理中
            String extName = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");

            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), extName, null);

            return baseUrlProperties.getBaseUrl() + storePath.getFullPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据指定的 id 删除图片
     */
    public void deleteImageById(String id) {
        this.storageClient.deleteFile(id);
    }
}
