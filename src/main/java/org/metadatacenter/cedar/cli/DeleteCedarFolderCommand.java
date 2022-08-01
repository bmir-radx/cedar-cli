package org.metadatacenter.cedar.cli;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
public class DeleteCedarFolderCommand implements CedarCliCommand {

    @CommandLine.Mixin
    protected CedarFolderCommandMixin cedar;


    @Override
    public Integer call() throws Exception {
        return null;
    }
}
