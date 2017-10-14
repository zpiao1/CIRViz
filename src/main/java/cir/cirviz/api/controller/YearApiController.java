package cir.cirviz.api.controller;

import cir.cirviz.api.service.PaperService;
import cir.cirviz.api.service.YearService;
import cir.cirviz.api.util.NotFoundException;
import cir.cirviz.data.model.Author;
import cir.cirviz.data.model.Paper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YearApiController implements YearApi {

  private final YearService yearService;
  private final PaperService paperService;

  @Autowired
  public YearApiController(YearService yearService,
      PaperService paperService) {
    this.yearService = yearService;
    this.paperService = paperService;
  }

  @GetMapping("/api/years")
  @Override
  public List<Integer> getYears() {
    return yearService.getYears();
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
  public List<Paper> getPapersByYear(@PathVariable(name = "year") int year) {
    return yearService.getPapersByYear(year);
  }

  @GetMapping("/api/years/{year}/authors")
  @Override
  public List<Author> getAuthorsByYear(@PathVariable(name = "year") int year) {
    return yearService.getPapersByYear(year).stream()
        .flatMap(paper -> paper.getAuthors().stream())
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/years/{year}/venues")
  @Override
  public List<String> getVenuesByYear(@PathVariable(name = "year") int year) {
    return yearService.getPapersByYear(year).stream()
        .map(Paper::getVenue)
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/years/{year}/keyPhrases")
  @Override
  public List<String> getKeyPhrasesByYear(@PathVariable(name = "year") int year) {
    return yearService.getPapersByYear(year).stream()
        .flatMap(paper -> paper.getKeyPhrases().stream())
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/years/{year}/outCitations")
  @Override
  public List<Paper> getCitationsByYear(@PathVariable(name = "year") int year) {
    return yearService.getPapersByYear(year).stream()
        .flatMap(paper -> paperService.getOutCitationsByPaper(paper).stream())
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/years/{year}/inCitations")
  @Override
  public List<Paper> getPapersCitingPapersAtYear(@PathVariable(name = "year") int year) {
    return yearService.getPapersByYear(year).stream()
        .flatMap(paper -> paperService.getInCitationsByPaper(paper).stream())
        .distinct()
        .collect(Collectors.toList());
  }
}
