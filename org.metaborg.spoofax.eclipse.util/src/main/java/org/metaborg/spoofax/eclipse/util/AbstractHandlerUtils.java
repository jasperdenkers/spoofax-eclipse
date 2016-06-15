package org.metaborg.spoofax.eclipse.util;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.metaborg.util.iterators.Iterables2;

/**
 * Utility functions for {@link AbstractHandler}.
 */
public final class AbstractHandlerUtils {
    /**
     * Converts selection in given execution event into a structured selection.
     * 
     * @param event
     *            Execution event.
     * @return Structured selection, or null if selection in given execution event is not a structured selection.
     */
    public static @Nullable IStructuredSelection toStructured(ExecutionEvent event) {
        final ISelection selection = HandlerUtil.getCurrentSelection(event);
        if(selection == null) {
            return null;
        }
        return SelectionUtils.toStructured(selection);
    }


    /**
     * Retrieves all resources from selection in given execution event.
     * 
     * @param event
     *            Execution event.
     * @return Selected resources, or null if no structured selection could be found.
     */
    public static @Nullable Iterable<IResource> toResources(ExecutionEvent event) {
        final IStructuredSelection selection = toStructured(event);
        if(selection == null) {
            return null;
        }
        return SelectionUtils.toResources(selection);
    }

    /**
     * Retrieves all files from selection in given execution event.
     * 
     * @param event
     *            Execution event.
     * @return Selected files, or null if no structed selection could be found.
     */
    public static Iterable<IFile> toFiles(ExecutionEvent event) {
        final IStructuredSelection selection = toStructured(event);
        if(selection == null) {
            return null;
        }
        return SelectionUtils.toFiles(selection);
    }

    /**
     * Attempts to retrieve the project from the selection in given execution event.
     * 
     * @param event
     *            Execution event.
     * @return Selected project, or null if it could not be retrieved.
     */
    public static @Nullable IProject toProject(ExecutionEvent event) {
        final IStructuredSelection selection = toStructured(event);
        if(selection == null) {
            return null;
        }
        return SelectionUtils.toProject(selection);
    }
    
    /**
     * Attempts to retrieve the projects from the selection in given execution event.
     * 
     * @param event
     *            Execution event.
     * @return Selected projects, or an empty iterable if it could not be retrieved.
     */
    public static @Nullable Iterable<IProject> toProjects(ExecutionEvent event) {
        final IStructuredSelection selection = toStructured(event);
        if(selection == null) {
            return Iterables2.empty();
        }
        return SelectionUtils.toProjects(selection);
    }
}
