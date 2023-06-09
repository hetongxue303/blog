package com.blog.controller;

import com.blog.annotation.LogPrint;
import com.blog.domain.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试模块
 *
 * @author hy
 * @version 1.0
 */
@RestController
@Tag(name = "测试模块", description = "测试模块的描述")
public class TestController {

    @GetMapping("test")
    @Operation(summary = "测试方法", description = "测试方法描述")
    @Parameters({@Parameter(name = "name", description = "你的姓名", required = true)})
    @ApiResponses({@ApiResponse(responseCode = "200", description = "请求成功"), @ApiResponse(responseCode = "400", description = "请求失败")})
    @LogPrint("测试接口被调用")
    public Result test() {
        return Result.success("hello,blog!");
    }

}