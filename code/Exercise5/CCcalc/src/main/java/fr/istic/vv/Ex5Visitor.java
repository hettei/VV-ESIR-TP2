package fr.istic.vv;

import java.io.PrintWriter;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class Ex5Visitor extends VoidVisitorWithDefaults<Void> {
    String packageName;

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    @Override
    public void visit(PackageDeclaration declaration, Void arg){
        this.packageName = declaration.getNameAsString();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        PrintWriter writer = CCcalc.getPrintWriter();
        try{
            for (MethodDeclaration method : declaration.getMethods()){
                writer.println(this.packageName+";"+declaration.getNameAsString()+";"+method.getDeclarationAsString()+";"+calculateCC(method));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public int calculateCC(MethodDeclaration method) {
        List<IfStmt> ifStmts = method.findAll(IfStmt.class);
        List<ForStmt> forStmts = method.findAll(ForStmt.class);
        List<WhileStmt> whileStmts = method.findAll(WhileStmt.class);
        List<DoStmt> doStmts = method.findAll(DoStmt.class);
        
        return  ifStmts.size() +
                forStmts.size() +
                whileStmts.size() +
                doStmts.size() +
                1; //There's always at least 1 path through the method
    }
}
