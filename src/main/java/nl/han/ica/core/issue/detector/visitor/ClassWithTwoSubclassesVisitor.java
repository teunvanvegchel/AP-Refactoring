/**
 * Copyright 2013 
 * 
 * HAN University of Applied Sciences
 * Maik Diepenbroek
 * Wouter Konecny
 * Sjoerd van den Top
 * Teun van Vegchel
 * Niek Versteege
 *
 * See the file MIT-license.txt for copying permission.
 */


package nl.han.ica.core.issue.detector.visitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Niek
 * Date: 12-11-12
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public class ClassWithTwoSubclassesVisitor extends ASTVisitor {

    private Map<Type, List<ASTNode>> subclassesPerSuperClass;

    public ClassWithTwoSubclassesVisitor() {
        subclassesPerSuperClass = new HashMap<Type, List<ASTNode>>();
    }

    @Override
    public boolean visit(TypeDeclaration type) {

        Type superclass = type.getSuperclassType();

        if (superclass != null) {
            ASTNode subClass = type.getParent();

            if (subclassesPerSuperClass.containsKey(superclass)) {
                subclassesPerSuperClass.get(superclass).add(subClass);
            } else {
                ArrayList<ASTNode> subclasses = new ArrayList<ASTNode>();
                subclasses.add(subClass);
                subclassesPerSuperClass.put(superclass, subclasses);
            }
        }

        filterTwoOrMoreSubclasses();

        return super.visit(type);
    }

    private void filterTwoOrMoreSubclasses() {

        Set<Type> types = subclassesPerSuperClass.keySet();
        Set<Type> keysToRemove = new HashSet<Type>();

        for (Type type : types) {
            if (subclassesPerSuperClass.get(type).size() < 2) {
                keysToRemove.add(type);
            }
        }
        for (Type type : keysToRemove) {
            subclassesPerSuperClass.remove(type);
        }
    }

    public Map<Type, List<ASTNode>> getSubclassesPerSuperClass() {
        return subclassesPerSuperClass;
    }

    public void clear() {
        this.subclassesPerSuperClass.clear();
    }
}
