package fr.istic.vv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.github.javaparser.utils.SourceRoot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
public class CCcalc {

    private static PrintWriter csvWriter;
    private static DefaultCategoryDataset dataSet;
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
            dataSet = new DefaultCategoryDataset ();
            Ex5Visitor printer = new Ex5Visitor();
            root.parse("", (localPath, absolutePath, result) -> {
                result.ifSuccessful(unit -> unit.accept(printer, null));
                return SourceRoot.Callback.Result.DONT_SAVE;
            }); 
            csvWriter.close();
            JFreeChart chart = ChartFactory.createBarChart("Report "+file.getName(), "Valeur de la CC","Nombre de methode", dataSet, PlotOrientation.VERTICAL, true, true, false);
            ChartUtils.saveChartAsPNG(new File("cc_chartreport_"+file.getName()+".png"), chart, 1920, 1080);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static PrintWriter getPrintWriter(){
        return csvWriter;
    }

    public static DefaultCategoryDataset getDataSet(){
        return dataSet;
    }
}
