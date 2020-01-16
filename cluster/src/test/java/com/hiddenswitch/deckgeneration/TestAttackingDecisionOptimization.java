package com.hiddenswitch.deckgeneration;

import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.Limits;
import io.jenetics.util.Factory;
import net.demilich.metastone.game.cards.Card;
import net.demilich.metastone.game.cards.CardCatalogue;
import net.demilich.metastone.game.cards.CardSet;
import net.demilich.metastone.game.decks.GameDeck;
import net.demilich.metastone.game.entities.heroes.HeroClass;
import net.demilich.metastone.game.logic.XORShiftRandom;
import org.testng.annotations.Test;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class TestAttackingDecisionOptimization {
	static {
		CardCatalogue.loadCardsFromPackage();
	}
	/**
	 * In a deck with a 30/1 charge minion, tests if the player
	 * will optimize to always attack the opponent's hero instead
	 * of minions
	 */
	@Test
	public void testWillAlwaysHitFace() {
		int GAMES_PER_MATCH = 100;
		int STARTING_HP = 30;
		int POPULATION_SIZE = 3;
		int CARDS_IN_DECK = 30;
		int STABLE_GENERATIONS = 5;

		
		List<GameDeck> basicTournamentDecks = new ArrayList<>();
		List<Card> indexInBitmap = CardCatalogue.getAll()
				.stream()
				.filter(card -> card.isCollectible()
						&& (card.getHeroClass().equals(HeroClass.BLUE) || card.getHeroClass().equals(HeroClass.ANY))
						&& card.getCardSet().equals(CardSet.BASIC)).limit(CARDS_IN_DECK - 1)
				.collect(toList());

		indexInBitmap.add(CardCatalogue.getCardById("minion_charge_30"));

		GameDeck tournamentDeck = new GameDeck(HeroClass.BLUE, indexInBitmap.stream().map(Card::getCardId).collect(toList()));
		basicTournamentDecks.add(tournamentDeck);

		DeckAndDecisionGeneratorContext deckAndDecisionGeneratorContext = new DeckAndDecisionGeneratorContext(indexInBitmap, basicTournamentDecks, new ArrayList<>(), Collections.singletonList(DecisionType.ALWAYS_ATTACK_ENEMY_HERO));
		PlayRandomWithoutSelfDamageBehaviour enemyBehvaiour = new PlayRandomWithoutSelfDamageBehaviour();
		deckAndDecisionGeneratorContext.setEnemyBehaviour(enemyBehvaiour);
		deckAndDecisionGeneratorContext.setGamesPerMatch(GAMES_PER_MATCH);
		deckAndDecisionGeneratorContext.setStartingHp(STARTING_HP);
		deckAndDecisionGeneratorContext.setMaxCardsPerDeck(CARDS_IN_DECK);

		List<Integer> chromosomesToActOn = new ArrayList<>();
		chromosomesToActOn.add(1);

		Factory<Genotype<BitGene>> bitGeneFactory = new DeckAndDecisionGeneFactory(CARDS_IN_DECK, CARDS_IN_DECK, 0, 1);
		Engine<BitGene, Double> engine = Engine.builder((individual) -> deckAndDecisionGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory)
				.mapping(pop -> pop)
				.populationSize(POPULATION_SIZE)
				.alterers(new ActsOnSpecificChromosomesBasicMutator<>(1, chromosomesToActOn))
				.build();

		Genotype<BitGene> result = engine.stream()
				.limit(Limits.bySteadyFitness(STABLE_GENERATIONS))
				.collect(EvolutionResult.toBestGenotype());


		assertTrue(result.getChromosome(1).getGene().booleanValue());
	}

	/**
	 * In a deck with a 30/1 charge minion, tests if the player
	 * will optimize to always attack the opponent's hero with
	 * that 30/1 creature instead of minions, (but variable for
	 * other creatures in the deck)
	 */
	@Test
	public void testWillAlwaysHitFaceWithCharge30() {
		int GAMES_PER_MATCH = 50;
		int STARTING_HP = 30;
		int POPULATION_SIZE = 10;
		int CARDS_IN_DECK = 30;
		int STABLE_GENERATIONS = 10;

		
		List<GameDeck> basicTournamentDecks = new ArrayList<>();
		List<Card> indexInBitmap = CardCatalogue.getAll()
				.stream()
				.filter(card -> card.isCollectible()
						&& (card.getHeroClass().equals(HeroClass.BLUE) || card.getHeroClass().equals(HeroClass.ANY))
						&& card.getCardSet().equals(CardSet.BASIC)).limit(CARDS_IN_DECK - 1)
				.collect(toList());

		indexInBitmap.add(CardCatalogue.getCardById("minion_charge_30"));

		GameDeck tournamentDeck = new GameDeck(HeroClass.BLUE, indexInBitmap.stream().map(card -> card.getCardId()).collect(toList()));
		basicTournamentDecks.add(tournamentDeck);

		DeckAndDecisionGeneratorContext deckAndDecisionGeneratorContext = new DeckAndDecisionGeneratorContext(indexInBitmap, basicTournamentDecks, Collections.singletonList(DecisionType.SOME_MINIONS_DO_NOT_ATTACK_ENEMY_MINION), new ArrayList<>());
		PlayRandomWithoutSelfDamageBehaviour enemyBehvaiour = new PlayRandomWithoutSelfDamageBehaviour();
		deckAndDecisionGeneratorContext.setEnemyBehaviour(enemyBehvaiour);
		deckAndDecisionGeneratorContext.setGamesPerMatch(GAMES_PER_MATCH);
		deckAndDecisionGeneratorContext.setStartingHp(STARTING_HP);
		deckAndDecisionGeneratorContext.setMaxCardsPerDeck(CARDS_IN_DECK);

		List<Integer> chromosomesToActOn = new ArrayList<>();
		chromosomesToActOn.add(1);

		Factory<Genotype<BitGene>> bitGeneFactory = new DeckAndDecisionGeneFactory(CARDS_IN_DECK, CARDS_IN_DECK, 1, 0);
		Engine<BitGene, Double> engine = Engine.builder((individual) -> deckAndDecisionGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory)
				.populationSize(POPULATION_SIZE)
				.alterers(new ActsOnSpecificChromosomesBasicMutator<>(0.2, chromosomesToActOn), new MultiPointCrossoverOnSpecificChromosomes<>(1, 2, chromosomesToActOn))
				.build();

		Genotype<BitGene> result = engine.stream()
				.limit(Limits.bySteadyFitness(STABLE_GENERATIONS))
				.collect(EvolutionResult.toBestGenotype());

		int chargeIndex = indexInBitmap.size() - 1;

		assertTrue(result.getChromosome(1).getGene(chargeIndex).booleanValue());
	}

	/**
	 * With a player behaviour that always attacks the enemy hero instead
	 * of minions, tests that the genetic algorithm evolves a deck to include
	 * a 30/1 charge minion if available
	 */
	@Test
	public void testWillAlwaysFindCharge30IfAlwaysAttackingEnemyHero() {
		int GAMES_PER_MATCH_FOR_DECK_GENERATION = 10;
		int STARTING_HP = 30;
		int POPULATION_SIZE = 10;
		int CARDS_IN_DECK = 20;
		int STABLE_GENERATIONS = 10;
		int NUMBER_OF_DECKS = 10;

		
		List<GameDeck> basicTournamentDecks = new ArrayList<>();
		List<Card> indexInBitmap = TestDeckGeneration.getBitmap();

		indexInBitmap.add(CardCatalogue.getCardById("minion_charge_30"));
		int chargeIndex = indexInBitmap.size() - 1;

		Random random = new XORShiftRandom(101010L);

		// Create random decks for the tournament
		for (int i = 0; i < NUMBER_OF_DECKS; i++) {
			GameDeck tournamentDeck = new GameDeck(HeroClass.BLUE);
			for (int j = 0; j < CARDS_IN_DECK; j++) {
				tournamentDeck.getCards().add(indexInBitmap.get(random.nextInt(indexInBitmap.size())));
			}
			basicTournamentDecks.add(tournamentDeck);
		}

		DeckAndDecisionGeneratorContext deckAndDecisionGeneratorContext = new DeckAndDecisionGeneratorContext(indexInBitmap, basicTournamentDecks, new ArrayList<>(), Collections.singletonList(DecisionType.ALWAYS_ATTACK_ENEMY_HERO));
		PlayRandomWithoutSelfDamageWithDefinedDecisions enemyBehvaiour = new PlayRandomWithoutSelfDamageWithDefinedDecisions(new HashSet<>());
		enemyBehvaiour.setCanEndTurnIfAttackingEnemyHeroIsValid(false);
		deckAndDecisionGeneratorContext.setEnemyBehaviour(enemyBehvaiour);
		deckAndDecisionGeneratorContext.setGamesPerMatch(GAMES_PER_MATCH_FOR_DECK_GENERATION);
		deckAndDecisionGeneratorContext.setStartingHp(STARTING_HP);
		deckAndDecisionGeneratorContext.setMaxCardsPerDeck(CARDS_IN_DECK);

		List<Integer> deckListChromosomes = Collections.singletonList(0);

		Factory<Genotype<BitGene>> deckFactory = new DeckAndDecisionGeneFactory(CARDS_IN_DECK, indexInBitmap.size(), Collections.singletonList(chargeIndex), 0, 1, 1.0);

		Engine<BitGene, Double> deckEngine = Engine.builder((individual) -> deckAndDecisionGeneratorContext.fitness(individual, HeroClass.BLUE), deckFactory)
				.mapping(pop -> pop)
				.populationSize(POPULATION_SIZE)
				.alterers(
						new BitSwapOnSpecificChromosomesMutator<>(1, deckListChromosomes),
						new BitSwapOnSpecificChromosomesMutator<>(1, deckListChromosomes),
						new BitSwapBetweenTwoSequencesOnSpecificChromosomesMutator<>(1, deckListChromosomes))
				.build();

		Phenotype<BitGene, Double> deckResultPhenotype = deckEngine.stream()
				.limit(Limits.bySteadyFitness(STABLE_GENERATIONS))
				.collect(EvolutionResult.toBestPhenotype());

		Genotype<BitGene> deckResult = deckResultPhenotype.getGenotype();
		assertTrue(deckResult.getChromosome(0).getGene(chargeIndex).booleanValue());
		assertTrue(deckResult.getChromosome(1).getGene().booleanValue());
	}

	/**
	 * In a deck with a 1/1 minion that deals 30 damage to the enemy
	 * if it attacks a minion, tests if the genetic algorithm will optimize
	 * the player behavior in order to ensure that this minion does not
	 * always attack the enemy hero (even though it is initialized to do so)
	 * against a deck that utilizes many minions
	 */
	@Test(invocationCount = 10)
	public void testWillNotAlwaysHitFaceWithMinionThatDealsDamageWhenAttackingMinions() {
		int GAMES_PER_MATCH = 100;
		int STARTING_HP = 30;
		int POPULATION_SIZE = 10;
		int CARDS_IN_DECK = 30;
		int STABLE_GENERATIONS = 10;

		Random random = new XORShiftRandom(101010L);
		
		List<GameDeck> basicTournamentDecks = new ArrayList<>();
		List<Card> indexInBitmap = CardCatalogue.getAll()
				.stream()
				.filter(card -> card.isCollectible()
						&& (card.getHeroClass().equals(HeroClass.BLUE) || card.getHeroClass().equals(HeroClass.ANY))
						&& card.getCardSet().equals(CardSet.BASIC)).limit(CARDS_IN_DECK - 2)
				.collect(toList());

		indexInBitmap.add(CardCatalogue.getCardById("secret_explosive_trap"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_deal_damage_when_attacking_minion"));

		GameDeck tournamentDeck = new GameDeck(HeroClass.BLUE);

		for (int i =0; i< CARDS_IN_DECK - 11; i++) {
			tournamentDeck.getCards().add(indexInBitmap.get(random.nextInt(indexInBitmap.size())));
		}
		for (int i =0; i< 10; i++) {
			tournamentDeck.getCards().add(CardCatalogue.getCardById("spell_summon_minion_3_2"));
		}

		tournamentDeck.getCards().add(CardCatalogue.getCardById("minion_deal_damage_when_attacking_minion"));
		basicTournamentDecks.add(tournamentDeck);

		DeckAndDecisionGeneratorContext deckAndDecisionGeneratorContext = new DeckAndDecisionGeneratorContext(indexInBitmap, basicTournamentDecks, Collections.singletonList(DecisionType.SOME_MINIONS_DO_NOT_ATTACK_ENEMY_MINION), new ArrayList<>());
		PlayRandomWithoutSelfDamageBehaviour enemyBehvaiour = new PlayRandomWithoutSelfDamageBehaviour();
		deckAndDecisionGeneratorContext.setEnemyBehaviour(enemyBehvaiour);
		deckAndDecisionGeneratorContext.setGamesPerMatch(GAMES_PER_MATCH);
		deckAndDecisionGeneratorContext.setStartingHp(STARTING_HP);
		deckAndDecisionGeneratorContext.setMaxCardsPerDeck(CARDS_IN_DECK);

		List<Integer> chromosomesToActOn = new ArrayList<>();
		chromosomesToActOn.add(1);

		Factory<Genotype<BitGene>> bitGeneFactory = new DeckAndDecisionGeneFactory(CARDS_IN_DECK, CARDS_IN_DECK, new ArrayList<>(), 1, 0, 1.0);
		Engine<BitGene, Double> engine = Engine.builder((individual) -> deckAndDecisionGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory)
				.mapping(pop -> pop)
				.populationSize(POPULATION_SIZE)
				.alterers(
						new ActsOnSpecificChromosomesBasicMutator<>(0.5, chromosomesToActOn),
						new MultiPointCrossoverOnSpecificChromosomes<>(1, 2, chromosomesToActOn))
				.build();

		Genotype<BitGene> result = engine.stream()
				.limit(Limits.bySteadyFitness(STABLE_GENERATIONS))
				.collect(EvolutionResult.toBestGenotype());

		int chargeIndex = indexInBitmap.size() - 1;

		assertFalse(result.getChromosome(1).getGene(chargeIndex).booleanValue());
	}

}
