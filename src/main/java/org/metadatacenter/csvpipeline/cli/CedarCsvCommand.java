package org.metadatacenter.csvpipeline.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
@Command(name = "cedar-csv")
public class CedarCsvCommand implements CedarCsvCliCommand {

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
