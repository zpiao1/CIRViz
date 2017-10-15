package cir.cirviz.api.service;

import cir.cirviz.data.entity.Paper;
import java.util.List;

public interface YearService {

  List<Integer> getYears();

  List<Paper> getPapersByYear(int year);
}
