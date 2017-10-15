package cir.cirviz.api.controller;

import cir.cirviz.api.service.KeyPhraseService;
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
public class KeyPhrasesApiController implements KeyPhrasesApi {

  private final KeyPhraseService keyPhraseService;
  private final PaperService paperService;

  private final ModelComparators modelComparators;

  @Autowired
  public KeyPhrasesApiController(
      KeyPhraseService keyPhraseService,
      PaperService paperService,
      ModelComparators modelComparators
  ) {
    this.keyPhraseService = keyPhraseService;
    this.paperService = paperService;
    this.modelComparators = modelComparators;
  }

  @GetMapping("/api/keyPhrases")
  @Override
  public List<String> getKeyPhrases(
      @RequestParam(name = "orderBy", required = false, defaultValue = "keyPhrase") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<String> keyPhrases = keyPhraseService.getKeyPhrases().stream();
    StreamModifier<String> modifier = new StreamModifier<>(
        modelComparators.getKeyPhraseComparatorMap());
    keyPhrases = modifier.modify(keyPhrases, orderBy, asc, limit);
    return keyPhrases.collect(Collectors.toList());
  }

  @GetMapping("/api/keyPhrases/count")
  @Override
  public long getKeyPhrasesCount() {
    return keyPhraseService.getKeyPhrases().size();
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}")
  @Override
  public String getKeyPhrase(@PathVariable(name = "keyPhrase") String keyPhrase) {
    List<String> keyPhrases = keyPhraseService.getKeyPhrases();
    if (keyPhrases.contains(keyPhrase)) {
      return keyPhrase;
    } else {
      throw new NotFoundException("KeyPhrase: " + keyPhrase + "is not found.");
    }
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/papers")
  @Override
  public List<Paper> getPapersByKeyPhrase(
      @PathVariable(name = "keyPhrase") String keyPhrase,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream();
    StreamModifier<Paper> modifier = new StreamModifier<>(
        modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/papers/count")
  @Override
  public long getPapersCountByKeyPhrase(@PathVariable(name = "keyPhrase") String keyPhrase) {
    return keyPhraseService.getPapersByKeyPhrase(keyPhrase).size();
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/authors")
  @Override
  public List<Author> getAuthorsByKeyPhrase(
      @PathVariable(name = "keyPhrase") String keyPhrase,
      @RequestParam(name = "orderBy", required = false, defaultValue = "name") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Author> authors = keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream()
        .flatMap(paper -> paper.getAuthors().stream())
        .distinct();
    StreamModifier<Author> modifier = new StreamModifier<>(
        modelComparators.getAuthorComparatorMap());
    authors = modifier.modify(authors, orderBy, asc, limit);
    return authors.collect(Collectors.toList());
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/authors/count")
  @Override
  public long getAuthorsCountByKeyPhrase(@PathVariable(name = "keyPhrase") String keyPhrase) {
    return keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream()
        .flatMap(paper -> paper.getAuthors().stream())
        .distinct()
        .count();
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/years")
  @Override
  public List<Integer> getYearsByKeyPhrase(
      @PathVariable(name = "keyPhrase") String keyPhrase,
      @RequestParam(name = "orderBy", required = false, defaultValue = "year") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Integer> years = keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream()
        .map(Paper::getYear)
        .distinct();
    StreamModifier<Integer> modifier = new StreamModifier<>(
        modelComparators.getYearComparatorMap());
    years = modifier.modify(years, orderBy, asc, limit);
    return years.collect(Collectors.toList());
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/years/count")
  @Override
  public long getYearsCountByKeyPhrase(@PathVariable(name = "keyPhrase") String keyPhrase) {
    return keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream()
        .map(Paper::getYear)
        .distinct()
        .count();
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/venues")
  @Override
  public List<String> getVenuesByKeyPhrase(
      @PathVariable(name = "keyPhrase") String keyPhrase,
      @RequestParam(name = "orderBy", required = false, defaultValue = "year") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<String> venues = keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream()
        .map(Paper::getVenue)
        .distinct();
    StreamModifier<String> modifier = new StreamModifier<>(
        modelComparators.getVenueComparatorMap());
    venues = modifier.modify(venues, orderBy, asc, limit);
    return venues.collect(Collectors.toList());
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/venues/count")
  @Override
  public long getVenuesCountByKeyPhrase(@PathVariable(name = "keyPhrase") String keyPhrase) {
    return keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream()
        .map(Paper::getVenue)
        .distinct()
        .count();
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/outCitations")
  @Override
  public List<Paper> getCitationsByKeyPhrase(
      @PathVariable(name = "keyPhrase") String keyPhrase,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream()
        .flatMap(paper -> paperService.getOutCitationsByPaper(paper).stream())
        .distinct();
    StreamModifier<Paper> modifier = new StreamModifier<>(modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @GetMapping("/api/keyPhrases/{keyPhrases}/outCitations/count")
  @Override
  public long getCitationsCountByKeyPhrase(@PathVariable(name = "keyPhrase") String keyPhrase) {
    return keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream()
        .flatMap(paper -> paperService.getOutCitationsByPaper(paper).stream())
        .distinct()
        .count();
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/inCitations")
  @Override
  public List<Paper> getPapersCitingPapersWithKeyPhrase(
      @PathVariable(name = "keyPhrase") String keyPhrase,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream()
        .flatMap(paper -> paperService.getInCitationsByPaper(paper).stream())
        .distinct();
    StreamModifier<Paper> modifier = new StreamModifier<>(modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/inCitations/count")
  @Override
  public long getPapersCountCitingPapersWithKeyPhrase(
      @PathVariable(name = "keyPhrase") String keyPhrase) {
    return keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream()
        .flatMap(paper -> paperService.getInCitationsByPaper(paper).stream())
        .distinct()
        .count();
  }
}
