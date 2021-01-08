package com.joakes.knowledgedbservice.document.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class DocumentUpdateDTO {
    // Used to fetch instances of existing entities persisted in the DB with the value of these @Ids.
    // For DTOs that do not include @Id properties, we will simply generate new entities based on the values sent,
    // without querying the DB
    @Id
    @NotNull
    private Long id;

    @NotNull
    private String name;
    @NotNull
    private String url;
    @NotNull
    private String source;

    @JsonIgnore
    private final LocalDateTime modifiedAt = LocalDateTime.now();
}
