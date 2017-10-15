package cir.cirviz.api.service;

import cir.cirviz.data.entity.Paper;
import java.util.List;

public interface KeyPhraseService {

  List<String> getKeyPhrases();

  List<Paper> getPapersByKeyPhrase(String keyPhrase);

}
