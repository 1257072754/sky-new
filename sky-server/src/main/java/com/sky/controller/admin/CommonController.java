package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliyunOSSOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "文件上传接口")
@Slf4j
public class CommonController {
    @Autowired
    AliyunOSSOperator aliyunOSSOperator;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        try {
            String url = aliyunOSSOperator.upload(file.getBytes(), Objects.requireNonNull(file.getOriginalFilename()));
            log.info("url：{}", url);
            return Result.success(url);
        } catch (Exception e) {
            log.info("e：{}", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
