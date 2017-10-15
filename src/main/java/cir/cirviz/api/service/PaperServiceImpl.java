package cir.cirviz.api.service;

import cir.cirviz.data.PaperRepository;
import cir.cirviz.data.entity.Author;
import cir.cirviz.data.entity.Paper;
import com.sun.istack.internal.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaperServiceImpl implements PaperService {

  private final PaperRepository paperRepository;

  @Autowired
  public PaperServiceImpl(PaperRepository paperRepository) {
    this.paperRepository = paperRepository;
  }

  @Override
  public List<Paper> getPapers() {
    return new ArrayList<>(paperRepository.getPapers().values());
  }

  @Override
  public List<Author> getAuthorsByPaper(Paper paper) {
    if (paper == null) {
      return Collections.emptyList();
    } else {
      return paper.getAuthors();
    }
  }

  @Override
  public List<Author> getAuthorsByPaperId(String paperId) {
    Paper paper = getPaperById(paperId);
    return getAuthorsByPaper(paper);
  }

  @Override
  @Nullable
  public String getVenueByPaper(Paper paper) {
    if (paper == null) {
      return null;
    } else {
      return paper.getVenue();
    }
  }

  @Override
  public String getVenueByPaperId(String paperId) {
    Paper paper = getPaperById(paperId);
    return getVenueByPaper(paper);
  }

  @Override
  public int getYearByPaper(Paper paper) {
    if (paper == null) {
      return -1;
    } else {
      return paper.getYear();
    }
  }

  @Override
  public int getYearByPaperId(String paperId) {
    Paper paper = paperRepository.getPapers().get(paperId);
    return getYearByPaper(paper);
  }

  @Override
  public List<String> getKeyPhrasesByPaper(Paper paper) {
    if (paper == null) {
      return Collections.emptyList();
    } else {
      return paper.getKeyPhrases();
    }
  }

  @Override
  public List<String> getKeyPhrasesByPaperId(String paperId) {
    Paper paper = paperRepository.getPapers().get(paperId);
    return getKeyPhrasesByPaper(paper);
  }

  @Override
  public List<Paper> getInCitationsByPaper(Paper paper) {
    if (paper == null) {
      return Collections.emptyList();
    } else {
      return getInCitationsByPaperId(paper.getId());
    }
  }

  @Override
  public List<Paper> getInCitationsByPaperId(String paperId) {
    return paperRepository.getInCitations().getOrDefault(paperId, Collections.emptyList());
  }

  @Override
  public List<Paper> getOutCitationsByPaper(Paper paper) {
    if (paper == null) {
      return Collections.emptyList();
    } else {
      return getOutCitationsByPaperId(paper.getId());
    }
  }

  @Override
  public List<Paper> getOutCitationsByPaperId(String paperId) {
    return paperRepository.getOutCitations().getOrDefault(paperId, Collections.emptyList());
  }

  public Paper getPaperById(String paperId) {
    return paperRepository.getPapers().get(paperId);
  }
}
