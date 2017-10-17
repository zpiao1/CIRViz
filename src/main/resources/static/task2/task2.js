const svg = d3.select('svg');
const width = +svg.attr('width');
const height = +svg.attr('height');

const format = d3.format(',d');

const color = d3.scaleOrdinal(d3.schemeCategory20);

const pack = d3.pack()
    .size([width, height])
    .padding(1.5);

d3.json(
    'http://localhost:8080/api/venues/ArXiv/papers?orderBy=inCitations&asc=false&limit=5')
    .get((error, papers) => {
      if (error) {
        throw error;
      }

      const data = papers.map(p => ({id: p.id, value: p.inCitations.length}));

      const root = d3.hierarchy({children: data})
          .sum(d => d.value)
          .each(d => console.log(d));

      const node = svg.selectAll('.node')
          .data(pack(root).leaves())
          .enter().append('g')
          .attr('class', 'node')
          .attr('transform', d => `translate(${d.x},${d.y})`);

      node.append('circle')
          .attr('id', d => d.data.id)
          .attr('r', d => d.r)
          .style('fill', d => color(d.package));

      node.append('clipPath')
          .attr('id', d => `clip-${d.data.id}`)
          .append('use')
          .attr('xlink:href', d => `#${d.data.id}`);

      node.append('text')
          .attr('clip-path', d => `url(#clip-${d.data.id})`)
          .selectAll('tspan')
          .data(d => papers.find(p => p.id === d.data.id).title.split(' '))
          .enter().append('tspan')
          .attr('x', 0)
          .attr('y', (d, i, nodes) => 13 + (i - nodes.length / 2 - 0.5) * 10)
          .text(d => d);

      node.append('title')
          .text(d => {
            const paper = papers.find(p => p.id === d.data.id);
            const authors = paper.authors.map(a => a.name);
            return `Title: ${paper.title}\n` +
                `Authors: ${authors.join(', ')}\n` +
                `InCitations: ${format(d.data.value)}`;
          });
    });