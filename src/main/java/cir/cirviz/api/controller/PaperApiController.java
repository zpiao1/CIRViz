package cir.cirviz.api.controller;

import cir.cirviz.api.service.PaperService;
import cir.cirviz.api.util.NotFoundException;
import cir.cirviz.data.model.Author;
import cir.cirviz.data.model.Paper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaperApiController implements PaperApi {

  private final PaperService paperService;

  @Autowired
  public PaperApiController(PaperService paperService) {
    this.paperService = paperService;
  }

  @GetMapping("/api/papers")
  @Override
  public List<Paper> getPapers(@RequestParam(name = "title", required = false) String title) {
    List<Paper> papers = paperService.getPapers();
    if (title == null || title.isEmpty()) {
      return papers;
    } else {
      return papers.stream()
          .filter(paper -> paper.getTitle().toLowerCase().contains(title.toLowerCase()))
          .distinct()
          .collect(Collectors.toList());
    }
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
  public List<Author> getAuthorsOfPaper(@PathVariable(name = "id") String paperId) {
    return getPaperById(paperId).getAuthors();
  }

  @GetMapping("/api/papers/{id}/year")
  @Override
  public int getYearOfPaper(@PathVariable(name = "id") String paperId) {
    return getPaperById(paperId).getYear();
  }

  @GetMapping("/api/papers/{id}/keyPhrases")
  @Override
  public List<String> getKeyPhrasesOfPaper(@PathVariable(name = "id") String paperId) {
    return getPaperById(paperId).getKeyPhrases();
  }

  @GetMapping("/api/papers/{id}/venue")
  @Override
  public String getVenueOfPaper(@PathVariable(name = "id") String paperId) {
    return getPaperById(paperId).getVenue();
  }

  @GetMapping("/api/papers/{id}/outCitations")
  @Override
  public List<Paper> getOutCitationsOfPaper(@PathVariable(name = "id") String paperId) {
    return paperService.getOutCitationsByPaperId(paperId);
  }

  @GetMapping("/api/papers/{id}/inCitations")
  @Override
  public List<Paper> getInCitationsOfPaper(@PathVariable(name = "id") String paperId) {
    return paperService.getInCitationsByPaperId(paperId);
  }
}
