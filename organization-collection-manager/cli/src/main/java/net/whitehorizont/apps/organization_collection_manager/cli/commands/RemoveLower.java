package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.OrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.RemoveByRevenueCommand;
import net.whitehorizont.apps.organization_collection_manager.core.commands.OrganisationCollectionCommandReceiver.RemovalCriteria;
import net.whitehorizont.apps.organization_collection_manager.lib.DoubleFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.IFromStringBuilder;

@NonNullByDefault
public class RemoveLower implements ICliCommand<OrganisationCollectionCommandReceiver> {

  private static final String DESCRIPTION = "removes organisations from collection, which has annual turnover bellow specified";
  private static final IFromStringBuilder<Double> doubleParser = new DoubleFactory();

  @Override
  public boolean hasArgument() {
    return true;
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
