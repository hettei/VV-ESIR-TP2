package fr.istic.vv;

import java.util.List;
import java.util.ArrayList;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

public class TightClassCohesion extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration decl, Void arg)
    {
        List<MethodDeclaration> method_list = decl.getMethods();
        int method_count = method_list.size();
        int max_edge_count = method_count * (method_count - 1) / 2;
        int direct_edge_count = 0;

        if (max_edge_count == 0) {
            System.out.println(String.format(
                "CLASS %s: no methods, skipping...",
                decl.getName().toString()
            ));
            return; 
        }

        // list of lists of used fields per method
        ArrayList<ArrayList<String>> used_fields =
            new ArrayList<ArrayList<String>>();

        // extract all field accesses from all methods
        for (MethodDeclaration method : method_list)
        {
            used_fields.addFirst(new ArrayList<String>());
            for (Node nd : method.getChildNodes())
            {
                if (nd instanceof FieldAccessExpr) {
                    used_fields.get(0).add(((FieldAccessExpr)nd).getName().toString());
                }
            }
        }
        
        // check for matching fields
        for (int i = 0; i < method_count; i++) 
        {
            for (int j = i + 1; j < method_count; j++) 
            {
                ArrayList<ArrayList<String>> used_fields2 = 
                    new ArrayList<ArrayList<String>>(used_fields);
                
                direct_edge_count++;
                // delete non-matching elements
                used_fields2.get(i).retainAll(used_fields.get(j));
                if (!used_fields2.get(i).isEmpty()) {
                    direct_edge_count++;
                }
            }
        }

        System.out.println(String.format(
            "CLASS %s: %f",
            decl.getName().toString(),
            (float) direct_edge_count / max_edge_count
        ));

        // decl.accept(this, null);
    }

}
