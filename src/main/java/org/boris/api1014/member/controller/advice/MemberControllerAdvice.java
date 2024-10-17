package org.boris.api1014.member.controller.advice;


import lombok.extern.log4j.Log4j2;
import org.boris.api1014.member.exception.MemberTaskException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class MemberControllerAdvice {
    //MemberTaskException 발생하면.
    @ExceptionHandler(MemberTaskException.class)
    public ResponseEntity<Map<String, Object>> ex(MemberTaskException ex) {
        log.error("====================================================");

        StackTraceElement[] arr = ex.getStackTrace();

        for (StackTraceElement ste : arr) {
            log.error(ste.toString());
        }
        log.error("====================================================");

        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("message", ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(msgMap);
    }
}
