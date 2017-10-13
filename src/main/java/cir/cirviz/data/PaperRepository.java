package cir.cirviz.data;

import cir.cirviz.data.model.Author;
import cir.cirviz.data.model.Paper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PaperRepository {

  private Map<String, Paper> papers = new HashMap<>();

  private Map<String, Author> authors = new HashMap<>();

  private Map<String, List<Author>> paperToAuthors = new HashMap<>();

  private Map<String, List<Paper>> authorToPapers = new HashMap<>();

  private Map<String, List<Paper>> inCitations = new HashMap<>();

  private Map<String, List<Paper>> outCitations = new HashMap<>();

  private Map<String, List<Paper>> keyPhraseToPapers = new HashMap<>();

  private Map<String, List<Paper>> venueToPapers = new HashMap<>();

  private Map<Integer, List<Paper>> yearToPapers = new HashMap<>();

  public void addPaper(Paper paper) {
    String id = paper.getId();
    papers.put(id, paper);
  }

  public void buildRelations() {
    papers.values().forEach(paper -> {
      paper.getAuthors().forEach(author -> {
        addAuthor(author);
        buildPaperAuthorRelation(paper, author);
      });

      paper.getInCitations().forEach(inCitationId -> {
        Paper citingPaper = papers.get(inCitationId);
        if (citingPaper != null) {
          buildCitationRelation(citingPaper, paper);
        }
      });

      paper.getOutCitations().forEach(outCitationId -> {
        Paper citedPaper = papers.get(outCitationId);
        if (outCitationId != null) {
          buildCitationRelation(paper, citedPaper);
        }
      });

      paper.getKeyPhrases().forEach(keyPhrase -> buildPaperKeyPhraseRelation(paper, keyPhrase));

      buildPaperVenueRelation(paper, paper.getVenue());
      buildYearVenueRelation(paper, paper.getYear());
    });
  }

  private void addAuthor(Author author) {
    String id = author.getId();
    authors.put(id, author);
  }

  private void buildPaperAuthorRelation(Paper paper, Author author) {
    String paperId = paper.getId();
    String authorId = author.getId();

    if (!paperToAuthors.containsKey(paperId)) {
      paperToAuthors.put(paperId, new ArrayList<>());
    }
    paperToAuthors.get(paperId).add(author);

    if (!authorToPapers.containsKey(authorId)) {
      authorToPapers.put(authorId, new ArrayList<>());
    }
    authorToPapers.get(authorId).add(paper);
  }

  private void buildCitationRelation(Paper citingPaper, Paper citedPaper) {
    String paperId = citingPaper.getId();
    String citedPaperId = citedPaper.getId();

    if (!inCitations.containsKey(citedPaperId)) {
      inCitations.put(citedPaperId, new ArrayList<>());
    }
    inCitations.get(citedPaperId).add(citingPaper);

    if (!outCitations.containsKey(paperId)) {
      outCitations.put(paperId, new ArrayList<>());
    }
    outCitations.get(paperId).add(citedPaper);
  }

  private void buildPaperKeyPhraseRelation(Paper paper, String keyPhrase) {
    if (!keyPhraseToPapers.containsKey(keyPhrase)) {
      keyPhraseToPapers.put(keyPhrase, new ArrayList<>());
    }
    keyPhraseToPapers.get(keyPhrase).add(paper);
  }

  private void buildPaperVenueRelation(Paper paper, String venue) {
    if (!venueToPapers.containsKey(venue)) {
      venueToPapers.put(venue, new ArrayList<>());
    }
    venueToPapers.get(venue).add(paper);
  }

  private void buildYearVenueRelation(Paper paper, int year) {
    if (!yearToPapers.containsKey(year)) {
      yearToPapers.put(year, new ArrayList<>());
    }
    yearToPapers.get(year).add(paper);
  }
}
