package org.metaborg.spoofax.core.stratego.primitives;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileType;
import org.metaborg.spoofax.core.language.ILanguage;
import org.metaborg.spoofax.core.language.ILanguageIdentifierService;
import org.metaborg.spoofax.core.resource.IResourceService;
import org.metaborg.spoofax.core.syntax.ISyntaxService;
import org.metaborg.spoofax.core.syntax.ParseException;
import org.metaborg.spoofax.core.syntax.ParseResult;
import org.metaborg.spoofax.core.text.ISourceTextService;
import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.InterpreterException;
import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.library.AbstractPrimitive;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;

import com.google.inject.Inject;

public class ParseFilePrimitive extends AbstractPrimitive {
    private final IResourceService resourceService;
    private final ILanguageIdentifierService languageIdentifierService;
    private final ISourceTextService sourceTextService;
    private final ISyntaxService<IStrategoTerm> syntaxService;


    @Inject public ParseFilePrimitive(IResourceService resourceService,
        ILanguageIdentifierService languageIdentifierService, ISourceTextService sourceTextService,
        ISyntaxService<IStrategoTerm> syntaxService) {
        super("STRSGLR_parse_string", 1, 4);

        this.resourceService = resourceService;
        this.languageIdentifierService = languageIdentifierService;
        this.sourceTextService = sourceTextService;
        this.syntaxService = syntaxService;
    }


    @Override public boolean call(IContext env, Strategy[] strategies, IStrategoTerm[] terms)
        throws InterpreterException {
        if(!Tools.isTermString(terms[0]))
            return false;

        try {
            final String path = Tools.asJavaString(terms[0]);
            final FileObject resource = resourceService.resolve(path);
            if(resource.getType() != FileType.FILE) {
                return false;
            }
            final ILanguage language = languageIdentifierService.identify(resource);
            if(language == null) {
                return false;
            }
            final String text = sourceTextService.text(resource);
            final ParseResult<IStrategoTerm> result = syntaxService.parse(text, resource, language);
            if(result.result == null) {
                return false;
            }
            env.setCurrent(result.result);
        } catch(ParseException | IOException e) {
            throw new InterpreterException("Parsing failed", e);
        }

        return true;
    }
}
