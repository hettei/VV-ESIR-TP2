package fr.istic.vv;

import java.io.PrintWriter;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class Ex4Visitor extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        List<MethodDeclaration> methodsList = declaration.getMethods();
        PrintWriter writer = NoGetter.getPrintWriter();
        for (FieldDeclaration field : declaration.getFields()){
            if(field.isPrivate() && !hasGetter(field.getVariable(0), methodsList)){
                writer.println(field.getCommonType()+";"+field.getVariable(0)+";"+declaration.getNameAsString());
            }
        }
    }

    private boolean hasGetter(VariableDeclarator variable, List<MethodDeclaration> methodsList) {
        for (MethodDeclaration method : methodsList){
            if(method.getNameAsString().toLowerCase().equals("get"+variable.getNameAsString().toLowerCase())){
                return true;
            }
        }
        return false;
    }
}
