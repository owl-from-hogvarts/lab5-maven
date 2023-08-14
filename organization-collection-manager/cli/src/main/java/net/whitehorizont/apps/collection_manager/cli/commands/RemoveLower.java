package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.commands.OrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.commands.RemoveByRevenueCommand;
import net.whitehorizont.apps.collection_manager.commands.OrganisationCollectionCommandReceiver.RemovalCriteria;
import net.whitehorizont.apps.organization_collection_manager.lib.factories.DoubleFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.factories.IFromStringBuilder;

@NonNullByDefault
public class RemoveLower implements ICliCommand<OrganisationCollectionCommandReceiver> {

  private static final String DESCRIPTION = "removes organisations from collection, which has annual turnover bellow specified";
  private static final IFromStringBuilder<Double> doubleParser = new DoubleFactory();

  @Override
  public Optional<String> getArgument() {
    return Optional.of("annualTurnover");
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<? extends OrganisationCollectionCommandReceiver> dependencyManager,
      Stack<String> arguments) throws Exception {
        final var collection = dependencyManager.getCollectionReceiver();

        final String targetTurnoverString = arguments.pop().trim().strip();
        final double targetTurnover = doubleParser.buildFromString(targetTurnoverString);
        final var removeLowerCommand = new RemoveByRevenueCommand(collection, RemovalCriteria.BELOW, targetTurnover);
        return dependencyManager.getCommandQueue().push(removeLowerCommand);
      }
  
}
