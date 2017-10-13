package cir.cirviz.data;

import cir.cirviz.data.model.Paper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ParserService {

  private static Logger logger = LoggerFactory.getLogger(ParserService.class);

  @Value("${dataset.path}")
  private Resource datasetResource;

  private PaperRepository paperRepository;

  @Autowired
  public ParserService(PaperRepository paperRepository) {
    this.paperRepository = paperRepository;
  }

  @PostConstruct
  public void parse() {
    logger.info("Parsing");
    try {
      InputStream inputStream = datasetResource.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      ObjectMapper mapper = new ObjectMapper();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        Paper paper = mapper.readValue(line, Paper.class);
        paperRepository.addPaper(paper);
        logger.info("Added paper: " + paper.getTitle());
      }
      paperRepository.buildRelations();
    } catch (FileNotFoundException e) {
      logger.error("Dataset Not found");
    } catch (IOException e) {
      logger.error("parse(): " + e.getMessage());
    }
    logger.info("Finished parsing, enjoy ^^");
  }

}
