package org.metadatacenter.cedar.cli;

import picocli.CommandLine.Command;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
@Command(name = "cedar-csv")
public class CedarCommand implements CedarCliCommand {

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
