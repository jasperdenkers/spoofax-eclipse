package org.metaborg.spoofax.eclipse;

import org.apache.commons.vfs2.FileSystemManager;
import org.metaborg.core.MetaborgModule;
import org.metaborg.core.editor.IEditorRegistry;
import org.metaborg.core.project.IMavenProjectService;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.core.transform.CompileGoal;
import org.metaborg.core.transform.ITransformerGoal;
import org.metaborg.core.transform.ITransformerResultHandler;
import org.metaborg.core.transform.NamedGoal;
import org.metaborg.spoofax.core.SpoofaxModule;
import org.metaborg.spoofax.eclipse.build.MavenProjectService;
import org.metaborg.spoofax.eclipse.editor.IEclipseEditorRegistry;
import org.metaborg.spoofax.eclipse.editor.IEclipseEditorRegistryInternal;
import org.metaborg.spoofax.eclipse.editor.SpoofaxEditorRegistry;
import org.metaborg.spoofax.eclipse.job.GlobalSchedulingRules;
import org.metaborg.spoofax.eclipse.language.LanguageChangeProcessor;
import org.metaborg.spoofax.eclipse.resource.EclipseFileSystemManagerProvider;
import org.metaborg.spoofax.eclipse.resource.EclipseProjectService;
import org.metaborg.spoofax.eclipse.resource.EclipseResourceService;
import org.metaborg.spoofax.eclipse.resource.IEclipseResourceService;
import org.metaborg.spoofax.eclipse.transform.OpenEditorResultHandler;
import org.spoofax.interpreter.terms.IStrategoTerm;

import com.google.inject.Singleton;
import com.google.inject.multibindings.MapBinder;

public class SpoofaxEclipseModule extends SpoofaxModule {
    public SpoofaxEclipseModule() {
        super(SpoofaxEclipseModule.class.getClassLoader());
    }

    public SpoofaxEclipseModule(ClassLoader resourceClassLoader) {
        super(resourceClassLoader);
    }


    @Override protected void configure() {
        super.configure();

        bind(GlobalSchedulingRules.class).in(Singleton.class);
        bind(LanguageChangeProcessor.class).in(Singleton.class);

        bind(SpoofaxEditorRegistry.class).in(Singleton.class);
        bind(IEditorRegistry.class).to(SpoofaxEditorRegistry.class);
        bind(IEclipseEditorRegistry.class).to(SpoofaxEditorRegistry.class);
        bind(IEclipseEditorRegistryInternal.class).to(SpoofaxEditorRegistry.class);
    }


    /**
     * Overrides {@link MetaborgModule#bindResource()} to provide an Eclipse resource manager and filesystem
     * implementation.
     */
    @Override protected void bindResource() {
        bind(EclipseResourceService.class).in(Singleton.class);
        bind(IResourceService.class).to(EclipseResourceService.class);
        bind(IEclipseResourceService.class).to(EclipseResourceService.class);

        bind(FileSystemManager.class).toProvider(EclipseFileSystemManagerProvider.class).in(Singleton.class);
    }

    /**
     * Overrides {@link MetaborgModule#bindProject()} for non-dummy implementation of project and Maven project service.
     */
    @Override protected void bindProject() {
        bind(IProjectService.class).to(EclipseProjectService.class).in(Singleton.class);
        bind(IMavenProjectService.class).to(MavenProjectService.class).in(Singleton.class);
    }

    /**
     * Overrides {@link SpoofaxModule#bindTransformerResultHandlers()} for transformation handlers that open new
     * editors.
     */
    @Override protected void bindTransformerResultHandlers(
        MapBinder<Class<? extends ITransformerGoal>, ITransformerResultHandler<IStrategoTerm>> binder) {
        bind(OpenEditorResultHandler.class).in(Singleton.class);
        binder.addBinding(NamedGoal.class).to(OpenEditorResultHandler.class);
        binder.addBinding(CompileGoal.class).to(OpenEditorResultHandler.class);
    }
}
