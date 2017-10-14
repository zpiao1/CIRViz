package cir.cirviz.data;

import cir.cirviz.data.model.Author;
import cir.cirviz.data.model.Paper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PaperRepository {

  private static Logger logger = LoggerFactory.getLogger(PaperRepository.class);

  private ConcurrentMap<String, Paper> papers = new ConcurrentHashMap<>();

  private ConcurrentMap<String, Author> authors = new ConcurrentHashMap<>();

  private ConcurrentMap<String, List<Author>> paperToAuthors = new ConcurrentHashMap<>();

  private ConcurrentMap<String, List<Paper>> authorToPapers = new ConcurrentHashMap<>();

  private ConcurrentMap<String, List<Paper>> inCitations = new ConcurrentHashMap<>();

  private ConcurrentMap<String, List<Paper>> outCitations = new ConcurrentHashMap<>();

  private ConcurrentMap<String, List<Paper>> keyPhraseToPapers = new ConcurrentHashMap<>();

  private ConcurrentMap<String, List<Paper>> venueToPapers = new ConcurrentHashMap<>();

  private ConcurrentMap<Integer, List<Paper>> yearToPapers = new ConcurrentHashMap<>();

  public void addPaper(Paper paper) {
    String id = paper.getId();
    papers.put(id, paper);
  }

  private static <K, V> void preprocessMapToList(ConcurrentMap<K, List<V>> map, K key) {
    map.putIfAbsent(key, Collections.synchronizedList(new ArrayList<>()));
  }

  private void addAuthor(Author author) {
    String id = author.getId();
    authors.put(id, author);
  }

  public void buildRelations() {
    papers.values()
        .parallelStream()
        .forEach(paper -> {
          paper.getAuthors()
              .parallelStream()
              .forEach(author -> {
                addAuthor(author);
                buildPaperAuthorRelation(paper, author);
              });

          paper.getInCitations()
              .parallelStream()
              .forEach(inCitationId -> {
                Paper citingPaper = papers.get(inCitationId);
                if (citingPaper != null) {
                  buildCitationRelation(citingPaper, paper);  // Ignoring non-existing papers
                }
              });

          paper.getOutCitations()
              .parallelStream()
              .forEach(outCitationId -> {
                Paper citedPaper = papers.get(outCitationId);
                if (citedPaper != null) {
                  buildCitationRelation(paper, citedPaper); // Ignoring non-existing papers
                }
              });

          paper.getKeyPhrases()
              .parallelStream()
              .forEach(keyPhrase -> buildPaperKeyPhraseRelation(paper, keyPhrase));

          buildPaperVenueRelation(paper, paper.getVenue());
          buildYearVenueRelation(paper, paper.getYear());

          logger.info("Finished paper: " + paper.getId());
        });
  }

  private void buildPaperAuthorRelation(Paper paper, Author author) {
    String paperId = paper.getId();
    String authorId = author.getId();

    preprocessMapToList(paperToAuthors, paperId);
    paperToAuthors.get(paperId).add(author);

    preprocessMapToList(authorToPapers, authorId);
    authorToPapers.get(authorId).add(paper);
  }

  private void buildCitationRelation(Paper citingPaper, Paper citedPaper) {
    // InCitations: citedPaperId -> List of papers citing this one
    // OutCitations: citingPaperId -> List of papers this one is citing
    String citingPaperId = citingPaper.getId();
    String citedPaperId = citedPaper.getId();

    preprocessMapToList(inCitations, citedPaperId);
    inCitations.get(citedPaperId).add(citingPaper);

    preprocessMapToList(outCitations, citingPaperId);
    outCitations.get(citingPaperId).add(citedPaper);
  }

  private void buildPaperKeyPhraseRelation(Paper paper, String keyPhrase) {
    preprocessMapToList(keyPhraseToPapers, keyPhrase);
    keyPhraseToPapers.get(keyPhrase).add(paper);
  }

  private void buildPaperVenueRelation(Paper paper, String venue) {
    preprocessMapToList(venueToPapers, venue);
    venueToPapers.get(venue).add(paper);
  }

  private void buildYearVenueRelation(Paper paper, int year) {
    preprocessMapToList(yearToPapers, year);
    yearToPapers.get(year).add(paper);
  }
}
