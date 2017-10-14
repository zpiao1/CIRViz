package cir.cirviz.api.service;

import cir.cirviz.data.PaperRepository;
import cir.cirviz.data.model.Author;
import cir.cirviz.data.model.Paper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

  private final PaperRepository paperRepository;

  @Autowired
  public AuthorServiceImpl(PaperRepository paperRepository) {
    this.paperRepository = paperRepository;
  }

  @Override
  public List<Author> getAuthors() {
    return new ArrayList<>(paperRepository.getAuthors().values());
  }

  @Override
  public Author getAuthorById(String authorId) {
    return paperRepository.getAuthors().get(authorId);
  }

  @Override
  public List<Paper> getPapersByAuthor(Author author) {
    return getPapersByAuthorId(author.getId());
  }

  @Override
  public List<Paper> getPapersByAuthorId(String authorId) {
    return paperRepository.getAuthorToPapers().getOrDefault(authorId, Collections.emptyList());
  }
}
