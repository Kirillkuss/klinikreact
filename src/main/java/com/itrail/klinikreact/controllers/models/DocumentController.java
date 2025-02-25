package com.itrail.klinikreact.controllers.models;

import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.models.Document;
import com.itrail.klinikreact.repositories.DocumentRepository;
import com.itrail.klinikreact.rest.models.IDocument;
import com.itrail.klinikreact.services.model.DocumentService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class DocumentController implements IDocument {
    
    private final DocumentService documentService;
    private final DocumentRepository documentRepository;

    @Override
    public Mono<Document> addDocument(Document document) {
        return documentService.addDocument( document );
    }

    @Override
    public Flux<Document> findByWord(String word) {
        return documentService.findByParam(word, 1, 40 );
    }

    @Override
    public Flux<Document> getLazyDocument(int page, int size) {
        return documentService.getLazyDocuments( page, size );
    }

    @Override
    public Mono<Long> getCountDocument() {
        return documentRepository.count();
    }
    
}
