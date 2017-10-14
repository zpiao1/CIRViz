package cir.cirviz.api.service;

import cir.cirviz.data.model.Paper;
import java.util.List;

public interface VenueService {

  List<String> getVenues();

  List<Paper> getPapersByVenue(String venue);
}
