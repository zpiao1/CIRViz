package cir.cirviz.data;

import cir.cirviz.data.entity.Author;
import cir.cirviz.data.entity.Paper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class PaperRepositoryImpl implements PaperRepository {

  private static Logger logger = LoggerFactory.getLogger(PaperRepositoryImpl.class);

  private ConcurrentMap<String, Paper> papers = new ConcurrentHashMap<>();

  private ConcurrentMap<String, Author> authors = new ConcurrentHashMap<>();

  private ConcurrentMap<String, List<Author>> paperToAuthors = new ConcurrentHashMap<>();

  private ConcurrentMap<String, List<Paper>> authorToPapers = new ConcurrentHashMap<>();

  private ConcurrentMap<String, List<Paper>> inCitations = new ConcurrentHashMap<>();

  private ConcurrentMap<String, List<Paper>> outCitations = new ConcurrentHashMap<>();

  private ConcurrentMap<String, List<Paper>> keyPhraseToPapers = new ConcurrentHashMap<>();

  private ConcurrentMap<String, List<Paper>> venueToPapers = new ConcurrentHashMap<>();

  private ConcurrentMap<Integer, List<Paper>> yearToPapers = new ConcurrentHashMap<>();

  private static <K, V> void preprocessMapToList(ConcurrentMap<K, List<V>> map, K key) {
    map.putIfAbsent(key, Collections.synchronizedList(new ArrayList<>()));
  }

  public void addPaper(Paper paper) {
    String id = paper.getId();
    papers.put(id, paper);
  }

  public void save() {
    buildRelations();
  }

  private void addAuthor(Author author) {
    String id = author.getId();
    authors.put(id, author);
  }

  private void buildRelations() {
    papers.values()
        .parallelStream()
        .forEach(this::buildRelationForPaper);
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

  public Map<String, Paper> getPapers() {
    return papers;
  }

  public Map<String, Author> getAuthors() {
    return authors;
  }

  public Map<String, List<Author>> getPaperToAuthors() {
    return paperToAuthors;
  }

  public Map<String, List<Paper>> getAuthorToPapers() {
    return authorToPapers;
  }

  public Map<String, List<Paper>> getInCitations() {
    return inCitations;
  }

  public Map<String, List<Paper>> getOutCitations() {
    return outCitations;
  }

  public Map<String, List<Paper>> getKeyPhraseToPapers() {
    return keyPhraseToPapers;
  }

  public Map<String, List<Paper>> getVenueToPapers() {
    return venueToPapers;
  }

  public Map<Integer, List<Paper>> getYearToPapers() {
    return yearToPapers;
  }

  private void buildRelationForPaper(Paper paper) {
    paper.getAuthors()
        .parallelStream()
        .forEach(author -> {
          addAuthor(author);
          buildPaperAuthorRelation(paper, author);
        });

    List<String> inCitations = paper.getInCitations();
    Iterator<String> inCitationsIterator = inCitations.iterator();
    while (inCitationsIterator.hasNext()) {
      String inCitationId = inCitationsIterator.next();
      Paper citingPaper = papers.get(inCitationId);
      if (citingPaper != null) {
        buildCitationRelation(citingPaper, paper);
      } else {
        inCitationsIterator.remove(); // Remove IDs of non-existing papers
      }
    }

    List<String> outCitations = paper.getOutCitations();
    Iterator<String> outCitationsIterator = outCitations.iterator();
    while (outCitationsIterator.hasNext()) {
      String outCitationId = outCitationsIterator.next();
      Paper citedPaper = papers.get(outCitationId);
      if (citedPaper != null) {
        buildCitationRelation(paper, citedPaper);
      } else {
        outCitationsIterator.remove(); // Remove IDs of non-existing papers
      }
    }

    paper.getKeyPhrases()
        .parallelStream()
        .forEach(keyPhrase -> buildPaperKeyPhraseRelation(paper, keyPhrase));

    buildPaperVenueRelation(paper, paper.getVenue());
    buildYearVenueRelation(paper, paper.getYear());

    logger.info("Finished paper: " + paper.getId());
  }
}
