# CEDAR CLI
A Command Line Interface (CLI) for working with CEDAR

## Basic Use

To get a list of available subcommands type

```
java -jar cedar-cli-0.9.0.jar help
```

To get help for a specific subcommand include the name of the subcommand.  For example, to get help for the `search` subcommand type

```
java -jar cedar-cli-0.9.0.jar search help
```

## Environment Variables

### CEDAR_API_KEY

Some subcommands require you to enter a CEDAR API Key.  Your API Key can be found on your CEDAR Profile page.  To save you entering a CEDAR API key for every command that requires it you may set an environment variable called CEDAR_API_KEY.  In a Bash compatible shell you can do this using ```export CEDAR_API_KEY=<YOUR API KEY>```.

### CEDAR_HOME_FOLDER_ID

Some of the subcommands require a folder ID.  You may specify just the UUID part of folder IDs.  Some commands require a parent folder ID.  You may set your home folder ID using the CEDAR_USER_HOME_FOLDER_ID environment variable and this folder will be used as a fallback folder ID if no parent folder ID is specified.
