package net.demilich.metastone.game.spells.desc.filter;

import co.paralleluniverse.fibers.Suspendable;
import net.demilich.metastone.game.GameContext;
import net.demilich.metastone.game.Player;
import net.demilich.metastone.game.entities.Entity;

/**
 * Only returns entities that have even-cost in the hand or an odd base cost.
 */
public final class EvenCostFilter extends ManaCostFilter {

	public EvenCostFilter(EntityFilterDesc desc) {
		super(desc);
	}

	@Override
	@Suspendable
	protected boolean test(GameContext context, Player player, Entity entity, Entity host) {
		return getManaCost(context, player, entity) % 2 == 0;
	}
}
