package org.zerock.ex1.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//별도의 화면없이 데이터 전송(REST API)
@RestController
public class SampleController {
    //브라우저의 주소창에서 호출이 가능하도록 설정
    @GetMapping("/hello")
    public String[] hello(){
        return new String[]{"Hello","World"};
    }
}
// 별도의 설정없이도 json형태로 데이터가 전송