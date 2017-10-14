package cir.cirviz.api.service;

import cir.cirviz.data.model.Author;
import cir.cirviz.data.model.Paper;
import java.util.List;

public interface AuthorService {

  List<Author> getAuthors();

  Author getAuthorById(String authorId);

  List<Paper> getPapersByAuthor(Author author);

  List<Paper> getPapersByAuthorId(String authorId);
}
