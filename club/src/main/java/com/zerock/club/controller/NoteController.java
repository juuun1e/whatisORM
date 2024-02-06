package com.zerock.club.controller;

import com.zerock.club.dto.NoteDTO;
import com.zerock.club.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/notes/")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping(value = "")           //@RequestBody를 이용해 JSON데이터를 받아 NoteDTO로 변환할 수 있도록 처리
    public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO){

        log.info("======================================");
        log.info(noteDTO);

        Long num = noteService.register(noteDTO);

        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    // read() : @PathVariable를 이용해서 경로에 있는 note의 num을 얻어 해당 Note정보를 반환
    @GetMapping(value = "/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteDTO> read(@PathVariable("num") Long num){

        log.info("------------read-------------");
        log.info(num);

        return new ResponseEntity<>(noteService.get(num), HttpStatus.OK);
    }

    // getList() : 특정이메일을 가진 회원이 작성한 모든 Note를 조회할 수 있는 기능 구현
    // 파라미터로 전달되는 이메일 주소를 통해 해당 회원이 작성한 모든 Note에 대한 정보를 JSON으로 변환
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NoteDTO>> getList(String email){

        log.info("------------getList-------------");
        log.info(email);

        return new ResponseEntity<>(noteService.getAllWithWriter(email), HttpStatus.OK);
    }

    // remove()
    @DeleteMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> remove(@PathVariable("num")Long num){

        log.info("------------remove-------------");
        log.info(num);

        return new ResponseEntity<>("removed", HttpStatus.OK);
    }

    // modify() : 등록과 달리 JSON데이터에 num 속성을 포함하여 전송
    @PutMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> modify(@RequestBody NoteDTO noteDTO){

        log.info("------------modify-------------");
        log.info(noteDTO);

        return new ResponseEntity<>("modified", HttpStatus.OK);
    }
}
