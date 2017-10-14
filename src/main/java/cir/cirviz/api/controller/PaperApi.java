package cir.cirviz.api.controller;

import cir.cirviz.data.model.Author;
import cir.cirviz.data.model.Paper;
import java.util.List;

public interface PaperApi {

  List<Paper> getPapers(String title);

  Paper getPaperById(String paperId);

  List<Author> getAuthorsOfPaper(String paperId);

  int getYearOfPaper(String paperId);

  List<String> getKeyPhrasesOfPaper(String paperId);

  String getVenueOfPaper(String paperId);

  List<Paper> getOutCitationsOfPaper(String paperId);

  List<Paper> getInCitationsOfPaper(String paperId);
}
