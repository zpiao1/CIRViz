(async () => {

    const AUTHOR_ID = '1742253';
    const YEAR = 2015;

    const svgWidth = 1000;
    const svgHeight = 500;

    const margin = {
        top: 50,
        left: 200,
        right: 200,
        bottom: 50
    };

    const radiusRatio = 0.5;

    const width = svgWidth - margin.left - margin.right;
    const height = svgHeight - margin.top - margin.bottom;

    const radius = Math.min(width, height) / 2;

    const color = d3.scaleOrdinal(d3.schemeCategory10);

    const div = d3.select('#chart');

    const svg = div.append('svg')
        .attr('width', svgWidth)
        .attr('height', svgHeight);

    const name = await getName(AUTHOR_ID);
    const data = await getData(AUTHOR_ID, YEAR);

    // Create pie chart
    const chart = svg.append('g')
        .attr('height', height)
        .attr('width', width)
        .attr('transform', `translate(${margin.left + width / 2},${margin.top + height / 2})`);

    const arc = d3.arc()
        .innerRadius(radius * radiusRatio)
        .outerRadius(radius);

    const arcText = d3.arc()
        .innerRadius(radius * 1.1)
        .outerRadius(radius * 1.1);

    const pie = d3.pie()
        .value(d => d.count);

    const path = chart.selectAll('path')
        .data(pie(data))
        .enter()
        .append('path')
        .attr('d', arc)
        .attr('fill', d => color(d.data.venue));

    // Label pie chart
    chart.selectAll('polyline')
        .data(pie(data))
        .enter()
        .append('polyline')
        .attr('fill', 'none')
        .attr('stroke', 'black')
        .attr('stroke-width', '1px')
        .attr('points', d => {
            var startPoint = arc.centroid(d);
            var midPoint = arcText.centroid(d);
            var endPoint = arcText.centroid(d);
            endPoint[0] = radius * 1.2 * ((d.startAngle + d.endAngle) / 2 < Math.PI ? 1 : -1);  
            return [startPoint, midPoint, endPoint];
        });

    chart.selectAll('text')
        .data(pie(data))
        .enter()
        .append('text')
        .attr('x', d => radius * 1.25 * ((d.startAngle + d.endAngle) / 2 < Math.PI ? 1 : -1))
        .attr('y', d => arcText.centroid(d)[1])
        .attr('dy', '0.3em')
        .style('text-anchor', d => (d.startAngle + d.endAngle) / 2 < Math.PI ? 'start' : 'end')
        .text(d => d.data.venue);

    // Label title
    svg.append('text')
        .attr('x', (margin.left + width + margin.right) / 2)
        .attr('y', 0)
        .attr('dy', '1em')
        .style('text-anchor', 'middle')
        .style('font-size', 20)
        .text(`Contributions of ${name} in ${YEAR}`);

    // Generate tooltips
    const tooltip = div.append('div')
        .attr('class', 'tooltip');

    tooltip.append('div')
        .attr('class', 'venue');

    tooltip.append('div')
        .attr('class', 'count');

    tooltip.append('div')
        .attr('class', 'percent');

    path.on('mouseover', d => {

        var total = d3.sum(data.map(d => d.count));
        var percent = Math.round(1000 * d.data.count / total) / 10;

        tooltip.select('.venue').html('Venue: ' + d.data.venue);
        tooltip.select('.count').html('Count: ' + d.data.count);
        tooltip.select('.percent').html('Percentage: ' + percent + '%');
        tooltip.style('display', 'block');
    });

    path.on('mousemove', d => {
        tooltip.style('left', (d3.event.layerX + 10) + 'px');
        tooltip.style('top', (d3.event.layerY + 10) + 'px')
    });

    path.on('mouseout', () => tooltip.style('display', 'none'));
})();

async function getName(authorId) {

    const author = await getJsonFromUrl(`http://localhost:8080/api/authors/${authorId}`);

    return author.name;
}

async function getData(authorId, year) {

    const papersCount = await getJsonFromUrl(`http://localhost:8080/api/authors/${authorId}/papers/count`);
    const papers = await getJsonFromUrl(`http://localhost:8080/api/authors/${authorId}/papers?limit=${papersCount}`);

    const venues = new Map();

    papers.forEach(paper => {

        if (paper.year != year) {
            return;
        }

        var venue = paper.venue || 'Other';

        var count = venues.get(venue);

        if (count) {
            venues.set(venue, count + 1);
        } else {
            venues.set(venue, 1);
        }
    });

    const data = [];

    venues.forEach((value, key) => {
        data.push({
            venue: key,
            count: value
        });
    });

    data.sort((d1, d2) => {

        if (d1.venue == 'Other') {
            return 1;
        } else if (d2.venue == 'Other') {
            return -1;
        }

        if (d1.count < d2.count) {
            return 1;
        } else if (d1.count > d2.count) {
            return -1;
        } else {
            return 0;
        }
    }); 

    return data;
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
