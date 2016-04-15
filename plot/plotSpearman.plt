set xrange [1:35]
set yrange [0.6:1]
plot 'plotQtdAlternatives.out' using 1:4:5 w yerrorbars ls 1, '' using 1:4 w lines ls 1, 'plotQtdFeatures.out' using 1:4:5 w yerrorbars ls 1, '' using 1:4 w lines ls 1, 'plotQtdCriteria.out' using 1:4:5 w yerrorbars ls 1, '' using 1:4 w lines ls 1