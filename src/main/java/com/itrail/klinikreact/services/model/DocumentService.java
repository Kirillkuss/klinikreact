package com.itrail.klinikreact.services.model;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.itrail.klinikreact.models.Document;
import com.itrail.klinikreact.repositories.DocumentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DocumentService {
    
    private final DocumentRepository documentRepository;

    /**
     * Проверка при добавлении документа
     * @param document - входной параметр 
     * @return Mono Void
     */
    private Mono<Void> checkDocumentUniqueness(Document document) {
        return documentRepository.findDocumentByNumar( document.getNumar() )
                .flatMap( foundDocument -> Mono.error( new IllegalArgumentException("Документ с таким номером документа уже существует" )))
                .then(documentRepository.findDocumentByPolis( document.getPolis() )
                        .flatMap( foundDocument -> Mono.error( new IllegalArgumentException("Документ с таким полисом уже существует" )))
                        .then(documentRepository.findDocumentBySnils( document.getSnils() )
                                .flatMap( foundDocument -> Mono.error( new IllegalArgumentException("Документ с таким СНИЛСом уже существует" )))));
    }
    /**
     * Додавление документа
     * @param document - входной параметр
     * @return Mono Document
     */
    public Mono<Document> addDocument( Document document ) {
        return checkDocumentUniqueness(document)
                .then( documentRepository.save( document ));
    }
    /**
     * Параллельная проверка при добавлении документа
     * @param document - входной параметр 
     * @return Mono Document
     */
    public Mono<Document> addDocument2(Document document) {
        return Mono.zip(
                documentRepository.findDocumentByNumar(document.getNumar()),
                documentRepository.findDocumentByPolis(document.getPolis()),
                documentRepository.findDocumentBySnils(document.getSnils())
        ).flatMap(tuple -> {
            if (tuple.getT1() != null || tuple.getT2() != null || tuple.getT3() != null) {
                return Mono.error(new IllegalArgumentException("Документ с такими данными уже существует"));
            }
            return documentRepository.save(document);
        });
    }

    public Flux<Document> findByParam(String param, int page, int size ){
        return documentRepository.findByNSP( param  + "%" )
                                 .skip((page - 1) * size)
                                 .take(size)
                                 .switchIfEmpty( Flux.error( new NoSuchElementException( "По данному запросу ничего не найдено")));
    }

    public Flux<Document> getLazyDocuments(int page, int size){
        return documentRepository.findAll()
                                 .skip((page - 1) * size)
                                 .take(size);
    }
}
