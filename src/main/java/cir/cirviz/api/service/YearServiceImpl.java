package cir.cirviz.api.service;

import cir.cirviz.data.PaperRepository;
import cir.cirviz.data.entity.Paper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YearServiceImpl implements YearService {

  private final PaperRepository paperRepository;

  @Autowired
  public YearServiceImpl(PaperRepository paperRepository) {
    this.paperRepository = paperRepository;
  }

  @Override
  public List<Integer> getYears() {
    return new ArrayList<>(paperRepository.getYearToPapers().keySet());
  }

  @Override
  public List<Paper> getPapersByYear(int year) {
    return paperRepository.getYearToPapers().getOrDefault(year, Collections.emptyList());
  }
}
