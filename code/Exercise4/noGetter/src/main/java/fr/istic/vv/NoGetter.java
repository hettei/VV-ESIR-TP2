package fr.istic.vv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.github.javaparser.utils.SourceRoot;

public class NoGetter {
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
            csvWriter = new PrintWriter(new FileWriter("NoGetter_report_"+file.getName()+".csv"));
            csvWriter.println("Field_Type;Field;Class");
            Ex4Visitor printer = new Ex4Visitor();
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
