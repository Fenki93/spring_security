package org.springtest.controller.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springtest.controller.request.CreateNoteRequest;
import org.springtest.controller.request.UpdateNoteRequest;
import org.springtest.controller.response.NoteResponse;
import org.springtest.service.dto.NoteDto;
import org.springtest.service.exception.NoteNotFoundException;
import org.springtest.service.mapper.NoteMapper;
import org.springtest.service.service.NoteService;
import org.springtest.service.service.UserDetailsServiceImpl;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final UserDetailsServiceImpl userService;
    private final NoteService noteService;
    private final NoteMapper noteMapper;

    @GetMapping("/list")
    public ResponseEntity<List<NoteResponse>> noteList(Principal principal) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(noteMapper.toNoteResponses(noteService.listAll(principal.getName())));
}

    @GetMapping("/edit")
    public ResponseEntity<NoteResponse> getNoteById(@RequestParam("id") UUID id) throws NoteNotFoundException {
        NoteDto noteDto = noteService.getById(id);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(noteMapper.toNoteResponse(noteDto));
    }

    @PostMapping("/create")
    public ResponseEntity<NoteResponse> createNote(@Valid @NotNull @RequestBody CreateNoteRequest request, Principal principal) {
        NoteDto noteDto = noteService.add(noteMapper.toNoteDto(request), principal.getName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noteMapper.toNoteResponse(noteDto));
    }

    @PutMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    public void updateNote(
        @RequestBody @Valid @NotNull UpdateNoteRequest request) throws NoteNotFoundException {
        System.out.println("request = " + request);
        noteService.update(noteMapper.toNoteDto(request.getId(), request));
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteNoteById(@RequestBody UpdateNoteRequest request) throws NoteNotFoundException {
        noteService.deleteById(request.getId());
    }
}
