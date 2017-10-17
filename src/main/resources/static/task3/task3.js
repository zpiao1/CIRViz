const tooltip = d3.select('#chart')
    .append('div')
    .attr('class', 'tooltip');
tooltip.append('div')
    .attr('class', 'year');
tooltip.append('div')
    .attr('class', 'papers');

function setVisible(svg) {
  svg.style.display = 'inline';
}

function setInvisible(svg) {
  svg.style.display = 'none';
}

const barChart = document.querySelector('#barChart');
const radialBar = document.querySelector('#radialBar');
const radialBarChoice = document.querySelector('#chartChoice1');
const barChartChoice = document.querySelector('#chartChoice2');
barChartChoice.addEventListener('click', () => {
  setVisible(barChart);
  setInvisible(radialBar);
});
radialBarChoice.addEventListener('click', () => {
  setVisible(radialBar);
  setInvisible(barChart);
});