package liquibase.command;

import liquibase.exception.CommandValidationException;

import java.util.List;

/**
 * Defines a particular step in a command pipeline.
 * When a command is executed, Liquibase will find all the step whose {@link #defineCommandNames()} matches the command,
 * add the dependencies and run the pipeline.
 *
 * @see CommandScope#execute()
 */
public interface CommandStep {

    /**
     * Defines new command names
     * For example, if it is part of `liquibase update`, this should return new String[][]{ new String[] {"update"}}.
     * If it is a part of `liquibase example init`, this should return {"example", "init"}.
     * <p>
     * This is used to determine the available command names.
     * <p>
     * This can return null if this step is not defining a new command but "cross-cutting" existing commands
     */
    String[][] defineCommandNames();

    /**
     * Called by the command pipeline setup to adjust the {@link CommandDefinition} metadata about the overall command.
     * <b>This method should not be called directly. It is called by the overall pipeline logic in the {@link CommandFactory#getCommandDefinition(String...)}.</b>
     */
    void adjustCommandDefinition(CommandDefinition commandDefinition);

    /**
     * Validates that the {@link CommandScope} is correctly set up for this step to run.
     * Any validation in {@link CommandArgumentDefinition#validate(CommandScope)} will be checked previous to this method being called.
     */
    void validate(CommandScope commandScope) throws CommandValidationException;

    /**
     * Performs the business logic.
     * <b>This method should not be called directly. It is called by the overall pipeline logic in the {@link CommandScope#execute()} order.</b>
     */
    void run(CommandResultsBuilder resultsBuilder) throws Exception;

    /**
     * Return a list of configured Classes that this command requires to be passed as a dependency.
     * @return list with the required classes types
     */
    List<Class<?>> requiredDependencies();

    /**
     * Returns a list of all the dependency Classes that this step provides
     * @return list with the provided classes types
     */
    List<Class<?>> providedDependencies();
}
