package study.springboot.mvc.support.exception;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 1.可以处理非Controller层抛出的异常，例如404
 * 2.如果没有配置ErrorController, SpringBoot会通过ErrorMvcAutoConfiguration自动配置一个，默认的实现类为 BasicErrorController。
 */
@Slf4j
@RestController
public class GlobalErrorController implements ErrorController {

    private static final String PATH_ERROR = "/error";

    private static final String KEY_STATUS_CODE = "javax.servlet.error.status_code";

    private static final String KEY_EXCEPTION = "javax.servlet.error.exception";

    private static final String KEY_MESSAGE = "javax.servlet.error.message";

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return PATH_ERROR;
    }

    @RequestMapping(value = PATH_ERROR, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity errorData(HttpServletRequest request) {
        int statusCode = (int) request.getAttribute(KEY_STATUS_CODE);
        Exception ex = (Exception) request.getAttribute(KEY_EXCEPTION);
        String msg = (String) request.getAttribute(KEY_MESSAGE);
        String ur = (String) request.getAttribute("raw_url");
        Map<String, Object> data = Maps.newHashMap();
        if (500 == statusCode) {
            data.put("code", "9999");
            data.put("desc", "系统异常");
        } else if (404 == statusCode) {
            data.put("code", "9999");
            data.put("desc", "非法url");
        }
        ResponseEntity entity = new ResponseEntity(data, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = PATH_ERROR, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorPage(HttpServletRequest request) {
        return new ModelAndView("globalError");
    }
//
//    private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
//        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
//        Map<String, Object> map = errorAttributes.getErrorAttributes(requestAttributes);
//        if (request.getAttribute("status") instanceof Integer) {
//            map.put("status", request.getAttribute("status"));
//        }
//        map.put("url", request.getRequestURL().toString());
//        map.putIfAbsent("path", request.getAttribute("raw_url"));
//        return map;
//    }

}
