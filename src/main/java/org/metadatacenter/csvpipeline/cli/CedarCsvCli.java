package org.metadatacenter.csvpipeline.cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
@Component
public class CedarCsvCli {

    private final CommandLine.IFactory factory;

    private final List<CedarCsvCliCommand> commandList;

    public CedarCsvCli(CommandLine.IFactory factory, List<CedarCsvCliCommand> commandList) {
        this.factory = factory;
        this.commandList = commandList;
    }

    public int run(String... args) throws Exception {
        var cli = new CommandLine(new CedarCsvCommand(), factory);
        commandList.forEach(cli::addSubcommand);
        return cli.execute(args);
    }
}
