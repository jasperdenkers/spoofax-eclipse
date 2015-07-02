package org.metaborg.spoofax.eclipse.language;

import java.util.Set;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.PlatformUI;
import org.metaborg.core.language.ILanguageCache;
import org.metaborg.core.language.ILanguageDiscoveryService;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.core.language.LanguageChange;
import org.metaborg.core.language.dialect.IDialectProcessor;
import org.metaborg.spoofax.eclipse.editor.IEclipseEditorRegistry;
import org.metaborg.spoofax.eclipse.job.GlobalSchedulingRules;
import org.metaborg.spoofax.eclipse.resource.IEclipseResourceService;

import rx.functions.Action1;

import com.google.inject.Inject;

public class LanguageChangeProcessor {
    private final IEclipseResourceService resourceService;
    private final ILanguageService languageService;
    private final ILanguageIdentifierService languageIdentifier;
    private final ILanguageDiscoveryService languageDiscoveryService;
    private final Set<ILanguageCache> languageCaches;
    private final IDialectProcessor dialectProcessor;

    private final IEclipseEditorRegistry editorListener;
    private final GlobalSchedulingRules globalRules;

    private final IWorkspace workspace;
    private final IEditorRegistry editorRegistry;


    @Inject public LanguageChangeProcessor(IEclipseResourceService resourceService, ILanguageService languageService,
        ILanguageIdentifierService languageIdentifierService, ILanguageDiscoveryService languageDiscoveryService,
        Set<ILanguageCache> languageCaches, IDialectProcessor dialectProcessor,
        IEclipseEditorRegistry spoofaxEditorListener, GlobalSchedulingRules globalRules) {
        this.resourceService = resourceService;
        this.languageService = languageService;
        this.languageIdentifier = languageIdentifierService;
        this.languageDiscoveryService = languageDiscoveryService;
        this.languageCaches = languageCaches;
        this.dialectProcessor = dialectProcessor;

        this.editorListener = spoofaxEditorListener;
        this.globalRules = globalRules;

        this.workspace = ResourcesPlugin.getWorkspace();
        this.editorRegistry = PlatformUI.getWorkbench().getEditorRegistry();

        this.languageService.changes().subscribe(new Action1<LanguageChange>() {
            @Override public void call(LanguageChange change) {
                languageChange(change);
            }
        });
    }

    public void discover() {
        final Job job = new DiscoverLanguagesJob(resourceService, languageDiscoveryService, dialectProcessor);
        job.setRule(new MultiRule(new ISchedulingRule[] { workspace.getRoot(), globalRules.startupWriteLock(),
            globalRules.languageServiceLock() }));
        job.schedule();
    }


    private void languageChange(LanguageChange change) {
        final Job job;
        switch(change.kind) {
            case ADD_FIRST:
                job = new LanguageAddedJob(editorListener, editorRegistry, change.newLanguage);
                break;
            case REPLACE_ACTIVE:
            case RELOAD_ACTIVE:
                job =
                    new LanguageReloadedActiveJob(languageCaches, editorListener, editorRegistry, change.oldLanguage,
                        change.newLanguage);
                break;
            case RELOAD:
            case REMOVE:
                job = new LanguageInvalidatedJob(languageCaches, change.oldLanguage);
                break;
            case REMOVE_LAST:
                job =
                    new LanguageRemovedJob(resourceService, languageIdentifier, editorListener, editorRegistry,
                        workspace, change.oldLanguage);
                break;
            default:
                job = null;
                break;
        }

        if(job == null) {
            return;
        }

        job.setRule(new MultiRule(new ISchedulingRule[] { workspace.getRoot(), globalRules.startupReadLock(),
            globalRules.languageServiceLock() }));
        job.schedule();
    }
}
