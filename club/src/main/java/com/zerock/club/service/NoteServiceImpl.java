package com.zerock.club.service;

import com.zerock.club.dto.NoteDTO;
import com.zerock.club.entity.Note;
import com.zerock.club.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService{

    private final NoteRepository noteRepository;

    @Override
    public Long register(NoteDTO noteDTO) {

        Note note = dtoToEntity(noteDTO);

        log.info("=======================");
        log.info(note);

        noteRepository.save(note);

        return note.getNum();
    }

    @Override
    public NoteDTO get(Long num) {

        Optional<Note> result = noteRepository.getWithWriter(num);
        if(result.isPresent()){
            return entityToDTO(result.get());
        }
        return null;
    }

    @Override
    public void modify(NoteDTO noteDTO) {

        Long num = noteDTO.getNum();
        Optional<Note> result = noteRepository.findById(num);

        if(result.isPresent()){
            Note note = result.get();

            note.changeTitle(noteDTO.getTitle());
            note.changeContent(noteDTO.getContent());

            noteRepository.save(note);
        }

    }

    @Override
    public void remove(Long num) {

        noteRepository.deleteById(num);

    }

    @Override
    public List<NoteDTO> getAllWithWriter(String writerEmail) {

        List<Note> noteList = noteRepository.getList(writerEmail);
        // return noteList.stream().map(note -> entityToDTO(note).collect(Collectors.toList()));}
        // collect(Collectors.toList()))부분이 map함수의 결과를 얻은 스트림을 수집하는 것이기에 map메서드의 외부에 위치해야 함
        return noteList.stream()
                .map(this::entityToDTO)  //this::entityToDTO는 메서드 레퍼런스로, Note를 NoteDTO로 변환하는 메서드를 가리킴
                .collect(Collectors.toList());  // 변환된 NoteDTO들을 리스트로 변환하여 수집
    }

}
