package com.yyh.backendjava.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyh
 * @since 2024-04-28
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String userName;

    private String userPassword;

    private Integer userType;

    private String userEmail;

    private Integer deleted;

    /**
     * 加盐
     */
    private String userSalt;

    private String userPhone;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
    public String getUserSalt() {
        return userSalt;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Override
    public String toString() {
        return "User{" +
            "userId=" + userId +
            ", userName=" + userName +
            ", userPassword=" + userPassword +
            ", userType=" + userType +
            ", userEmail=" + userEmail +
            ", deleted=" + deleted +
            ", userSalt=" + userSalt +
            ", userPhone=" + userPhone +
        "}";
    }
}
