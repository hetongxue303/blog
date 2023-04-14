package com.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 查询条件
 *
 * @author hy
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Schema(name = "查询条件")
public class SearchVo implements Serializable {

    @Schema(title = "当前页")
    private Long current;
    @Schema(title = "页面条数")
    private Long size;

    @Schema(title = "标签名称")
    private String tagName;
}