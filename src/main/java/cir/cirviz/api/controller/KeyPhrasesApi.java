package cir.cirviz.api.controller;

import cir.cirviz.data.entity.Author;
import cir.cirviz.data.entity.Paper;
import java.util.List;

public interface KeyPhrasesApi {

  List<String> getKeyPhrases(
      String orderBy,
      boolean asc,
      long limit
  );

  long getKeyPhrasesCount();

  String getKeyPhrase(String keyPhrase);

  List<Paper> getPapersByKeyPhrase(
      String keyPhrase,
      String orderBy,
      boolean asc,
      long limit
  );

  long getPapersCountByKeyPhrase(String keyPhrase);

  List<Author> getAuthorsByKeyPhrase(
      String keyPhrase,
      String orderBy,
      boolean asc,
      long limit
  );

  long getAuthorsCountByKeyPhrase(String keyPhrase);

  List<Integer> getYearsByKeyPhrase(
      String keyPhrase,
      String orderBy,
      boolean asc,
      long limit
  );

  long getYearsCountByKeyPhrase(String keyPhrase);

  List<String> getVenuesByKeyPhrase(
      String keyPhrase,
      String orderBy,
      boolean asc,
      long limit
  );

  long getVenuesCountByKeyPhrase(String keyPhrase);

  List<Paper> getCitationsByKeyPhrase(
      String keyPhrase,
      String orderBy,
      boolean asc,
      long limit
  );

  long getCitationsCountByKeyPhrase(String keyPhrase);

  List<Paper> getPapersCitingPapersWithKeyPhrase(
      String keyPhrase,
      String orderBy,
      boolean asc,
      long limit
  );

  long getPapersCountCitingPapersWithKeyPhrase(String keyPhrase);
}
