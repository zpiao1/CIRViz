package cir.cirviz.api.controller;

import cir.cirviz.data.entity.Author;
import cir.cirviz.data.entity.Paper;
import java.util.List;

public interface VenueApi {

  List<String> getVenues(
      String orderBy,
      boolean asc,
      long limit
  );

  long getVenuesCount();

  String getVenue(String venue);

  List<Paper> getPapersByVenue(
      String venue,
      String orderBy,
      boolean asc,
      long limit
  );

  long getPapersCountByVenue(String venue);

  List<Author> getAuthorsByVenue(
      String venue,
      String orderBy,
      boolean asc,
      long limit
  );

  long getAuthorsCountByVenue(String venue);

  List<Integer> getYearsByVenue(
      String venue,
      String orderBy,
      boolean asc,
      long limit
  );

  long getYearsCountByVenue(String venue);

  List<String> getKeyPhrasesByVenue(
      String venue,
      String orderBy,
      boolean asc,
      long limit
  );

  long getKeyPhrasesCountByVenue(String venue);

  List<Paper> getCitationsByVenue(
      String venue,
      String orderBy,
      boolean asc,
      long limit
  );

  long getCitationsCountByVenue(String venue);

  List<Paper> getPapersCitingPapersAtVenue(
      String venue,
      String orderBy,
      boolean asc,
      long limit
  );

  long getPapersCountCitingPapersAtVenue(String venue);
}
