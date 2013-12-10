package jobs

import jobs.steps.BioMarkerDumpDataStep
import jobs.steps.Step

class MarkerSelection extends AbstractAnalysisJob {

    @Override
    protected Step createDumpHighDimensionDataStep(Closure resultsHolder) {
        new BioMarkerDumpDataStep(
                temporaryDirectory: temporaryDirectory,
                resultsHolder: resultsHolder)
    }

    @Override
    protected List<String> getRStatements() {
        // set path to markerselection processor
        String sourceMarkerSelection = 'source(\'$pluginDirectory/MarkerSelection/MarkerSelection.R\')'
        // call for analysis for marker selection
        // TODO number of permutations is not set? numberOfPermutations = as.integer(\'$txtNumberOfPermutations\'),
        String markerSelectionLoad = '''MS.loader(
                            input.filename = \'outputfile\',
                            numberOfMarkers = as.integer(\'$txtNumberOfMarkers\'))'''
        // set path to heatmap png file generator
        String sourceHeatmap = 'source(\'$pluginDirectory/Heatmap/HeatmapLoader.R\')'
        // generate the actual heatmap png picture
        String createHeatmap = '''Heatmap.loader(
                            input.filename = \'heatmapdata\',
                            meltData       = FALSE,
                            imageWidth     = as.integer(\'$txtImageWidth\'),
                            imageHeight    = as.integer(\'$txtImageHeight\'),
                            pointsize      = as.integer(\'$txtImagePointsize\'))'''

        [ sourceMarkerSelection, markerSelectionLoad, sourceHeatmap, createHeatmap ]
    }

    @Override
    final String getForwardPath() {
        "/markerSelection/markerSelectionOut?jobName=${name}"
    }
}