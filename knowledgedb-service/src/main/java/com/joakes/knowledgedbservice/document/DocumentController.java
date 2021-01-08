package com.joakes.knowledgedbservice.document;

import com.joakes.knowledgedbservice.document.dto.DocumentCreationDTO;
import com.joakes.knowledgedbservice.document.dto.DocumentUpdateDTO;
import com.joakes.knowledgedbservice.util.DTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RequestMapping("/v1/documents")
@RequiredArgsConstructor
@RestController
public class DocumentController {

    private final DocumentRepository repository;

    @GetMapping
    public Iterable<Document> getAllDocuments() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        Optional<Document> document = repository.findById(id);
        return document.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    public ResponseEntity<Document> createDocument(@DTO(DocumentCreationDTO.class) Document document) throws URISyntaxException {
        Document result = repository.save(document);
        return ResponseEntity.created(new URI("/v1/documents/" + result.getId()))
                .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@DTO(DocumentUpdateDTO.class) Document document) {
        return ResponseEntity.ok(repository.save(document));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
