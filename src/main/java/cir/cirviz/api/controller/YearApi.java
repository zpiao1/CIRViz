package cir.cirviz.api.controller;

import cir.cirviz.data.model.Author;
import cir.cirviz.data.model.Paper;
import java.util.List;

public interface YearApi {

  List<Integer> getYears();

  int getYear(int year);

  List<Paper> getPapersByYear(int year);

  List<Author> getAuthorsByYear(int year);

  List<String> getVenuesByYear(int year);

  List<String> getKeyPhrasesByYear(int year);

  List<Paper> getCitationsByYear(int year);

  List<Paper> getPapersCitingPapersAtYear(int year);
}
