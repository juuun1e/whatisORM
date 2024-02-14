package com.zerock.where.service;

import com.zerock.where.dto.NoteDTO;
import com.zerock.where.entity.ClubMember;
import com.zerock.where.entity.Notices;

import java.util.List;

public interface NoteService {
    Long register(NoteDTO noteDTO);
    NoteDTO get(Long num);

    void modify(NoteDTO noteDTO);

    void remove(Long num);

    List<NoteDTO> getAllWithWriter(String writerEmail);

    default Notices dtoToEntity(NoteDTO noteDTO){
        Notices notices = Notices.builder()
                .num(noteDTO.getNum())
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .writer(ClubMember.builder().email(noteDTO.getWriterEmail()).build())
                .build();
        return notices;
    }

    default NoteDTO entityToDTO(Notices notices){

        NoteDTO noteDTO = NoteDTO.builder()
                .num(notices.getNum())
                .title(notices.getTitle())
                .content(notices.getTitle())
                .writerEmail(notices.getWriter().getEmail())
                .redDate(notices.getRegDate())
                .modDate(notices.getModDate())
                .build();

        return noteDTO;
    }

}
