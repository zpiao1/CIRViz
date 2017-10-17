(() => {
  function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

  const svg = d3.select('#barChart'),
      margin = {top: 20, right: 20, bottom: 30, left: 40},
      width = +svg.attr('width') - margin.left - margin.right,
      height = +svg.attr('height') - margin.top - margin.bottom;

  const x = d3.scaleBand().rangeRound([0, width]).padding(0.1),
      y = d3.scaleLinear().rangeRound([height, 0]);

  const g = svg.append('g')
      .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');

  d3.json(
      'http://localhost:8080/api/venues/ICSE/papers?orderBy=year&asc=true&limit=100',
      (error, jsonArray) => {
        if (error) {
          throw error;
        }

        const map = new Map();
        jsonArray.forEach(json => {
          if (!map.has(json.year)) {
            map.set(json.year, 0);
          }
          map.set(json.year, map.get(json.year) + 1);
        });

        const data = [];
        map.forEach((papers, year) => data.push({year: year, papers: papers}));

        x.domain(data.map(d => d.year));
        y.domain([0, d3.max(data, d => d.papers)]);

        g.append('g')
            .attr('class', 'axis axis--x')
            .attr('transform', `translate(0,${height})`)
            .call(d3.axisBottom(x));

        g.append('g')
            .attr('class', 'axis axis--y')
            .call(d3.axisLeft(y).ticks(10))
            .append('text')
            .attr('transform', 'rotate(-90)')
            .attr('y', 6)
            .attr('dy', '0.71em')
            .attr('text-anchor', 'end')
            .text('papers');

        g.selectAll('.bar')
            .data(data)
            .enter().append('rect')
            .attr('class', 'bar')
            .attr('x', d => x(d.year))
            .attr('y', d => y(d.papers))
            .attr('width', x.bandwidth())
            .attr('height', d => height - y(d.papers))
            .style('fill', getRandomColor())
            .on('mouseover', d => {
              const rect = d3.event.target;
              if (!d.oldColor) {
                d.oldColor = rect.style.fill;
              }
              let newColor;
              do {
                newColor = getRandomColor();
              } while (newColor === d.oldColor);
              rect.style.fill = newColor;
              const tooltip = d3.select('.tooltip');
              tooltip.select('.year').html(`Year: ${d.year}`);
              tooltip.select('.papers').html(`Papers: ${d.papers}`);
              tooltip.style('display', 'block');
            })
            .on('mouseout', d => {
              d3.select('.tooltip').style('display', 'gone');
              d3.event.target.style.fill = d.oldColor;
            })
            .on('mousemove', d => d3.select('.tooltip')
                .style('top', `${d3.event.layerY + 10}px`)
                .style('left', `${d3.event.layerX + 10}px`));
      });
})();