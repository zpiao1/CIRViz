package cir.cirviz.api.controller;

import cir.cirviz.data.entity.Author;
import cir.cirviz.data.entity.Paper;
import java.util.List;

public interface YearApi {

  List<Integer> getYears(
      String orderBy,
      boolean asc,
      long limit
  );

  long getYearsCount();

  int getYear(int year);

  List<Paper> getPapersByYear(
      int year,
      String orderBy,
      boolean asc,
      long limit
  );

  long getPapersCountByYear(int year);

  List<Author> getAuthorsByYear(
      int year,
      String orderBy,
      boolean asc,
      long limit
  );

  long getAuthorsCountByYear(int year);

  List<String> getVenuesByYear(
      int year,
      String orderBy,
      boolean asc,
      long limit
  );

  long getVenuesCountByYear(int year);

  List<String> getKeyPhrasesByYear(
      int year,
      String orderBy,
      boolean asc,
      long limit
  );

  long getKeyPhrasesCountByYear(int year);

  List<Paper> getCitationsByYear(
      int year,
      String orderBy,
      boolean asc,
      long limit
  );

  long getCitationsCountByYear(int year);

  List<Paper> getPapersCitingPapersAtYear(
      int year,
      String orderBy,
      boolean asc,
      long limit
  );

  long getPapersCountCitingPapersAtYear(int year);
}
