package cir.cirviz.api.controller;

import cir.cirviz.api.service.PaperService;
import cir.cirviz.api.util.ModelComparators;
import cir.cirviz.api.util.NotFoundException;
import cir.cirviz.api.util.StreamModifier;
import cir.cirviz.data.entity.Author;
import cir.cirviz.data.entity.Paper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaperApiController implements PaperApi {

  private final PaperService paperService;
  private final ModelComparators modelComparators;

  @Autowired
  public PaperApiController(PaperService paperService,
      ModelComparators modelComparators) {
    this.paperService = paperService;
    this.modelComparators = modelComparators;
  }

  @GetMapping("/api/papers")
  @Override
  public List<Paper> getPapers(
      @RequestParam(name = "title", required = false) String title,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = paperService.getPapers().stream();
    if (title != null && !title.isEmpty()) {
      papers = papers
          .filter(paper -> paper.getTitle().toLowerCase().contains(title.toLowerCase()))
          .distinct();
    }
    StreamModifier<Paper> modifier = new StreamModifier<>(modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @GetMapping("/api/papers/count")
  @Override
  public long getPapersCount() {
    return paperService.getPapers().size();
  }

  @GetMapping("/api/papers/{id}")
  @Override
  public Paper getPaperById(@PathVariable(name = "id") String paperId) {
    Paper paper = paperService.getPaperById(paperId);
    if (paper == null) {
      throw new NotFoundException("Paper with id: " + paperId + " is not found.");
    }
    return paper;
  }

  @GetMapping("/api/papers/{id}/authors")
  @Override
  public List<Author> getAuthorsOfPaper(
      @PathVariable(name = "id") String paperId,
      @RequestParam(name = "orderBy", required = false, defaultValue = "name") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Author> authors = getPaperById(paperId).getAuthors().stream();
    StreamModifier<Author> modifier = new StreamModifier<>(
        modelComparators.getAuthorComparatorMap());
    authors = modifier.modify(authors, orderBy, asc, limit);
    return authors.collect(Collectors.toList());
  }

  @GetMapping("/api/papers/{id}/authors/count")
  @Override
  public long getAuthorsCountOfPaper(@PathVariable(name = "id") String paperId) {
    return getPaperById(paperId).getAuthors().size();
  }

  @GetMapping("/api/papers/{id}/year")
  @Override
  public int getYearOfPaper(@PathVariable(name = "id") String paperId) {
    return getPaperById(paperId).getYear();
  }

  @GetMapping("/api/papers/{id}/keyPhrases")
  @Override
  public List<String> getKeyPhrasesOfPaper(
      @PathVariable(name = "id") String paperId,
      @RequestParam(name = "orderBy", required = false, defaultValue = "keyPhrase") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<String> keyPhrases = getPaperById(paperId).getKeyPhrases().stream();
    StreamModifier<String> modifier = new StreamModifier<>(
        modelComparators.getKeyPhraseComparatorMap());
    keyPhrases = modifier.modify(keyPhrases, orderBy, asc, limit);
    return keyPhrases.collect(Collectors.toList());
  }

  @GetMapping("/api/papers/{id}/keyPhrases/count")
  @Override
  public long getKeyPhrasesCountOfPaper(@PathVariable(name = "id") String paperId) {
    return getPaperById(paperId).getKeyPhrases().size();
  }

  @GetMapping("/api/papers/{id}/venue")
  @Override
  public String getVenueOfPaper(@PathVariable(name = "id") String paperId) {
    return getPaperById(paperId).getVenue();
  }

  @GetMapping("/api/papers/{id}/outCitations")
  @Override
  public List<Paper> getOutCitationsOfPaper(
      @PathVariable(name = "id") String paperId,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = paperService.getOutCitationsByPaperId(paperId).stream();
    StreamModifier<Paper> modifier = new StreamModifier<>(modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @GetMapping("/api/papers/{id}/outCitations/count")
  @Override
  public long getOutCitationsCountOfPaper(@PathVariable(name = "id") String paperId) {
    return paperService.getOutCitationsByPaperId(paperId).size();
  }

  @GetMapping("/api/papers/{id}/inCitations")
  @Override
  public List<Paper> getInCitationsOfPaper(
      @PathVariable(name = "id") String paperId,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = paperService.getInCitationsByPaperId(paperId).stream();
    StreamModifier<Paper> modifier = new StreamModifier<>(modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @Override
  public long getInCitationsCountOfPaper(String paperId) {
    return paperService.getInCitationsByPaperId(paperId).size();
  }
}
