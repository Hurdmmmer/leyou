package com.leyou.auth.pojo;

/**
 *  该实体类, 用于存放赋予浏览器的 token 中的数据, 用户的常用不敏感信息
 * @author shen youjian
 * @date 2018/8/2 16:39
 */
public class UserInfo {
    private Long id;
    private String username;

    public UserInfo() {
    }

    public UserInfo(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
