package org.metadatacenter.cedar.cli;

import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
@Component
@Command(name = "cedarcli")
public class CedarCommand implements CedarCliCommand {

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
