package cir.cirviz.data;

import cir.cirviz.data.model.Author;
import cir.cirviz.data.model.Paper;
import java.util.List;
import java.util.Map;

public interface PaperRepository {

  Map<String, Paper> getPapers();

  Map<String, Author> getAuthors();

  Map<String, List<Author>> getPaperToAuthors();

  Map<String, List<Paper>> getAuthorToPapers();

  Map<String, List<Paper>> getInCitations();

  Map<String, List<Paper>> getOutCitations();

  Map<String, List<Paper>> getKeyPhraseToPapers();

  Map<String, List<Paper>> getVenueToPapers();

  Map<Integer, List<Paper>> getYearToPapers();

  void addPaper(Paper paper);

  void save();
}
