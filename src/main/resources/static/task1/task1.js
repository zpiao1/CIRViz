(async () => {

    const svg = d3.select('svg');

    const margin = {
        top: 50,
        left: 200,
        right: 50,
        bottom: 100
    };

    const width = +svg.attr('width') - margin.left - margin.right;
    const height = +svg.attr('height') - margin.top - margin.bottom;

    const color = d3.scaleOrdinal(d3.schemeCategory10);

    const authors = await getData();

    var data = authors.map(author => {
        return {
            name: author.name,
            count: author.papers.length
        }
    });

    var keys = data.map(author => author.name);
    var values = data.map(author => author.count);

    const x = d3.scaleLinear()
        .domain([0, d3.max(values)])
        .range([0, width]);

    const y = d3.scaleBand()
        .domain(keys)
        .rangeRound([0, height])
        .padding(0.05, 0.05);

    const chart = svg.append('g')
        .attr('height', height)
        .attr('width', width)
        .attr('transform', `translate(${margin.left},${margin.top})`);

    chart.append('g')
        .attr('transform', `translate(0,${height})`)
        .call(d3.axisBottom(x));

    chart.append('g')
        .call(d3.axisLeft(y));

    const bar = chart.selectAll('.bar')
        .data(data)
        .enter()
        .append('g')
        .attr('class', 'bar');

    bar.append('rect')
        .attr('x', 0)
        .attr('y', d => y(d.name))
        .attr('height', y.bandwidth())
        .attr('width', d => x(d.count))
        .attr('fill', d => color(d.name));

    bar.append('text')
        .attr('x', d => x(d.count) + 5)
        .attr('y', d => y(d.name))
        .attr('dy', '1em')
        .style('text-anchor', 'start')
        .text(d => d.count);

    // Label title
    svg.append('text')
        .attr('x', (margin.left + width + margin.right) / 2)
        .attr('y', 0)
        .attr('dy', '1em')
        .style('text-anchor', 'middle')
        .style('font-size', 20)
        .text('Top 10 Authors for ArXiv');

    // Label x-axis
    svg.append('text')
        .attr('x', margin.left + width / 2)
        .attr('y', margin.top + height + margin.bottom / 2)
        .style('text-anchor', 'middle')
        .text('Number of Publications');

    // Label y-axis
    svg.append('text')
        .attr('x', 0)
        .attr('y', margin.top + height / 2)
        .style('text-anchor', 'start')
        .text('Author');
})();

async function getData() {

    const papersCount = await getJsonFromUrl('http://localhost:8080/api/venues/ArXiv/papers/count');
    const papers = await getJsonFromUrl(`http://localhost:8080/api/venues/ArXiv/papers?limit=${papersCount}`);
    const authors = new Map();

    papers.forEach(paper => {
        paper.authors.forEach(author => {

            const authorData = authors.get(author.id);

            if (!authorData) {
                authors.set(author.id, {
                    name: author.name,
                    id: author.id,
                    papers: new Map()
                });
            } else {
                const newPapers = authorData.papers;
                newPapers.set(paper.id, paper);
            }
        });
    });

    const authorsArray = [...authors.values()];

    authorsArray.sort((a1, a2) => {
        if (a1.papers.size > a2.papers.size) {
            return -1;
        } else if (a1.papers.size < a2.papers.size) {
            return 1;
        } else if (a1.id < a2.id) {
            return -1;
        } else {
            return 1;
        }
    });

    return authorsArray.slice(0, 10)
        .map(author => ({...author, papers: [...author.papers.values()]}));
}

function getJsonFromUrl(url) {
    return new Promise((resolve, reject) => {
        d3.json(url, (error, json) => {
            if (error) {
                reject(error);
            } else {
                resolve(json);
            }
        });
    });
}
