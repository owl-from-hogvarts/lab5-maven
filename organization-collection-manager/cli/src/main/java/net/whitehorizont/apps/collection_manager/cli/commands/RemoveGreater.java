package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.collection_manager.organisation.commands.IOrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.organisation.commands.IOrganisationCollectionCommandReceiver.RemovalCriteria;
import net.whitehorizont.apps.collection_manager.organisation.commands.RemoveByRevenueCommand;
import net.whitehorizont.apps.organization_collection_manager.lib.factories.DoubleFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.factories.IFromStringBuilder;

@NonNullByDefault
public class RemoveGreater implements ICliCommand<IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {

  private static final String DESCRIPTION = "removes organisations from collection, which has annual turnover above specified";
  private static final IFromStringBuilder<Double> doubleParser = new DoubleFactory();

  @Override
  public Optional<String> getArgumentName() {
    return Optional.of("annualTurnover");
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<? extends IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> dependencyManager,
      Stack<String> arguments) throws Exception {
        final String targetTurnoverString = arguments.pop().trim().strip();
        final double targetTurnover = doubleParser.buildFromString(targetTurnoverString);
        final var removeLowerCommand = new RemoveByRevenueCommand(RemovalCriteria.ABOVE, targetTurnover);
        return dependencyManager.getCommandQueue().push(removeLowerCommand);
      }
  
}
