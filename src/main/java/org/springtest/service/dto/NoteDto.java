package org.springtest.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springtest.data.entity.User;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {

    private UUID id;
    private String title;
    private String content;
    private User user;
}
