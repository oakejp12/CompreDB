package document;

import com.joakes.knowledgedbservice.document.Document;
import com.joakes.knowledgedbservice.document.dto.DocumentCreationDTO;
import com.joakes.knowledgedbservice.document.dto.DocumentUpdateDTO;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DocumentTest {
    private static final ModelMapper mapper = new ModelMapper();

    @Test
    public void checkDocumentMapping() {
        DocumentCreationDTO creationDTO = new DocumentCreationDTO();
        creationDTO.setName("Red Blob Games");
        creationDTO.setSource("Bookmark");
        creationDTO.setUrl("https://www.redblobgames.com");

        Document document = mapper.map(creationDTO, Document.class);

        assertEquals(creationDTO.getName(), document.getName());
        assertEquals(creationDTO.getUrl(), document.getUrl());
        assertEquals(creationDTO.getSource(), document.getSource());
        assertNotNull(document.getCreatedAt());
        assertNotNull(document.getModifiedAt());

        DocumentUpdateDTO updateDTO = new DocumentUpdateDTO();
        updateDTO.setId(4L);
        updateDTO.setName("Z Algorithm - Codeforces");
        updateDTO.setSource("Bookmark");
        updateDTO.setUrl("https://www.codeforces.com/blog/entry/3107");

        mapper.map(updateDTO, document);

        assertEquals(updateDTO.getName(), document.getName());
        assertEquals(updateDTO.getUrl(), document.getUrl());
        assertEquals(updateDTO.getSource(), document.getSource());
        assertNotNull(document.getCreatedAt());
        assertNotNull(document.getModifiedAt());
    }

}
