package org.metaborg.spoofax.eclipse.util;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class NatureUtils {
    /**
     * Adds nature to given project. Does nothing if this nature has already been added to the project.
     * 
     * @param id
     *            Identifier of the nature to add.
     * @param project
     *            Project to add the nature to.
     * @param monitor
     *            Optional progress monitor.
     * @throws CoreException
     *             When {@link IProject#getDescription} throws a CoreException.
     */
    public static void addTo(String id, IProject project, @Nullable IProgressMonitor monitor) throws CoreException {
        final IProjectDescription description = project.getDescription();
        final String[] natures = description.getNatureIds();
        if(natureIndex(id, natures) == -1) {
            final String[] newNatures = ArrayUtils.add(natures, id);
            description.setNatureIds(newNatures);
            project.setDescription(description, monitor);
        }
    }

    /**
     * Removes nature from given project. Does nothing if the nature has not been added to the project.
     * 
     * @param id
     *            Identifier of the nature to remove.
     * @param project
     *            Project to remove the nature from.
     * @param monitor
     *            Optional progress monitor.
     * @throws CoreException
     *             When {@link IProject#getDescription} or {@link IProject#setDescription} throws a CoreException.
     */
    public static void removeFrom(String id, IProject project, @Nullable IProgressMonitor monitor) throws CoreException {
        final IProjectDescription description = project.getDescription();
        final String[] natures = description.getNatureIds();
        final int natureIndex = natureIndex(id, natures);
        if(natureIndex != -1) {
            final String[] newNatures = ArrayUtils.remove(natures, natureIndex);
            description.setNatureIds(newNatures);
            project.setDescription(description, monitor);
        }
    }

    private static int natureIndex(String id, String[] natures) throws CoreException {
        for(int i = 0; i < natures.length; ++i) {
            final String nature = natures[i];
            if(nature.equals(id)) {
                return i;
            }
        }
        return -1;
    }
}
