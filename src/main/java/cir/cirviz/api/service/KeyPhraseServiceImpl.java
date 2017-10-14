package cir.cirviz.api.service;

import cir.cirviz.data.PaperRepository;
import cir.cirviz.data.model.Paper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyPhraseServiceImpl implements KeyPhraseService {

  private final PaperRepository paperRepository;

  @Autowired
  public KeyPhraseServiceImpl(PaperRepository paperRepository) {
    this.paperRepository = paperRepository;
  }

  @Override
  public List<String> getKeyPhrases() {
    return new ArrayList<>(paperRepository.getKeyPhraseToPapers().keySet());
  }

  @Override
  public List<Paper> getPapersByKeyPhrase(String keyPhrase) {
    return paperRepository.getKeyPhraseToPapers().getOrDefault(keyPhrase, Collections.emptyList());
  }
}
