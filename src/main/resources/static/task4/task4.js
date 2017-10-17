(async () => {
  const types = [
    'circle',
    'square',
    'triangle-up',
    'diamond',
    'cross',
    'triangle-down'];
  let w = window.innerWidth;
  let h = window.innerHeight;

  const keys = {
    c: true,
    s: true,
    t: true,
    r: true,
    x: true,
    d: true,
    l: true,
    m: true,
    h: true,
    1: true,
    2: true,
    3: true,
    0: true,
  };

  let focus_node = null, highlight_node = null;

  let text_center = false, outline = false;

  let min_score = 0, max_score = 1;

  const color = d3.scale.linear()
      .domain([min_score, (min_score + max_score) / 2, max_score])
      .range(['lime', 'yellow', 'red']);

  const highlight_color = 'blue', highlight_trans = 0.1;

  const size = d3.scale.pow().exponent(1)
      .domain([1, 100])
      .range([8, 24]);

  const force = d3.layout.force()
      .linkDistance(60)
      .charge(-300)
      .size([w, h]);

  const default_node_color = '#ccc';
  const default_link_color = '#888';
  const nominal_base_node_size = 8;
  const nominal_text_size = 10;
  const max_text_size = 24;
  const nominal_stroke = 1.5;
  const max_stroke = 4.5;
  const max_base_node_size = 36;
  const min_zoom = 0.1;
  const max_zoom = 7;
  const svg = d3.select('#chart').append('svg');
  const zoom = d3.behavior.zoom().scaleExtent([min_zoom, max_zoom]);
  const g = svg.append('g');
  svg.style('cursor', 'move');

  const tooltip = d3.select('#chart')
      .append('div')
      .attr('class', 'tooltip');

  tooltip.append('div')
      .attr('class', 'title');

  tooltip.append('div')
      .attr('class', 'authors');

  tooltip.append('div')
      .attr('class', 'in-citations');

  const graph = await processData();

  const linkedByIndex = {};
  graph.links.forEach(d => {
    linkedByIndex[`${d.source},${d.target}`] = true;
  });

  function isConnected(a, b) {
    return linkedByIndex[`${a.index},${b.index}`]
        || linkedByIndex[`${b.index},${a.index}`] || a.index === b.index;
  }

  function hasConnections(a) {
    for (let property in linkedByIndex) {
      const s = property.split(',');
      if ((s[0] === a.index || s[1] === a.index)
          && linkedByIndex[property]) {
        return true;
      }
    }
    return false;
  }

  force
      .nodes(graph.nodes)
      .links(graph.links)
      .start();

  const link = g.selectAll('.link')
      .data(graph.links)
      .enter().append('line')
      .attr('class', 'link')
      .style('stroke-width', nominal_stroke)
      .style('stroke', d => {
        if (isNumber(d.score) && d.score >= 0) {
          return color(d.score);
        } else {
          return default_link_color;
        }
      });

  const node = g.selectAll('.node')
      .data(graph.nodes)
      .enter().append('g')
      .attr('class', 'node')
      .call(force.drag);

  node.on('dblclick.zoom', d => {
    d3.event.stopPropagation();
    const dcx = (window.innerWidth / 2 - d.x * zoom.scale());
    const dcy = (window.innerHeight / 2 - d.y * zoom.scale());
    zoom.translate([dcx, dcy]);
    g.attr('transform', `translate(${dcx},${dcy})scale(${zoom.scale()})`);
  });

  let tocolor = 'fill';
  let towhite = 'stroke';
  if (outline) {
    tocolor = 'stroke';
    towhite = 'fill';
  }

  const circle = node.append('path')
      .attr('d', d3.svg.symbol()
          .size(d => Math.PI * Math.pow(size(d.size) || nominal_base_node_size,
              2))
          .type(d => d.type))
      .style(tocolor, d => {
        if (isNumber(d.score) && d.score >= 0) {
          return color(d.score);
        } else {
          return default_node_color;
        }
      })
      .style('stroke-width', nominal_stroke)
      .style(towhite, 'white');

  const text = g.selectAll('.text')
      .data(graph.nodes)
      .enter().append('text')
      .attr('dy', '.35em')
      .style('font-size', `${nominal_text_size}px`);

  if (text_center) {
    text.text(d => '')
        .style('text-anchor', 'middle');
  } else {
    text.attr('dx', d => size(d.size) || nominal_base_node_size)
        .text(d => '');
  }

  node.on('mouseover', set_highlight)
      .on('mousedown', d => {
        d3.event.stopPropagation();
        focus_node = d;
        set_focus(d);
        if (highlight_node === null) {
          set_highlight(d);
        }
      })
      .on('mouseout', d => exit_highlight())
      .on('mousemove', d => tooltip.style('top', `${d3.event.layerY + 10}px`)
          .style('left', `${d3.event.layerX + 10}px`));

  d3.select(window).on('mouseup', () => {
    if (focus_node !== null) {
      focus_node = null;
      if (highlight_trans < 1) {
        circle.style('opacity', 1);
        text.style('opacity', 1);
        link.style('opacity', 1);
      }
    }

    if (highlight_node === null) {
      exit_highlight();
    }
  });

  function exit_highlight() {
    highlight_node = null;
    if (focus_node === null) {
      svg.style('cursor', 'move');
      if (highlight_color !== 'white') {
        circle.style('towhite', 'white');
        text.style('font-weight', 'normal');
        link.style('stroke', o => isNumber(o.score) && o.score >= 0
            ? color(o.score)
            : default_link_color);
      }
    }
    hideTooltip();
  }

  function set_focus(d) {
    if (highlight_trans < 1) {
      circle.style('opacity', o => isConnected(d, o) ? 1 : highlight_trans);
      text.style('opacity', o => isConnected(d, o) ? 1 : highlight_trans);
      link.style('opacity',
          o => o.source.index === d.index || o.target.index === d.index
              ? 1
              : highlight_trans);
    }
  }

  function set_highlight(d) {
    svg.style('cursor', 'pointer');
    if (focus_node !== null) {
      d = focus_node;
    }
    highlight_node = d;

    if (highlight_color !== 'white') {
      circle.style(towhite, o => isConnected(d, o) ? highlight_color : 'white');
      text.style('font-weight', o => isConnected(d, o) ? 'bold' : 'normal');
      link.style('stroke',
          o => o.source.index === d.index || o.target.index === d.index
              ? highlight_color
              : ((isNumber(o.score) && o.score >= 0)
                  ? color(o.score)
                  : default_link_color));
    }

    showTooltip(d);
  }

  function showTooltip(d) {
    const authors = d.authors.join(', ');
    const title = d.title;
    const inCitations = d.inCitations;
    tooltip.select('.title').html(`Title: ${title}`);
    tooltip.select('.authors').html(`Authors: ${authors}`);
    tooltip.select('.in-citations').html(`InCitations: ${inCitations}`);
    tooltip.style('display', 'block');
  }

  function hideTooltip() {
    tooltip.style('display', 'none');
  }

  zoom.on('zoom', () => {

    let stroke = nominal_stroke;
    if (nominal_stroke * zoom.scale() > max_stroke) {
      stroke = max_stroke
          / zoom.scale();
    }
    link.style('stroke-width', stroke);
    circle.style('stroke-width', stroke);

    let base_radius = nominal_base_node_size;
    if (nominal_base_node_size * zoom.scale()
        > max_base_node_size) {
      base_radius = max_base_node_size / zoom.scale();
    }
    circle.attr('d', d3.svg.symbol()
        .size(d => Math.PI * Math.pow(size(d.size) * base_radius
            / nominal_base_node_size || base_radius, 2))
        .type(d => d.type));

    if (!text_center) {
      text.attr('dx', d => (size(d.size) * base_radius / nominal_base_node_size
          || base_radius));
    }

    let text_size = nominal_text_size;
    if (nominal_text_size * zoom.scale()
        > max_text_size) {
      text_size = max_text_size / zoom.scale();
    }
    text.style('font-size', `${text_size}px`);

    g.attr('transform',
        `translate(${d3.event.translate})scale(${d3.event.scale})`);
  });
  svg.call(zoom);

  resize();
  d3.select(window).on('resize', resize).on('keydown', keydown);

  force.on('tick', () => {
    node.attr('transform', d => `translate(${d.x},${d.y})`);
    text.attr('transform', d => `translate(${d.x},${d.y})`);
    link.attr('x1', d => d.source.x)
        .attr('y1', d => d.source.y)
        .attr('x2', d => d.target.x)
        .attr('y2', d => d.target.y);

    node.attr('cx', d => d.x)
        .attr('cy', d => d.y);
  });

  function resize() {
    const width = window.innerWidth,
        height = window.innerHeight;
    svg.attr('width', width).attr('height', height);
    force.size([
      force.size()[0] + (width - w) / zoom.scale(),
      force.size()[1] + (height - h) / zoom.scale()]).resume();
    w = width;
    h = height;
  }

  function keydown() {
    if (d3.event.keyCode === 32) {
      force.stop();
    } else if (d3.event.keyCode >= 48 && d3.event.keyCode <= 90
        && !d3.event.ctrlKey && !d3.event.metaKey) {
      switch (String.fromCharCode(d3.event.keyCode)) {
        case 'C':
          keys.c = !keys.c;
          break;
        case 'S':
          keys.s = !keys.s;
          break;
        case 'T':
          keys.t = !keys.t;
          break;
        case 'R':
          keys.r = !keys.r;
          break;
        case 'X':
          keys.x = !keys.x;
          break;
        case 'D':
          keys.d = !keys.d;
          break;
        case 'L':
          keys.l = !keys.l;
          break;
        case 'M':
          keys.m = !keys.m;
          break;
        case 'H':
          keys.h = !keys.h;
          break;
        case '1':
          keys['1'] = !keys['1'];
          break;
        case '2':
          keys['2'] = !keys['2'];
          break;
        case '3':
          keys['3'] = !keys['3'];
          break;
        case '0':
          keys['0'] = !keys['0'];
          break;
      }

      link.style('display', d => {
        const flag = vis_by_type(d.source.type) && vis_by_type(d.target.type)
            && vis_by_node_score(d.source.score) && vis_by_node_score(
                d.target.score) && vis_by_link_score(d.score);
        linkedByIndex[`${d.source.index},${d.target.index}`] = flag;
        return flag ? 'inline' : 'none';
      });
      node.style('display', d => (keys['0'] || hasConnections(d))
      && vis_by_type(d.type)
      && vis_by_node_score(d.score) ? 'inline' : 'none');
      text.style('display', d => (keys['0'] || hasConnections(d))
      && vis_by_type(d.type)
      && vis_by_node_score(d.score) ? 'inline' : 'none');
      if (highlight_node !== null) {
        if ((keys['0'] || hasConnections(highlight_node)) && vis_by_type(
                highlight_node.type) && vis_by_node_score(
                highlight_node.score)) {
          if (focus_node !== null) {
            set_focus(focus_node);
          }
          set_highlight(highlight_node);
        } else {
          exit_highlight();
        }
      }
    }
  }

  function getJsonFromUrl(url) {
    return new Promise((resolve, reject) => {
      d3.json(url, (error, json) => {
        if (error) {
          reject(error);
        }
        resolve(json);
      });
    });
  }

  function mapPaperToModel(paper) {
    return {
      id: paper.id,
      title: paper.title,
      authors: paper.authors.map(a => a.name),
      inCitations: paper.inCitations.map(i => i),
    };
  }

  async function processData() {
    const data = new Map();
    const links = new Set();

    const jsonArray = await getJsonFromUrl(
        'http://localhost:8080/api/papers?title=parity%20check%20codes%20over');
    const basePaper = mapPaperToModel(jsonArray[0]);
    data.set(basePaper.id, basePaper);
    const level1Promises = Promise.all(basePaper.inCitations.map(
        id => getJsonFromUrl(`http://localhost:8080/api/papers/${id}`)));
    const level1Papers = await level1Promises;
    const level1Models = level1Papers.map(mapPaperToModel);
    level1Models.forEach(model => {
      data.set(model.id, model);
      links.add({source: model.id, target: basePaper.id});
    });
    for (const model of level1Models) {
      if (model.inCitations.length > 0) {
        const level2Promises = Promise.all(model.inCitations.map(
            id => getJsonFromUrl(`http://localhost:8080/api/papers/${id}`)));
        const level2Papers = await level2Promises;
        const level2Models = level2Papers.map(mapPaperToModel);
        level2Models.forEach(l2Model => {
          data.set(l2Model.id, l2Model);
          links.add({source: l2Model.id, target: model.id});
        });
      }
    }
    const dataArray = [...data.values()];
    const totalInCitations = dataArray.reduce(
        (sum, paper) => sum + paper.inCitations.length, 0);

    const graphDataArray = dataArray.map(paper => ({
      ...paper,
      inCitations: paper.inCitations.length,
      size: Math.round((paper.inCitations.length + 1) / (totalInCitations
          + dataArray.length) * 1000),
      score: Math.random(),
      type: types[getRandomInt(0, 5)],
    }));

    const paperIdsArray = [...data.keys()];

    const linksArray = [...links].map(link => ({
      source: paperIdsArray.indexOf(link.source),
      target: paperIdsArray.indexOf(link.target),
    }));
    return {
      graph: [],
      nodes: graphDataArray,
      links: linksArray,
      directed: true,
      multigraph: false,
    };
  }

  function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }

  function vis_by_type(type) {
    const index = types.indexOf(type);
    if (index !== -1) {
      return [keys.c, keys.s, keys.t, keys.r, keys.x, keys.d][index];
    } else {
      return true;
    }
  }

  function vis_by_node_score(score) {
    if (isNumber(score)) {
      if (score >= 0.666) {
        return keys.h;
      } else if (score >= 0.333) {
        return keys.m;
      } else if (score >= 0) {
        return keys.l;
      }
    }
    return true;
  }

  function vis_by_link_score(score) {
    if (isNumber(score)) {
      if (score >= 0.666) {
        return keys['3'];
      } else if (score >= 0.333) {
        return keys['2'];
      } else if (score >= 0) {
        return keys['1'];
      }
    }
    return true;
  }

  function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
  }
})();
