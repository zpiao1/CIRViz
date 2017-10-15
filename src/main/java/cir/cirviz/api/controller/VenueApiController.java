package cir.cirviz.api.controller;

import cir.cirviz.api.service.PaperService;
import cir.cirviz.api.service.VenueService;
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
public class VenueApiController implements VenueApi {

  private final VenueService venueService;
  private final PaperService paperService;
  private final ModelComparators modelComparators;

  @Autowired
  public VenueApiController(VenueService venueService,
      PaperService paperService,
      ModelComparators modelComparators) {
    this.venueService = venueService;
    this.paperService = paperService;
    this.modelComparators = modelComparators;
  }

  @GetMapping("/api/venues")
  @Override
  public List<String> getVenues(
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<String> venues = venueService.getVenues().stream();
    StreamModifier<String> modifier = new StreamModifier<>(
        modelComparators.getVenueComparatorMap());
    venues = modifier.modify(venues, orderBy, asc, limit);
    return venues.collect(Collectors.toList());
  }

  @GetMapping("/api/venues/count")
  @Override
  public long getVenuesCount() {
    return venueService.getVenues().size();
  }

  @GetMapping("/api/venues/{venue}")
  @Override
  public String getVenue(@PathVariable(name = "venue") String venue) {
    List<String> venues = venueService.getVenues();
    if (venues.contains(venue)) {
      return venue;
    } else {
      throw new NotFoundException("Venue: " + venue + " is not found.");
    }
  }

  @GetMapping("/api/venues/{venue}/papers")
  @Override
  public List<Paper> getPapersByVenue(
      @PathVariable(name = "venue") String venue,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = venueService.getPapersByVenue(venue).stream();
    StreamModifier<Paper> modifier = new StreamModifier<>(modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @GetMapping("/api/venues/{venue}/papers/count")
  @Override
  public long getPapersCountByVenue(@PathVariable(name = "venue") String venue) {
    return venueService.getPapersByVenue(venue).size();
  }

  @GetMapping("/api/venues/{venue}/authors")
  @Override
  public List<Author> getAuthorsByVenue(
      @PathVariable(name = "venue") String venue,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Author> authors = venueService.getPapersByVenue(venue).stream()
        .flatMap(paper -> paper.getAuthors().stream())
        .distinct();
    StreamModifier<Author> modifier = new StreamModifier<>(
        modelComparators.getAuthorComparatorMap());
    authors = modifier.modify(authors, orderBy, asc, limit);
    return authors.collect(Collectors.toList());
  }

  @GetMapping("/api/venues/{venue}/authors/count")
  @Override
  public long getAuthorsCountByVenue(@PathVariable(name = "venue") String venue) {
    return venueService.getPapersByVenue(venue).stream()
        .flatMap(paper -> paper.getAuthors().stream())
        .distinct()
        .count();
  }

  @GetMapping("/api/venues/{venue}/years")
  @Override
  public List<Integer> getYearsByVenue(
      @PathVariable(name = "venue") String venue,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Integer> years = venueService.getPapersByVenue(venue).stream()
        .map(Paper::getYear)
        .distinct();
    StreamModifier<Integer> modifier = new StreamModifier<>(
        modelComparators.getYearComparatorMap());
    years = modifier.modify(years, orderBy, asc, limit);
    return years.collect(Collectors.toList());
  }

  @GetMapping("/api/venues/{venue}/years/count")
  @Override
  public long getYearsCountByVenue(@PathVariable(name = "venue") String venue) {
    return venueService.getPapersByVenue(venue).stream()
        .map(Paper::getYear)
        .distinct()
        .count();
  }

  @GetMapping("/api/venues/{venue}/keyPhrases")
  @Override
  public List<String> getKeyPhrasesByVenue(
      @PathVariable(name = "venue") String venue,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<String> keyPhrases = venueService.getPapersByVenue(venue).stream()
        .flatMap(paper -> paper.getKeyPhrases().stream())
        .distinct();
    StreamModifier<String> modifier = new StreamModifier<>(
        modelComparators.getKeyPhraseComparatorMap());
    keyPhrases = modifier.modify(keyPhrases, orderBy, asc, limit);
    return keyPhrases.collect(Collectors.toList());
  }

  @GetMapping("/api/venue/{venue}/keyPhrases/count")
  @Override
  public long getKeyPhrasesCountByVenue(@PathVariable(name = "venue") String venue) {
    return venueService.getPapersByVenue(venue).stream()
        .flatMap(paper -> paper.getKeyPhrases().stream())
        .distinct()
        .count();
  }

  @GetMapping("/api/venues/{venue}/outCitations")
  @Override
  public List<Paper> getCitationsByVenue(
      @PathVariable(name = "venue") String venue,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = venueService.getPapersByVenue(venue).stream()
        .flatMap(paper -> paperService.getOutCitationsByPaper(paper).stream())
        .distinct();
    StreamModifier<Paper> modifier = new StreamModifier<>(modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @GetMapping("/api/venues/{venue}/outCitations/count")
  @Override
  public long getCitationsCountByVenue(@PathVariable(name = "venue") String venue) {
    return venueService.getPapersByVenue(venue).stream()
        .flatMap(paper -> paperService.getOutCitationsByPaper(paper).stream())
        .distinct()
        .count();
  }

  @GetMapping("/api/venues/{venue}/inCitations")
  @Override
  public List<Paper> getPapersCitingPapersAtVenue(
      @PathVariable(name = "venue") String venue,
      @RequestParam(name = "orderBy", required = false, defaultValue = "title") String orderBy,
      @RequestParam(name = "asc", required = false, defaultValue = "true") boolean asc,
      @RequestParam(name = "limit", required = false, defaultValue = "10") long limit
  ) {
    Stream<Paper> papers = venueService.getPapersByVenue(venue).stream()
        .flatMap(paper -> paperService.getInCitationsByPaper(paper).stream())
        .distinct();
    StreamModifier<Paper> modifier = new StreamModifier<>(modelComparators.getPaperComparatorMap());
    papers = modifier.modify(papers, orderBy, asc, limit);
    return papers.collect(Collectors.toList());
  }

  @GetMapping("/api/venues/{venue}/inCitations/count")
  @Override
  public long getPapersCountCitingPapersAtVenue(@PathVariable(name = "venue") String venue) {
    return venueService.getPapersByVenue(venue).stream()
        .flatMap(paper -> paperService.getInCitationsByPaper(paper).stream())
        .distinct()
        .count();
  }
}
