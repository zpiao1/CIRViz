package cir.cirviz.api.service;

import cir.cirviz.data.entity.Author;
import cir.cirviz.data.entity.Paper;
import java.util.List;

public interface PaperService {

  List<Paper> getPapers();

  Paper getPaperById(String paperId);

  List<Author> getAuthorsByPaper(Paper paper);

  List<Author> getAuthorsByPaperId(String paperId);

  String getVenueByPaper(Paper paper);

  String getVenueByPaperId(String paperId);

  int getYearByPaper(Paper paper);

  int getYearByPaperId(String paperId);

  List<String> getKeyPhrasesByPaper(Paper paper);

  List<String> getKeyPhrasesByPaperId(String paperId);

  List<Paper> getInCitationsByPaper(Paper paper);

  List<Paper> getInCitationsByPaperId(String paperId);

  List<Paper> getOutCitationsByPaper(Paper paper);

  List<Paper> getOutCitationsByPaperId(String paperId);
}
