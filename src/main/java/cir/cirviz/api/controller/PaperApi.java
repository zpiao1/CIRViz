package cir.cirviz.api.controller;

import cir.cirviz.data.entity.Author;
import cir.cirviz.data.entity.Paper;
import java.util.List;

public interface PaperApi {

  List<Paper> getPapers(
      String title,
      String orderBy,
      boolean asc,
      long limit
  );

  long getPapersCount();

  Paper getPaperById(String paperId);

  List<Author> getAuthorsOfPaper(
      String paperId,
      String orderBy,
      boolean asc,
      long limit
  );

  long getAuthorsCountOfPaper(String paperId);

  int getYearOfPaper(String paperId);

  List<String> getKeyPhrasesOfPaper(
      String paperId,
      String orderBy,
      boolean asc,
      long limit
  );

  long getKeyPhrasesCountOfPaper(String paperId);

  String getVenueOfPaper(String paperId);

  List<Paper> getOutCitationsOfPaper(
      String paperId,
      String orderBy,
      boolean asc,
      long limit
  );

  long getOutCitationsCountOfPaper(String paperId);

  List<Paper> getInCitationsOfPaper(
      String paperId,
      String orderBy,
      boolean asc,
      long limit
  );

  long getInCitationsCountOfPaper(String paperId);
}
