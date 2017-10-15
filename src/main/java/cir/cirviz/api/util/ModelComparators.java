package cir.cirviz.api.util;

import cir.cirviz.api.service.AuthorService;
import cir.cirviz.api.service.KeyPhraseService;
import cir.cirviz.api.service.VenueService;
import cir.cirviz.api.service.YearService;
import cir.cirviz.data.entity.Author;
import cir.cirviz.data.entity.Paper;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelComparators {

  private final AuthorService authorService;
  private final YearService yearService;
  private final KeyPhraseService keyPhraseService;
  private final VenueService venueService;

  private final Map<String, Comparator<Paper>> paperComparatorMap = new HashMap<>();
  private final Map<String, Comparator<Author>> authorComparatorMap = new HashMap<>();
  private final Map<String, Comparator<Integer>> yearComparatorMap = new HashMap<>();
  private final Map<String, Comparator<String>> venueComparatorMap = new HashMap<>();
  private final Map<String, Comparator<String>> keyPhraseComparatorMap = new HashMap<>();

  @Autowired
  public ModelComparators(AuthorService authorService,
      YearService yearService,
      KeyPhraseService keyPhraseService,
      VenueService venueService) {
    this.authorService = authorService;
    this.yearService = yearService;
    this.keyPhraseService = keyPhraseService;
    this.venueService = venueService;

    initPaperComparatorMap();
    initAuthorComparatorMap();
    initYearComparatorMap();
    initVenueComparatorMap();
    initKeyPhraseComparatorMap();
  }

  private void initPaperComparatorMap() {
    paperComparatorMap.put("title", Comparator.comparing(Paper::getTitle));
    paperComparatorMap.put("authors", Comparator.comparingInt(p -> p.getAuthors().size()));
    paperComparatorMap.put("inCitations", Comparator.comparingInt(p -> p.getInCitations().size()));
    paperComparatorMap
        .put("outCitations", Comparator.comparingInt(p -> p.getOutCitations().size()));
    paperComparatorMap.put("keyPhrases", Comparator.comparingInt(p -> p.getKeyPhrases().size()));
    paperComparatorMap.put("pdfUrls", Comparator.comparingInt(p -> p.getPdfUrls().size()));
    paperComparatorMap.put("s2Url", Comparator.comparing(Paper::getS2Url));
    paperComparatorMap.put("venue", Comparator.comparing(Paper::getVenue));
    paperComparatorMap.put("year", Comparator.comparingInt(Paper::getYear));
    paperComparatorMap.put("abstract", Comparator.comparing(Paper::getPaperAbstract));
  }

  private void initAuthorComparatorMap() {
    authorComparatorMap.put("name", Comparator.comparing(Author::getName));
    authorComparatorMap
        .put("papers", Comparator.comparingInt(a -> authorService.getPapersByAuthor(a).size()));
  }

  private void initYearComparatorMap() {
    yearComparatorMap.put("year", Comparator.comparingInt(y -> y));
    yearComparatorMap
        .put("papers", Comparator.comparingInt(y -> yearService.getPapersByYear(y).size()));
  }

  private void initVenueComparatorMap() {
    venueComparatorMap.put("venue", Comparator.comparing(v -> v));
    venueComparatorMap
        .put("papers", Comparator.comparingInt(v -> venueService.getPapersByVenue(v).size()));
  }

  private void initKeyPhraseComparatorMap() {
    keyPhraseComparatorMap.put("keyPhrase", Comparator.comparing(k -> k));
    keyPhraseComparatorMap.put("papers",
        Comparator.comparingInt(k -> keyPhraseService.getPapersByKeyPhrase(k).size()));
  }

  public Map<String, Comparator<Paper>> getPaperComparatorMap() {
    return paperComparatorMap;
  }

  public Map<String, Comparator<Author>> getAuthorComparatorMap() {
    return authorComparatorMap;
  }

  public Map<String, Comparator<Integer>> getYearComparatorMap() {
    return yearComparatorMap;
  }

  public Map<String, Comparator<String>> getVenueComparatorMap() {
    return venueComparatorMap;
  }

  public Map<String, Comparator<String>> getKeyPhraseComparatorMap() {
    return keyPhraseComparatorMap;
  }
}
