package cir.cirviz.api.controller;

import cir.cirviz.api.service.KeyPhraseService;
import cir.cirviz.api.service.PaperService;
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
public class KeyPhrasesApiController implements KeyPhrasesApi {

  private final KeyPhraseService keyPhraseService;
  private final PaperService paperService;

  @Autowired
  public KeyPhrasesApiController(KeyPhraseService keyPhraseService,
      PaperService paperService) {
    this.keyPhraseService = keyPhraseService;
    this.paperService = paperService;
  }

  @GetMapping("/api/keyPhrases")
  @Override
  public List<String> getKeyPhrases() {
    return keyPhraseService.getKeyPhrases();
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
  public List<Paper> getPapersByKeyPhrase(@PathVariable(name = "keyPhrase") String keyPhrase) {
    return keyPhraseService.getPapersByKeyPhrase(keyPhrase);
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/authors")
  @Override
  public List<Author> getAuthorsByKeyPhrase(@PathVariable(name = "keyPhrase") String keyPhrase) {
    return keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream()
        .flatMap(paper -> paper.getAuthors().stream())
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/years")
  @Override
  public List<Integer> getYearsByKeyPhrase(@PathVariable(name = "keyPhrase") String keyPhrase) {
    return keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream()
        .map(Paper::getYear)
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/outCitations")
  @Override
  public List<Paper> getCitationsByKeyPhrase(@PathVariable(name = "keyPhrase") String keyPhrase) {
    return keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream()
        .flatMap(paper -> paperService.getOutCitationsByPaper(paper).stream())
        .distinct()
        .collect(Collectors.toList());
  }

  @GetMapping("/api/keyPhrases/{keyPhrase}/inCitations")
  @Override
  public List<Paper> getPapersCitingPapersWithKeyPhrase(
      @PathVariable(name = "keyPhrase") String keyPhrase) {
    return keyPhraseService.getPapersByKeyPhrase(keyPhrase).stream()
        .flatMap(paper -> paperService.getInCitationsByPaper(paper).stream())
        .distinct()
        .collect(Collectors.toList());
  }
}
