package cir.cirviz.api.controller;

import cir.cirviz.api.service.AuthorService;
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
public class AuthorApiController implements AuthorApi {

  private final AuthorService authorService;
  private final PaperService paperService;
  private final ModelComparators modelComparators;

  @Autowired
  public AuthorApiController(AuthorService authorService,
      PaperService paperService,
      ModelComparators modelComparators) {
    this.authorService = authorService;
    this.paperService = paperService;
    this.modelComparators = modelComparators;
  }

  @GetMapping("/api/authors")
  @Override
  public List<Author> getAuthors(
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "orderBy", required = false, defaultValue = "name") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Author> authors = authorService.getAuthors().stream();
    if (name != null && !name.isEmpty()) {
      authors = authors
          .filter(author -> author.getName().toLowerCase().contains(name.toLowerCase()))
          .distinct();
    }
    StreamModifier<Author> modifier = new StreamModifier<>(
        modelComparators.getAuthorComparatorMap());
    authors = modifier.modify(authors, orderBy, asc, limit);
    return authors.collect(Collectors.toList());
  }

  @GetMapping("/api/authors/count")
  @Override
  public long getAuthorsCount() {
    return authorService.getAuthors().stream()
        .distinct()
        .count();
  }

  @GetMapping("/api/authors/{id}")
  @Override
  public Author getAuthorById(@PathVariable(name = "id") String authorId) {
    Author author = authorService.getAuthorById(authorId);
    if (author == null) {
      throw new NotFoundException("Author: " + authorId + " is not found");
    }
    return author;
  }

  @GetMapping("/api/authors/{id}/papers")
  @Override
  public List<Paper> getPapersByAuthor(
      @PathVariable(name = "id") String authorId,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = authorService.getPapersByAuthorId(authorId).stream();
    StreamModifier<Paper> modifier = new StreamModifier<>(
        modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @GetMapping("/api/authors/{id}/papers/count")
  @Override
  public long getPapersCountByAuthor(@PathVariable(name = "id") String authorId) {
    return authorService.getPapersByAuthorId(authorId).size();
  }

  @GetMapping("/api/authors/{id}/years")
  @Override
  public List<Integer> getYearsOfPapersByAuthor(
      @PathVariable(name = "id") String authorId,
      @RequestParam(name = "orderBy", required = false, defaultValue = "year") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Integer> years = authorService.getPapersByAuthorId(authorId).stream()
        .map(Paper::getYear)
        .distinct();
    StreamModifier<Integer> modifier = new StreamModifier<>(
        modelComparators.getYearComparatorMap());
    years = modifier.modify(years, orderBy, asc, limit);
    return years.collect(Collectors.toList());
  }

  @GetMapping("/api/authors/{id}/years/count")
  @Override
  public long getYearsOfPapersCountByAuthor(@PathVariable(name = "id") String authorId) {
    return authorService.getPapersByAuthorId(authorId).stream()
        .map(Paper::getYear)
        .distinct()
        .count();
  }

  @GetMapping("/api/authors/{id}/keyPhrases")
  @Override
  public List<String> getKeyPhrasesOfPapersByAuthor(
      @PathVariable(name = "id") String authorId,
      @RequestParam(name = "orderBy", required = false, defaultValue = "keyPhrase") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<String> keyPhrases = authorService.getPapersByAuthorId(authorId).stream()
        .flatMap(paper -> paper.getKeyPhrases().stream())
        .distinct();
    StreamModifier<String> modifier = new StreamModifier<>(
        modelComparators.getKeyPhraseComparatorMap());
    keyPhrases = modifier.modify(keyPhrases, orderBy, asc, limit);
    return keyPhrases.collect(Collectors.toList());
  }

  @GetMapping("/api/authors/{id}/keyPhrases/count")
  @Override
  public long getKeyPhrasesCountOfPapersByAuthor(@PathVariable(name = "id") String authorId) {
    return authorService.getPapersByAuthorId(authorId).stream()
        .flatMap(paper -> paper.getKeyPhrases().stream())
        .distinct()
        .count();
  }

  @GetMapping("/api/authors/{id}/venues")
  @Override
  public List<String> getVenuesOfPapersByAuthor(
      String authorId,
      @RequestParam(name = "orderBy", required = false, defaultValue = "venue") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<String> venues = authorService.getPapersByAuthorId(authorId).stream()
        .map(Paper::getVenue)
        .distinct();
    StreamModifier<String> modifier = new StreamModifier<>(
        modelComparators.getVenueComparatorMap());
    venues = modifier.modify(venues, orderBy, asc, limit);
    return venues.collect(Collectors.toList());
  }

  @GetMapping("/api/authors/{id}/venues/count")
  @Override
  public long getVenuesCountOfPapersByAuthor(@PathVariable(name = "id") String authorId) {
    return authorService.getPapersByAuthorId(authorId).stream()
        .map(Paper::getVenue)
        .distinct()
        .count();
  }

  @GetMapping("/api/authors/{id}/inCitations")
  @Override
  public List<Paper> getCitedPapersWrittenByAuthor(
      @PathVariable(name = "id") String authorId,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = authorService.getPapersByAuthorId(authorId).stream()
        .flatMap(paper -> paperService.getInCitationsByPaper(paper).stream())
        .distinct();
    StreamModifier<Paper> modifier = new StreamModifier<>(
        modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @GetMapping("/api/authors/{id}/inCitations/count")
  @Override
  public long getCitedPapersCountWrittenByAuthor(@PathVariable(name = "id") String authorId) {
    return authorService.getPapersByAuthorId(authorId).stream()
        .flatMap(paper -> paperService.getInCitationsByPaper(paper).stream())
        .distinct()
        .count();
  }

  @GetMapping("/api/authors/{id}/outCitations")
  @Override
  public List<Paper> getPapersCitedByAuthor(
      @PathVariable(name = "id") String authorId,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = authorService.getPapersByAuthorId(authorId).stream()
        .flatMap(paper -> paperService.getOutCitationsByPaper(paper).stream())
        .distinct();
    StreamModifier<Paper> modifier = new StreamModifier<>(
        modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @GetMapping("/api/authors/{id}/outCitations/count")
  @Override
  public long getPapersCountCitedByAuthor(@PathVariable(name = "id") String authorId) {
    return authorService.getPapersByAuthorId(authorId).stream()
        .flatMap(paper -> paperService.getOutCitationsByPaper(paper).stream())
        .distinct()
        .count();
  }
}
