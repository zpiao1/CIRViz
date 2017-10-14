package cir.cirviz.api.controller;

import cir.cirviz.data.model.Author;
import cir.cirviz.data.model.Paper;
import java.util.List;

public interface VenueApi {

  List<String> getVenues();

  String getVenue(String venue);

  List<Paper> getPapersByVenue(String venue);

  List<Author> getAuthorsByVenue(String venue);

  List<Integer> getYearsByVenue(String venue);

  List<String> getKeyPhrasesByVenue(String venue);

  List<Paper> getCitationsByVenue(String venue);

  List<Paper> getPapersCitingPapersAtVenue(String venue);
}
