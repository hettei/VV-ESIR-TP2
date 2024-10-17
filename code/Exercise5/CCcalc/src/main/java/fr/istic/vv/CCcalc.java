package fr.istic.vv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.github.javaparser.utils.SourceRoot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;

public class CCcalc {
/*
    private static void generateHistogram(Map<String, Ex5Visitor.MethodInfo> methodComplexityMap) throws IOException {
        HistogramDataset dataset = new HistogramDataset();
        double[] ccValues = methodComplexityMap.values().stream()
            .mapToDouble(info -> info.cyclomaticComplexity)
            .toArray();

        dataset.addSeries("Complexité Cyclomatique", ccValues, 5); // Ajustez la largeur des bins si nécessaire

        JFreeChart histogram = ChartFactory.createHistogram(
            "Histogramme de la Complexité Cyclomatique",
            "Valeur CC",
            "Fréquence",
            dataset
        );

        ChartUtils.saveChartAsPNG(new File("cc_histogram.png"), histogram, 800, 600);
    }*/
    private static PrintWriter csvWriter;
    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        try{
            csvWriter = new PrintWriter(new FileWriter("cc_report_"+file.getName()+".csv"));
            csvWriter.println("Class;Method;CC");
            Ex5Visitor printer = new Ex5Visitor();
            
            root.parse("", (localPath, absolutePath, result) -> {
                result.ifSuccessful(unit -> unit.accept(printer, null));
                return SourceRoot.Callback.Result.DONT_SAVE;
            }); 
            csvWriter.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static PrintWriter getPrintWriter(){
        return csvWriter;
    }
}
