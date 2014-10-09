package net.pferdimanzug.hearthstone.analyzer.game.cards.concrete.tokens.neutral;

import net.pferdimanzug.hearthstone.analyzer.game.cards.MinionCard;
import net.pferdimanzug.hearthstone.analyzer.game.cards.Rarity;
import net.pferdimanzug.hearthstone.analyzer.game.entities.heroes.HeroClass;
import net.pferdimanzug.hearthstone.analyzer.game.entities.minions.Minion;
import net.pferdimanzug.hearthstone.analyzer.game.spells.TransformRandomMinionSpell;
import net.pferdimanzug.hearthstone.analyzer.game.spells.desc.SpellDesc;
import net.pferdimanzug.hearthstone.analyzer.game.spells.trigger.SpellTrigger;
import net.pferdimanzug.hearthstone.analyzer.game.spells.trigger.TurnStartTrigger;
import net.pferdimanzug.hearthstone.analyzer.game.targeting.EntityReference;

public class Poultryizer extends MinionCard {

	public Poultryizer() {
		super("Poultryizer", 0, 3, Rarity.FREE, HeroClass.ANY, 1);
		setDescription("At the start of your turn, transform a random minion into a 1/1 Chicken.");

		setCollectible(false);
	}

	@Override
	public Minion summon() {
		Minion poultryizer = createMinion();
		SpellDesc chickenizeSpell = TransformRandomMinionSpell.create(new Chicken());
		chickenizeSpell.setTarget(EntityReference.ALL_MINIONS);
		SpellTrigger trigger = new SpellTrigger(new TurnStartTrigger(), chickenizeSpell);
		poultryizer.setSpellTrigger(trigger);
		return poultryizer;
	}

	@Override
	public int getTypeId() {
		return 448;
	}
}
