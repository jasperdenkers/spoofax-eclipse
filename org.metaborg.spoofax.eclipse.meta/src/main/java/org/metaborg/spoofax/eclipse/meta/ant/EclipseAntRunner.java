package org.metaborg.spoofax.eclipse.meta.ant;

import java.io.File;
import java.net.URL;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.BuildLogger;
import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.runtime.CoreException;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.spoofax.eclipse.processing.Cancel;
import org.metaborg.spoofax.eclipse.util.Nullable;
import org.metaborg.spoofax.meta.core.ant.IAntRunner;
import org.metaborg.util.task.ICancel;


public class EclipseAntRunner implements IAntRunner {
    private final AntRunner runner;


    public EclipseAntRunner(IResourceService resourceService, FileObject antFile, FileObject baseDir,
        Map<String, String> properties, @Nullable URL[] classpaths, @Nullable BuildListener listener) {
        final File localAntFile = resourceService.localFile(antFile);
        final File localBaseDir = resourceService.localPath(baseDir);

        runner = new AntRunner();
        runner.setBuildFileLocation(localAntFile.getPath());
        properties.put("basedir", localBaseDir.getPath());
        runner.addUserProperties(properties);
        runner.setCustomClasspath(classpaths);
        if(listener != null) {
            final String name = listener.getClass().getName();
            if(listener instanceof BuildLogger) {
                runner.addBuildLogger(name);
            } else {
                runner.addBuildListener(name);
            }
        }
    }


    @Override public void execute(String target, @Nullable ICancel cancel) throws MetaborgException {
        runner.setExecutionTargets(new String[] { target });

        try {
            if(cancel instanceof Cancel) {
                final Cancel eclipseCancel = (Cancel) cancel;
                runner.run(eclipseCancel.eclipseMonitor());
            } else {
                runner.run();
            }
        } catch(CoreException e) {
            throw new MetaborgException("Ant runner failed", e);
        }
    }
}
