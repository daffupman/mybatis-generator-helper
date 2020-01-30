package io.daff.generate.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="user表的实体类")
@Table(name = "`user`")
public class User implements Serializable {
    /**
     * 主键id
     */
    @Id
    @Column(name = "`id`")
    @ApiModelProperty(value="主键id")
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "`username`")
    @ApiModelProperty(value="用户名")
    private String username;

    /**
     * 年龄
     */
    @Column(name = "`age`")
    @ApiModelProperty(value="年龄")
    private Integer age;

    /**
     * M：男性，F：女性，S：保密
     */
    @Column(name = "`gender`")
    @ApiModelProperty(value="M：男性，F：女性，S：保密")
    private String gender;

    private static final long serialVersionUID = 1L;
}