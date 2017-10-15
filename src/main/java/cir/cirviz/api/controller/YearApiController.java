package cir.cirviz.api.controller;

import cir.cirviz.api.service.PaperService;
import cir.cirviz.api.service.YearService;
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
public class YearApiController implements YearApi {

  private final YearService yearService;
  private final PaperService paperService;
  private final ModelComparators modelComparators;

  @Autowired
  public YearApiController(YearService yearService,
      PaperService paperService,
      ModelComparators modelComparators) {
    this.yearService = yearService;
    this.paperService = paperService;
    this.modelComparators = modelComparators;
  }

  @GetMapping("/api/years")
  @Override
  public List<Integer> getYears(
      @RequestParam(name = "orderBy", required = false, defaultValue = "year") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Integer> years = yearService.getYears().stream();
    StreamModifier<Integer> modifier = new StreamModifier<>(
        modelComparators.getYearComparatorMap());
    years = modifier.modify(years, orderBy, asc, limit);
    return years.collect(Collectors.toList());
  }

  @GetMapping("/api/years/count")
  @Override
  public long getYearsCount() {
    return yearService.getYears().size();
  }

  @GetMapping("/api/years/{year}")
  @Override
  public int getYear(@PathVariable(name = "year") int year) {
    List<Integer> years = yearService.getYears();
    if (years.contains(year)) {
      return year;
    } else {
      throw new NotFoundException("Year: " + year + " not found.");
    }
  }

  @GetMapping("/api/years/{year}/papers")
  @Override
  public List<Paper> getPapersByYear(
      @PathVariable(name = "year") int year,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = yearService.getPapersByYear(year).stream();
    StreamModifier<Paper> modifier = new StreamModifier<>(modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @GetMapping("/api/years/{year}/papers/count")
  @Override
  public long getPapersCountByYear(@PathVariable(name = "year") int year) {
    return yearService.getPapersByYear(year).size();
  }

  @GetMapping("/api/years/{year}/authors")
  @Override
  public List<Author> getAuthorsByYear(
      @PathVariable(name = "year") int year,
      @RequestParam(name = "orderBy", required = false, defaultValue = "name") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Author> authors = yearService.getPapersByYear(year).stream()
        .flatMap(paper -> paper.getAuthors().stream())
        .distinct();
    StreamModifier<Author> modifier = new StreamModifier<>(
        modelComparators.getAuthorComparatorMap());
    authors = modifier.modify(authors, orderBy, asc, limit);
    return authors.collect(Collectors.toList());
  }

  @GetMapping("/api/years/{year}/authors/count")
  @Override
  public long getAuthorsCountByYear(@PathVariable(name = "year") int year) {
    return yearService.getPapersByYear(year).stream()
        .flatMap(paper -> paper.getAuthors().stream())
        .distinct()
        .count();
  }

  @GetMapping("/api/years/{year}/venues")
  @Override
  public List<String> getVenuesByYear(
      @PathVariable(name = "year") int year,
      @RequestParam(name = "orderBy", required = false, defaultValue = "venue") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<String> venues = yearService.getPapersByYear(year).stream()
        .map(Paper::getVenue)
        .distinct();
    StreamModifier<String> modifier = new StreamModifier<>(
        modelComparators.getVenueComparatorMap());
    venues = modifier.modify(venues, orderBy, asc, limit);
    return venues.collect(Collectors.toList());
  }

  @GetMapping("/api/years/{year}/venues/count")
  @Override
  public long getVenuesCountByYear(@PathVariable(name = "year") int year) {
    return yearService.getPapersByYear(year).stream()
        .map(Paper::getVenue)
        .distinct()
        .count();
  }

  @GetMapping("/api/years/{year}/keyPhrases")
  @Override
  public List<String> getKeyPhrasesByYear(
      @PathVariable(name = "year") int year,
      @RequestParam(name = "orderBy", required = false, defaultValue = "keyPhrase") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<String> keyPhrases = yearService.getPapersByYear(year).stream()
        .flatMap(paper -> paper.getKeyPhrases().stream())
        .distinct();
    StreamModifier<String> modifier = new StreamModifier<>(
        modelComparators.getKeyPhraseComparatorMap());
    keyPhrases = modifier.modify(keyPhrases, orderBy, asc, limit);
    return keyPhrases.collect(Collectors.toList());
  }

  @GetMapping("/api/years/{year}/keyPhrases/count")
  @Override
  public long getKeyPhrasesCountByYear(@PathVariable(name = "year") int year) {
    return yearService.getPapersByYear(year).stream()
        .flatMap(paper -> paper.getKeyPhrases().stream())
        .distinct()
        .count();
  }

  @GetMapping("/api/years/{year}/outCitations")
  @Override
  public List<Paper> getCitationsByYear(
      @PathVariable(name = "year") int year,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = yearService.getPapersByYear(year).stream()
        .flatMap(paper -> paperService.getOutCitationsByPaper(paper).stream())
        .distinct();
    StreamModifier<Paper> modifier = new StreamModifier<>(
        modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @GetMapping("/api/years/{year}/outCitations/count")
  @Override
  public long getCitationsCountByYear(@PathVariable(name = "year") int year) {
    return yearService.getPapersByYear(year).stream()
        .flatMap(paper -> paperService.getOutCitationsByPaper(paper).stream())
        .distinct()
        .count();
  }

  @GetMapping("/api/years/{year}/inCitations")
  @Override
  public List<Paper> getPapersCitingPapersAtYear(
      @PathVariable(name = "year") int year,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = yearService.getPapersByYear(year).stream()
        .flatMap(paper -> paperService.getInCitationsByPaper(paper).stream())
        .distinct();
    StreamModifier<Paper> modifier = new StreamModifier<>(modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @GetMapping("/api/years/{year}/inCitations/count")
  @Override
  public long getPapersCountCitingPapersAtYear(int year) {
    return yearService.getPapersByYear(year).stream()
        .flatMap(paper -> paperService.getInCitationsByPaper(paper).stream())
        .distinct()
        .count();
  }
}
