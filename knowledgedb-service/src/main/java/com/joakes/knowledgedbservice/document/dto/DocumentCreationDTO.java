package com.joakes.knowledgedbservice.document.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class DocumentCreationDTO {
    @NotNull
    private String name;
    @NotNull
    private String url;
    @NotNull
    private String source;

    @JsonIgnore
    private final LocalDateTime createdAt = LocalDateTime.now();
    @JsonIgnore
    private final LocalDateTime modifiedAt = LocalDateTime.now();
}
