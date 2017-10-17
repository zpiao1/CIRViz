#CIRViz - Visualization of conferece papers
This project contains 2 parts:
* API: See `/src/main/java/cir/cirviz/api/`. This provides RESTful APIs for the paper data collected. Mapped to URL `/api`
* Visualization: See `/src/main/resources/static/`. This serves HTML pages written using D3. Each page queries from the API and displays the corresponding visualization. URLs: `/resources/static/task2/index.html`, `/resources/static/task3/index.html`, `/resources/static/task4/index.html`.

###Note
Please put the 200,000 lines JSON file in `/src/main/resources/data`, and the file name should be `dataset.json` (as defined in `application.properties`).