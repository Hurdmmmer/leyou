package com.leyou.service;

import com.leyou.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用于专门处理静态化页面的 service
 * @author shen youjian
 * @date 2018/7/30 13:54
 */
@Service
public class StaticPageService {

    @Autowired
    private ItemPageService itemPageService;
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${ly.thymeleaf.destPath}")
    private String destPath;

    /**
     *  页面静态化, 使用 thymeleaf 解析模板并存储到指定的目录下
     */

    public void writeHtml(Long spuId) throws Exception {
        // 需要的数据
        //1. thymeleaf 的 Context
        Context context = new Context();
        // 获取填充到thymeleaf模板中的数据, 数据接收一个 map 集合
        context.setVariables(itemPageService.builderGoods(spuId));
        //2. Thymeleaf 引擎
        // 创建输出流，关联到一个临时文件
        File temp = new File(spuId + ".html");
        // 目标页面文件
        File dest = createPath(spuId);
        // 备份原页面文件
        File bak = new File(spuId + "_bak.html");
        try (PrintWriter writer = new PrintWriter(temp, "UTF-8")) {
            // 利用thymeleaf模板引擎生成 静态页面
            templateEngine.process("item", context, writer);

            if (dest.exists()) {
                // 如果目标文件已经存在，先备份
                dest.renameTo(bak);
            }
            // 将新页面覆盖旧页面
            FileCopyUtils.copy(temp,dest);
            // 成功后将备份页面删除
            bak.delete();
        } catch (IOException e) {
            // 失败后，将备份页面恢复
            bak.renameTo(dest);
            // 重新抛出异常，声明页面生成失败
            throw new Exception(e);
        } finally {
            // 删除临时页面
            if (temp.exists()) {
                temp.delete();
            }
        }

    }
    /** 根据id判断该id对应的文件是否存在, 如果存在则不创建新的文件, 返回存在的文件*/
    private File createPath(Long id) {
        if (id == null) {
            return null;
        }
        File dest = new File(this.destPath);
        if (!dest.exists()) {
            dest.mkdirs();
        }
        return new File(dest, id + ".html");
    }

    /**
     * 判断某个商品的页面是否存在
     * @param id
     * @return
     */
    public boolean exists(Long id){
        return this.createPath(id).exists();
    }

    /**
     * 异步创建html页面
     * @param id
     */
    public void syncCreateHtml(Long id){
        ThreadUtils.execute(() -> {
            try {
                writeHtml(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /** 根据商品id删除页面 */
    public void deleteHtml(Long id) {
        File path = createPath(id);
        if (path.exists()) {
            path.delete();
        }
    }
}
