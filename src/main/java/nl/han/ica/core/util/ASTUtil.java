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


package nl.han.ica.core.util;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;

/**
 * Helper functionality for common operations involving {@link ASTNode}s.
 */
public final class ASTUtil {

    /**
     * Private constructor to prevent class initialization.
     */
    private ASTUtil() {
        // Private constructor to prevent class initialization.
    }

    /**
     * Find the nearest parent node of a certain type for an {@link ASTNode}.
     *
     * @param klass The type class of the parent node to find. Must be derived from ASTNode.
     * @param node  The node to find a parent node for.
     * @param <T>   The ASTNode derived type of the parent node.
     * @return The found parent, or null if no such parent exists.
     */
    @SuppressWarnings("unchecked")
    public static <T extends ASTNode> T parent(final Class<T> klass, final ASTNode node) {
        ASTNode parent = node;
        do {
            parent = parent.getParent();
            if(parent == null){
                return null;
            }
        } while (parent.getClass() != klass);
        return (T) parent;
    }

    public static int getAnnotationsSize(IBinding binding) {
        if (binding.getAnnotations() != null) {
            return binding.getAnnotations().length;
        }
        return 0;
    }

}
