package study.springboot.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class FileuploadController {

    @GetMapping("/upload")
    public void upload(@RequestParam("head_img") MultipartFile file, HttpServletRequest request) {
        log.info("hello...{}", "str");
        String fileName = file.getOriginalFilename();
        file.getSize();
        log.info("{}", fileName);
    }
}
