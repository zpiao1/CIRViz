package cir.cirviz.api.controller;

import cir.cirviz.api.service.PaperService;
import cir.cirviz.api.service.VenueService;
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
public class VenueApiController implements VenueApi {

  private final VenueService venueService;
  private final PaperService paperService;

  @Autowired
  public VenueApiController(VenueService venueService,
      PaperService paperService) {
    this.venueService = venueService;
    this.paperService = paperService;
  }

  @GetMapping("/api/venues")
  @Override
  public List<String> getVenues() {
    return venueService.getVenues();
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
  public List<Paper> getPapersByVenue(@PathVariable(name = "venue") String venue) {
    return venueService.getPapersByVenue(venue);
  }

  @GetMapping("/api/venues/{venue}/authors")
  @Override
  public List<Author> getAuthorsByVenue(@PathVariable(name = "venue") String venue) {
    return venueService.getPapersByVenue(venue).stream()
        .flatMap(paper -> paper.getAuthors().stream())
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/venues/{venue}/years")
  @Override
  public List<Integer> getYearsByVenue(@PathVariable(name = "venue") String venue) {
    return venueService.getPapersByVenue(venue).stream()
        .map(Paper::getYear)
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/venues/{venue}/keyPhrases")
  @Override
  public List<String> getKeyPhrasesByVenue(@PathVariable(name = "venue") String venue) {
    return venueService.getPapersByVenue(venue).stream()
        .flatMap(paper -> paper.getKeyPhrases().stream())
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/venues/{venue}/outCitations")
  @Override
  public List<Paper> getCitationsByVenue(@PathVariable(name = "venue") String venue) {
    return venueService.getPapersByVenue(venue).stream()
        .flatMap(paper -> paperService.getOutCitationsByPaper(paper).stream())
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/venues/{venue}/inCitations")
  @Override
  public List<Paper> getPapersCitingPapersAtVenue(@PathVariable(name = "venue") String venue) {
    return venueService.getPapersByVenue(venue).stream()
        .flatMap(paper -> paperService.getInCitationsByPaper(paper).stream())
        .distinct()
        .collect(Collectors.toList());
  }
}
