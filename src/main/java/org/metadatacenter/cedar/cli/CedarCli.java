package org.metadatacenter.cedar.cli;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
@Component
public class CedarCli {

    private final CommandLine.IFactory factory;

    private final List<CedarCliCommand> commandList;

    public CedarCli(CommandLine.IFactory factory, List<CedarCliCommand> commandList) {
        this.factory = factory;
        this.commandList = commandList;
    }

    public int run(String... args) throws Exception {
        var cli = new CommandLine(new CedarCommand(), factory);
        commandList.forEach(cli::addSubcommand);
        cli.addSubcommand(new CommandLine.HelpCommand());
        return cli.execute(args);
    }
}
