package cir.cirviz.api.service;

import cir.cirviz.data.model.Paper;
import java.util.List;

public interface YearService {

  List<Integer> getYears();

  List<Paper> getPapersByYear(int year);
}
