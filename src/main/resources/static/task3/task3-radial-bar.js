(() => {
  function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

  const svg = d3.select('#radialBar'),
      width = +svg.attr('width'),
      height = +svg.attr('height'),
      innerRadius = 180,
      outerRadius = Math.min(width, height) / 2,
      g = svg.append('g')
          .attr('transform', `translate(${width / 2},${height / 2})`);

  const x = d3.scaleBand()
      .range([0, 2 * Math.PI])
      .align(0);

  const y = d3.scaleRadial()
      .range([innerRadius, outerRadius]);

  const z = d3.scaleOrdinal()
      .range([getRandomColor()]);

  d3.json(
      'http://localhost:8080/api/venues/ICSE/papers?orderBy=year&asc=true&limit=100')
      .get((error, jsonArray) => {
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
        map.forEach((papers, year) => data.push(
            {year: year, papers: papers, total: papers}));
        data.columns = ['year', 'papers'];

        x.domain(data.map(d => d.year));
        y.domain([0, d3.max(data, d => d.total)]);
        z.domain(['year']);

        g.append('g')
            .selectAll('g')
            .data(d3.stack().keys(data.columns.slice(1))(data))
            .enter().append('g')
            .attr('fill', d => z(d.key))
            .selectAll('path')
            .data(d => d)
            .enter().append('path')
            .attr('d', d3.arc()
                .innerRadius(d => y(d[0]))
                .outerRadius(d => y(d[1]))
                .startAngle(d => x(d.data.year))
                .endAngle(d => x(d.data.year) + x.bandwidth())
                .padAngle(0.01)
                .padRadius(innerRadius))
            .on('mouseover', d => {
              const tooltip = d3.select('.tooltip');
              tooltip.select('.year').html(`Year: ${d.data.year}`);
              tooltip.select('.papers').html(`Papers: ${d.data.papers}`);
              tooltip.style('display', 'block');
            })
            .on('mouseout', d => d3.select('.tooltip')
                .style('display', 'none'))
            .on('mousemove', d => d3.select('.tooltip')
                .style('top', `${d3.event.layerY + 10}px`)
                .style('left', `${d3.event.layerX + 10}px`));

        const label = g.append('g')
            .selectAll('g')
            .data(data)
            .enter().append('g')
            .attr('text-anchor', 'middle')
            .attr('transform',
                d => `rotate(${(x(d.year) + x.bandwidth() / 2) * 180 / Math.PI
                - 90})translate(${innerRadius},0)`);

        label.append('line')
            .attr('x2', -5)
            .attr('stroke', '#000');

        label.append('text')
            .attr('transform',
                d => (x(d.year) + x.bandwidth() / 2 + Math.PI / 2)
                % (2 * Math.PI)
                < Math.PI
                    ? 'rotate(90)translate(0,16)'
                    : 'rotate(-90)translate(0,-9)')
            .text(d => d.year);

        const yAxis = g.append('g')
            .attr('text-anchor', 'middle');

        const yTick = yAxis
            .selectAll('g')
            .data(y.ticks(5).slice(1))
            .enter().append('g');

        yTick.append('circle')
            .attr('fill', 'none')
            .attr('stroke', '#000')
            .attr('r', y);

        yTick.append('text')
            .attr('y', d => -y(d))
            .attr('dy', '0.35em')
            .attr('fill', 'none')
            .attr('stroke', '#fff')
            .attr('stroke-width', 5)
            .text(y.tickFormat(5, 's'));

        yTick.append('text')
            .attr('y', d => -y(d))
            .attr('dy', '0.35em')
            .text(y.tickFormat(5, 's'));

        yAxis.append('text')
            .attr('y', d => -y(y.ticks(5).pop()))
            .attr('dy', '-1em')
            .text('Papers');

        const legend = g.append('g')
            .selectAll('g')
            .data(data.columns.slice(1).reverse())
            .enter().append('g')
            .attr('transform',
                (d, i) => `translate(-40,${(i - (data.columns.length
                    - 1) / 2) * 20})`);

        legend.append('rect')
            .attr('width', 18)
            .attr('height', 18)
            .attr('fill', z);

        legend.append('text')
            .attr('x', 24)
            .attr('y', 9)
            .attr('dy', '0.35em')
            .text(d => d);
      });
})();