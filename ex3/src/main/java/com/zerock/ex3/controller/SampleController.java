package com.zerock.ex3.controller;

import org.springframework.ui.Model;
import com.zerock.ex3.dto.SampleDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sample")
@Log4j2  //동작을 확인하기 위해 적용
public class SampleController {
    @GetMapping("/ex1")
    public void ex1(){

        log.info("...........ex1.............");

    }

    //Thymeleaf에서의 반복문 처리
    @GetMapping({"/ex2","/exLink"})
    public void exModel(Model model){  //SampleDTO타입의 객체 20개 추가해서 Model에 담아 전송
        List<SampleDTO> list = IntStream.rangeClosed(1,20).asLongStream().mapToObj(i -> {
            SampleDTO dto = SampleDTO.builder()
                    .sno(i)
                    .first("First.."+i)
                    .last("Last.."+i)
                    .regTime(LocalDateTime.now())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("list",list);
    }

    //Thymeleaf 의 inline 속성
    @GetMapping({"/exInline"})
    public String exInline(RedirectAttributes redirectAttributes){

        log.info(".....exInline....");

        SampleDTO dto = SampleDTO.builder()
                .sno(100L)
                .first("First..100")
                .last("Last..100")
                .regTime(LocalDateTime.now())
                .build();
        redirectAttributes.addFlashAttribute("result","success");
        redirectAttributes.addFlashAttribute("dto",dto);

        return "redirect:/sample/ex3";
    }

    //'/exInline' 호출하면 리다이렉트되어 '/ex3'을 호출
    @GetMapping("/ex3")
    public void ex3(){

        log.info("....ex3....");

    }

    //Thymeleaf의 레이아웃
    //1. include 방식의 처리
    //2. 파라미터 방식의 처리
    //3. 레이아웃 템플릿 만들기
    @GetMapping({"/exLayout1","/exLayout2", "/exTemplate"})
    public void exLayout1(){

        log.info("....exLayout....");

    }
}
