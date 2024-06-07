package com.yyh.backendjava.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyh
 * @since 2024-04-30
 */
@TableName("do_test")
public class DoTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("testId")
    private Integer testId;
    @JsonProperty("selectAns")
    private String selectAns;
    @JsonProperty("blockAns")
    private String blockAns;
    @JsonProperty("score")
    private Float score;
    @JsonProperty("isFinished")
    private Integer isFinished;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }
    public String getSelectAns() {
        return selectAns;
    }

    public void setSelectAns(String selectAns) {
        this.selectAns = selectAns;
    }
    public String getBlockAns() {
        return blockAns;
    }

    public void setBlockAns(String blockAns) {
        this.blockAns = blockAns;
    }
    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
    public Integer getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Integer isFinished) {
        this.isFinished = isFinished;
    }

    @Override
    public String toString() {
        return "DoTest{" +
            "id=" + id +
            ", userId=" + userId +
            ", testId=" + testId +
            ", selectAns=" + selectAns +
            ", blockAns=" + blockAns +
            ", score=" + score +
            ", isFinished=" + isFinished +
        "}";
    }
}
