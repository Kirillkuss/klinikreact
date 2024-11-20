package com.itrail.klinikreact.controllers.models;

import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.models.Document;
import com.itrail.klinikreact.repositories.DocumentRepository;
import com.itrail.klinikreact.response.BaseError;
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

    @ExceptionHandler(Throwable.class)
    public Flux<BaseError> errBaseResponse( Throwable ex ){
        ex.printStackTrace();
        return Flux.just(  new BaseError( 500, ex.getMessage() ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public Flux<BaseError> errBaseResponse( NoSuchElementException ex ){
        return Flux.just( new BaseError( 400, ex.getMessage() ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Flux<BaseError> errBaseResponse( IllegalArgumentException ex ){
        return Flux.just( new BaseError( 404, ex.getMessage() ));
    }

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
