package cir.cirviz.api.service;

import cir.cirviz.data.PaperRepository;
import cir.cirviz.data.model.Paper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VenueServiceImpl implements VenueService {

  private final PaperRepository paperRepository;

  @Autowired
  public VenueServiceImpl(PaperRepository paperRepository) {
    this.paperRepository = paperRepository;
  }

  @Override
  public List<String> getVenues() {
    return new ArrayList<>(paperRepository.getVenueToPapers().keySet());
  }

  @Override
  public List<Paper> getPapersByVenue(String venue) {
    return paperRepository.getVenueToPapers().getOrDefault(venue, Collections.emptyList());
  }
}
