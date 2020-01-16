package com.hiddenswitch.deckgeneration;

import io.jenetics.BitGene;
import io.jenetics.Chromosome;
import io.jenetics.Genotype;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.stat.DoubleMomentStatistics;
import io.jenetics.util.Factory;
import net.demilich.metastone.game.behaviour.PlayRandomBehaviour;
import net.demilich.metastone.game.cards.Card;
import net.demilich.metastone.game.cards.CardCatalogue;
import net.demilich.metastone.game.cards.CardSet;
import net.demilich.metastone.game.decks.GameDeck;
import net.demilich.metastone.game.entities.heroes.HeroClass;
import net.demilich.metastone.game.logic.XORShiftRandom;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.testng.Assert.assertTrue;

public class TestDeckGenerationOptimization {

	/**
	 * Tests if the genetic algorithm can find cards that are objectively better
	 * than other similar cards (in this case, a 30 damage fireball spell with variable mana cost)
	 * We expect the finished deck to be the one-cost (minimal cost) version of the spell.
	 */
	@Test
	public void differentCostBurnSpellsTest() {
		int maxCardsPerDeck = 1;
		int GAMES_PER_MATCH = 18;
		int STARTING_HP = 15;
		int POPULATION_SIZE = 10;
		long NUMBER_OF_GENERATIONS = 10;

		CardCatalogue.loadCardsFromPackage();

		List<Card> indexInBitmap = new ArrayList<>();
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_1"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_2"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_3"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_4"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_5"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_6"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_7"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_8"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_9"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_10"));

		List<GameDeck> basicTournamentDecks = new ArrayList<>();

		// Create unique decks for each of the mana cost versions of the same card
		for (int i = 0; i < indexInBitmap.size(); i++) {
			basicTournamentDecks.add(new GameDeck(HeroClass.BLUE, Collections.singletonList(indexInBitmap.get(i).getCardId())));
		}

		DeckGeneratorContext deckGeneratorContext = new DeckGeneratorContext(indexInBitmap, basicTournamentDecks);
		deckGeneratorContext.setStartingHp(STARTING_HP);
		deckGeneratorContext.setMaxCardsPerDeck(maxCardsPerDeck);
		deckGeneratorContext.setGamesPerMatch(GAMES_PER_MATCH);
		deckGeneratorContext.setEnemyBehaviour(new PlayRandomWithoutSelfDamageBehaviour());
		deckGeneratorContext.setPlayerBehaviour(new PlayRandomWithoutSelfDamageBehaviour());

		List<Integer> invalidCards = new ArrayList<>();
		invalidCards.add(0);
		Factory<Genotype<BitGene>> bitGeneFactory = new DeckGeneFactory(maxCardsPerDeck, indexInBitmap.size(), invalidCards);
		Engine<BitGene, Double> engine = Engine.builder((individual) -> deckGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory)
				.populationSize(POPULATION_SIZE)
				.alterers(new BitSwapMutator<>(1))
				.build();

		EvolutionStatistics<Double, DoubleMomentStatistics> statistics = EvolutionStatistics.ofNumber();

		Phenotype<BitGene, Double> result = engine.stream()
				.limit(NUMBER_OF_GENERATIONS)
				.peek(statistics)
				.collect(EvolutionResult.toBestPhenotype());

		int bestCardIndex = 0;
		assertTrue(result.getGenotype().getChromosome().getGene(bestCardIndex).booleanValue());
	}

	/**
	 * We will now test if our bitSwapToAddDuplicate helps us find the
	 * deck that contains two copies of the minion_win card faster than just
	 * using bitSwapMutator
	 */
	@Test
	public void duplicateMutatorFindsOneCardWinComboFasterTest() {
		int maxCardsPerDeck = 2;
		int GAMES_PER_MATCH = 18;
		int STARTING_HP = 30;
		int POPULATION_SIZE = 20;

		Random random = new XORShiftRandom(101010L);

		CardCatalogue.loadCardsFromPackage();

		List<Card> indexInBitmap = new ArrayList<>();
		List<Card> halfIndexInBitmap = new ArrayList<>();
		halfIndexInBitmap.add(CardCatalogue.getCardById("minion_win"));
		halfIndexInBitmap.addAll(CardCatalogue.getAll()
				.stream()
				.filter(card -> card.isCollectible()
						&& (card.getHeroClass().equals(HeroClass.BLUE) || card.getHeroClass().equals(HeroClass.ANY))
						&& card.getCardSet().equals(CardSet.BASIC)).limit(9)
				.collect(toList()));
		indexInBitmap.addAll(halfIndexInBitmap);
		indexInBitmap.addAll(halfIndexInBitmap);

		int bestCardIndex1 = 0;
		int bestCardIndex2 = indexInBitmap.size() / 2;

		List<GameDeck> basicTournamentDecks = new ArrayList<>();

		// Create unique decks for each of the mana cost versions of the same card
		for (int i = 0; i < 5; i += 2) {
			List<String> deckList = new ArrayList<>(maxCardsPerDeck);
			for (int j = 0; j < maxCardsPerDeck; j++) {
				int nextCardIndex = random.nextInt(indexInBitmap.size());
				while (nextCardIndex != bestCardIndex1 && nextCardIndex != bestCardIndex2) {
					nextCardIndex = random.nextInt(indexInBitmap.size());
				}
				deckList.add(indexInBitmap.get(i).getCardId());
			}
			basicTournamentDecks.add(new GameDeck(HeroClass.BLUE, deckList));
		}

		DeckGeneratorContext deckGeneratorContext = new DeckGeneratorContext(indexInBitmap, basicTournamentDecks);
		deckGeneratorContext.setStartingHp(STARTING_HP);
		deckGeneratorContext.setMaxCardsPerDeck(maxCardsPerDeck);
		deckGeneratorContext.setGamesPerMatch(GAMES_PER_MATCH);
		deckGeneratorContext.setEnemyBehaviour(new PlayRandomWithoutSelfDamageBehaviour());
		deckGeneratorContext.setPlayerBehaviour(new PlayRandomWithoutSelfDamageBehaviour());

		List<Integer> invalidCards = new ArrayList<>();
		invalidCards.add(0);
		invalidCards.add(indexInBitmap.size() / 2);
		Factory<Genotype<BitGene>> bitGeneFactory1 = new DeckGeneFactory(maxCardsPerDeck, indexInBitmap.size(), invalidCards);
		Engine<BitGene, Double> goodEngine = Engine.builder((individual) -> deckGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory1)
				.populationSize(POPULATION_SIZE)
				.alterers(new BitSwapMutator<>(1), new BitSwapToChangeCardCountMutator<>(1, indexInBitmap))
				.build();

		Factory<Genotype<BitGene>> bitGeneFactory2 = new DeckGeneFactory(maxCardsPerDeck, indexInBitmap.size(), invalidCards);
		Engine<BitGene, Double> badEngine = Engine.builder((individual) -> deckGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory2)
				.populationSize(POPULATION_SIZE)
				.alterers(new BitSwapMutator<>(1), new BitSwapMutator<>(1))
				.build();

		Stream goodResult = goodEngine.stream()
				.limit(r -> {
					Chromosome<BitGene> bestChromosome = r.getBestPhenotype().getGenotype().getChromosome();
					return !(bestChromosome.getGene(bestCardIndex1).booleanValue() && bestChromosome.getGene(bestCardIndex2).booleanValue());
				});
		long goodCount = goodResult.count();


		Stream badResult = badEngine.stream()
				.limit(r -> {
					Chromosome<BitGene> bestChromosome = r.getBestPhenotype().getGenotype().getChromosome();
					return !(bestChromosome.getGene(bestCardIndex1).booleanValue() && bestChromosome.getGene(bestCardIndex2).booleanValue());
				})
				.limit(goodCount + 1);
		long badCount = badResult.count();

		assertTrue(goodCount <= badCount);
	}

	/**
	 * We will now test if our bitSwapToAddDuplicate helps us find the
	 * deck that contains two copies of spell_shock_1 faster than without it
	 */
	@Test
	public void duplicateMutatorFindsTwoCardWinComboFasterTest() {
		int maxCardsPerDeck = 4;
		int GAMES_PER_MATCH = 18;
		int STARTING_HP = 30;
		int POPULATION_SIZE = 20;

		Random random = new XORShiftRandom(101010L);

		CardCatalogue.loadCardsFromPackage();

		List<Card> indexInBitmap = new ArrayList<>();
		List<Card> halfIndexInBitmap = new ArrayList<>();
		halfIndexInBitmap.add(CardCatalogue.getCardById("minion_win"));
		halfIndexInBitmap.add(CardCatalogue.getCardById("minion_win_2"));
		halfIndexInBitmap.addAll(CardCatalogue.getAll()
				.stream()
				.filter(card -> card.isCollectible()
						&& (card.getHeroClass().equals(HeroClass.BLUE) || card.getHeroClass().equals(HeroClass.ANY))
						&& card.getCardSet().equals(CardSet.BASIC)).limit(7)
				.collect(toList()));
		indexInBitmap.addAll(halfIndexInBitmap);
		indexInBitmap.addAll(halfIndexInBitmap);

		int bestCardIndex1 = 0;
		int bestCardIndex2 = indexInBitmap.size() / 2;
		int bestCardIndex3 = 1;
		int bestCardIndex4 = 1 + (indexInBitmap.size() / 2);

		List<GameDeck> basicTournamentDecks = new ArrayList<>();

		// Create unique decks for each of the mana cost versions of the same card
		for (int i = 0; i < 3; i += 2) {
			List<String> deckList = new ArrayList<>(maxCardsPerDeck);
			for (int j = 0; j < maxCardsPerDeck; j++) {
				int nextCardIndex = random.nextInt(indexInBitmap.size());
				while (nextCardIndex != bestCardIndex1 && nextCardIndex != bestCardIndex2) {
					nextCardIndex = random.nextInt(indexInBitmap.size());
				}
				deckList.add(indexInBitmap.get(i).getCardId());
			}
			basicTournamentDecks.add(new GameDeck(HeroClass.BLUE, deckList));
		}

		DeckGeneratorContext deckGeneratorContext = new DeckGeneratorContext(indexInBitmap, basicTournamentDecks);
		deckGeneratorContext.setStartingHp(STARTING_HP);
		deckGeneratorContext.setMaxCardsPerDeck(maxCardsPerDeck);
		deckGeneratorContext.setGamesPerMatch(GAMES_PER_MATCH);
		deckGeneratorContext.setEnemyBehaviour(new PlayRandomWithoutSelfDamageBehaviour());
		deckGeneratorContext.setPlayerBehaviour(new PlayRandomWithoutSelfDamageBehaviour());

		List<Integer> invalidCards = new ArrayList<>();
		invalidCards.add(0);
		invalidCards.add(indexInBitmap.size() / 2);
		Factory<Genotype<BitGene>> bitGeneFactory1 = new DeckGeneFactory(maxCardsPerDeck, indexInBitmap.size(), invalidCards);
		Engine<BitGene, Double> goodEngine = Engine.builder((individual) -> deckGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory1)
				.populationSize(POPULATION_SIZE)
				.alterers(new BitSwapMutator<>(1), new BitSwapToChangeCardCountMutator<>(1, indexInBitmap))
				.build();

		Factory<Genotype<BitGene>> bitGeneFactory2 = new DeckGeneFactory(maxCardsPerDeck, indexInBitmap.size(), invalidCards);
		Engine<BitGene, Double> badEngine = Engine.builder((individual) -> deckGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory2)
				.populationSize(POPULATION_SIZE)
				.alterers(new BitSwapMutator<>(1), new BitSwapMutator<>(1))
				.build();

		Stream goodResult = goodEngine.stream()
				.limit(r -> {
					Chromosome<BitGene> bestChromosome = r.getBestPhenotype().getGenotype().getChromosome();
					return !(bestChromosome.getGene(bestCardIndex1).booleanValue() && bestChromosome.getGene(bestCardIndex2).booleanValue()
							&& bestChromosome.getGene(bestCardIndex3).booleanValue() && bestChromosome.getGene(bestCardIndex4).booleanValue());
				});
		long goodCount = goodResult.count();


		Stream badResult = badEngine.stream()
				.limit(r -> {
					Chromosome<BitGene> bestChromosome = r.getBestPhenotype().getGenotype().getChromosome();
					return !(bestChromosome.getGene(bestCardIndex1).booleanValue() && bestChromosome.getGene(bestCardIndex2).booleanValue()
							&& bestChromosome.getGene(bestCardIndex3).booleanValue() && bestChromosome.getGene(bestCardIndex4).booleanValue());
				})
				.limit(goodCount + 1);
		long badCount = badResult.count();

		assertTrue(goodCount <= badCount);
	}

	/**
	 * We will now test if our bitSwapToAddDuplicate helps us find the
	 * deck that contains two copies of spell_shock_1 faster than without it
	 */
	@Test
	public void differentCostBurnSpellsTestWithDuplicates() {
		int maxCardsPerDeck = 2;
		int GAMES_PER_MATCH = 18;
		int STARTING_HP = 30;
		int POPULATION_SIZE = 20;

		CardCatalogue.loadCardsFromPackage();

		List<Card> indexInBitmap = new ArrayList<>();
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_1"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_2"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_3"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_4"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_5"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_6"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_7"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_8"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_9"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_10"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_1"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_2"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_3"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_4"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_5"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_6"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_7"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_8"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_9"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_shock_10"));

		List<GameDeck> basicTournamentDecks = new ArrayList<>();

		// Create unique decks for each of the mana cost versions of the same card
		for (int i = 0; i < indexInBitmap.size() / 2; i += 2) {
			List<String> deckList = new ArrayList<>(maxCardsPerDeck);
			for (int j = 0; j < maxCardsPerDeck; j++) {
				deckList.add(indexInBitmap.get(i).getCardId());
			}
			basicTournamentDecks.add(new GameDeck(HeroClass.BLUE, deckList));
		}

		DeckGeneratorContext deckGeneratorContext = new DeckGeneratorContext(indexInBitmap, basicTournamentDecks);
		deckGeneratorContext.setStartingHp(STARTING_HP);
		deckGeneratorContext.setMaxCardsPerDeck(maxCardsPerDeck);
		deckGeneratorContext.setGamesPerMatch(GAMES_PER_MATCH);
		deckGeneratorContext.setEnemyBehaviour(new PlayRandomWithoutSelfDamageBehaviour());
		deckGeneratorContext.setPlayerBehaviour(new PlayRandomWithoutSelfDamageBehaviour());

		List<Integer> invalidCards = new ArrayList<>();
		invalidCards.add(0);
		invalidCards.add(indexInBitmap.size() / 2);
		Factory<Genotype<BitGene>> bitGeneFactory1 = new DeckGeneFactory(maxCardsPerDeck, indexInBitmap.size(), invalidCards);
		Engine<BitGene, Double> goodEngine = Engine.builder((individual) -> deckGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory1)
				.populationSize(POPULATION_SIZE)
				.alterers(new BitSwapMutator<>(1), new BitSwapToChangeCardCountMutator<>(1, indexInBitmap))
				.build();

		Factory<Genotype<BitGene>> bitGeneFactory2 = new DeckGeneFactory(maxCardsPerDeck, indexInBitmap.size(), invalidCards);
		Engine<BitGene, Double> badEngine = Engine.builder((individual) -> deckGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory2)
				.populationSize(POPULATION_SIZE)
				.alterers(new BitSwapMutator<>(1), new BitSwapMutator<>(1))
				.build();

		int bestCardIndex1 = 0;
		int bestCardIndex2 = indexInBitmap.size() / 2;

		Stream goodResult = goodEngine.stream()
				.limit(r -> {
					Chromosome<BitGene> bestChromosome = r.getBestPhenotype().getGenotype().getChromosome();
					return !(bestChromosome.getGene(bestCardIndex1).booleanValue() && bestChromosome.getGene(bestCardIndex2).booleanValue());
				});
		long goodCount = goodResult.count();


		Stream badResult = badEngine.stream()
				.limit(r -> {
					Chromosome<BitGene> bestChromosome = r.getBestPhenotype().getGenotype().getChromosome();
					return !(bestChromosome.getGene(bestCardIndex1).booleanValue() && bestChromosome.getGene(bestCardIndex2).booleanValue());
				})
				.limit(goodCount + 1);
		long badCount = badResult.count();

		assertTrue(goodCount <= badCount);
	}

	/**
	 * Tests if the genetic algorithm can find cards that are objectively better
	 * than other similar cards (in this case, a 4 cost fireball spell with variable damage cost)
	 * We expect the finished deck to be the maximal damage version of the spell.
	 */
	@Test
	public void differentDamageBurnSpellsTest() {
		int maxCardsPerDeck = 1;
		int GAMES_PER_MATCH = 18;
		int STARTING_HP = 30;
		int POPULATION_SIZE = 10;
		long NUMBER_OF_GENERATIONS = 10;

		CardCatalogue.loadCardsFromPackage();

		List<Card> indexInBitmap = new ArrayList<>();
		indexInBitmap.add(CardCatalogue.getCardById("spell_fireball_1"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_fireball_3"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_fireball_6"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_fireball_10"));

		List<GameDeck> basicTournamentDecks = new ArrayList<>();

		// Create unique decks for each of the different damage versions of the card
		for (int i = 0; i < indexInBitmap.size(); i++) {
			basicTournamentDecks.add(new GameDeck(HeroClass.BLUE, Collections.singletonList(indexInBitmap.get(i).getCardId())));
		}

		DeckGeneratorContext deckGeneratorContext = new DeckGeneratorContext(indexInBitmap, basicTournamentDecks);
		deckGeneratorContext.setStartingHp(STARTING_HP);
		deckGeneratorContext.setMaxCardsPerDeck(maxCardsPerDeck);
		deckGeneratorContext.setGamesPerMatch(GAMES_PER_MATCH);
		deckGeneratorContext.setEnemyBehaviour(new PlayRandomWithoutSelfDamageBehaviour());
		deckGeneratorContext.setPlayerBehaviour(new PlayRandomWithoutSelfDamageBehaviour());

		Factory<Genotype<BitGene>> bitGeneFactory = new DeckGeneFactory(maxCardsPerDeck, indexInBitmap.size());
		Engine<BitGene, Double> engine = Engine.builder((individual) -> deckGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory)
				.populationSize(POPULATION_SIZE)
				.alterers(new BitSwapMutator<>(1), new BitSwapBetweenTwoSequencesMutator<>(1))
				.build();

		EvolutionStatistics<Double, DoubleMomentStatistics> statistics = EvolutionStatistics.ofNumber();

		Phenotype<BitGene, Double> result = engine.stream()
				.limit(NUMBER_OF_GENERATIONS)
				.peek(statistics)
				.collect(EvolutionResult.toBestPhenotype());

		int bestCardIndex = indexInBitmap.size() - 1;
		assertTrue(result.getGenotype().getChromosome().getGene(bestCardIndex).booleanValue());
	}

	// With completely random play, we expect to have not nearly as good results
	@Test
	public void differentDamageBurnSpellsTestWithRandomPlay() {
		int maxCardsPerDeck = 1;
		int GAMES_PER_MATCH = 18;
		int STARTING_HP = 30;
		int POPULATION_SIZE = 10;
		int TRIALS = 10;
		long NUMBER_OF_GENERATIONS = 5;

		CardCatalogue.loadCardsFromPackage();

		List<Card> indexInBitmap = new ArrayList<>();
		indexInBitmap.add(CardCatalogue.getCardById("spell_fireball_1"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_fireball_3"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_fireball_6"));
		indexInBitmap.add(CardCatalogue.getCardById("spell_fireball_10"));

		List<GameDeck> basicTournamentDecks = new ArrayList<>();

		for (int i = 0; i < indexInBitmap.size(); i++) {
			basicTournamentDecks.add(new GameDeck(HeroClass.BLUE, Collections.singletonList(indexInBitmap.get(i).getCardId())));
		}

		DeckGeneratorContext deckGeneratorContext = new DeckGeneratorContext(indexInBitmap, basicTournamentDecks);
		deckGeneratorContext.setStartingHp(STARTING_HP);
		deckGeneratorContext.setMaxCardsPerDeck(maxCardsPerDeck);
		deckGeneratorContext.setGamesPerMatch(GAMES_PER_MATCH);
		deckGeneratorContext.setEnemyBehaviour(new PlayRandomBehaviour());
		deckGeneratorContext.setPlayerBehaviour(new PlayRandomBehaviour());

		// Keep track of how many times we succeed (presumed to be by pure chance)
		int succeeds = 0;
		for (int i = 0; i < 10; i++) {
			Factory<Genotype<BitGene>> bitGeneFactory = new DeckGeneFactory(maxCardsPerDeck, indexInBitmap.size());
			Engine<BitGene, Double> engine = Engine.builder((individual) -> deckGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory)
					.populationSize(POPULATION_SIZE)
					.alterers(new BitSwapMutator<>(1), new BitSwapBetweenTwoSequencesMutator<>(1))
					.build();

			EvolutionStatistics<Double, DoubleMomentStatistics> statistics = EvolutionStatistics.ofNumber();

			Phenotype<BitGene, Double> result = engine.stream()
					.limit(NUMBER_OF_GENERATIONS)
					.peek(statistics)
					.collect(EvolutionResult.toBestPhenotype());

			int bestCardIndex = indexInBitmap.size() - 1;
			if (result.getGenotype().getChromosome().getGene(bestCardIndex).booleanValue()) {
				succeeds++;
			}
		}

		// Should not succeed more than a couple times
		assertTrue(succeeds <= 2 * TRIALS / indexInBitmap.size());
	}

	/**
	 * Tests if the genetic algorithm can find cards that are objectively better
	 * than other similar cards (in this case, a 1 cost creature with variable stats)
	 */
	@Test
	public void differentStatMinionsTest() {
		int maxCardsPerDeck = 2;
		int GAMES_PER_MATCH = 18;
		int STARTING_HP = 30;
		int POPULATION_SIZE = 10;
		long NUMBER_OF_GENERATIONS = 10;

		CardCatalogue.loadCardsFromPackage();

		List<Card> indexInBitmap = new ArrayList<>();
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_1"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_2"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_3"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_4"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_5"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_6"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_7"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_8"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_9"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_10"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_1"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_2"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_3"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_4"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_5"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_6"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_7"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_8"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_9"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_10"));

		List<GameDeck> basicTournamentDecks = new ArrayList<>();

		// Create unique decks for each of the different stat versions of the card
		for (int i = 0; i < indexInBitmap.size() / 2; i += 3) {
			List<String> deckCardIds = new ArrayList<>();
			deckCardIds.add(indexInBitmap.get(i).getCardId());
			deckCardIds.add(indexInBitmap.get(i).getCardId());
			basicTournamentDecks.add(new GameDeck(HeroClass.BLUE, deckCardIds));

		}

		int bestCardIndex1 = (indexInBitmap.size() / 2) - 1;
		int bestCardIndex2 = indexInBitmap.size() - 1;

		List<Integer> invalidCards = new ArrayList<>();
		invalidCards.add(bestCardIndex1);
		invalidCards.add(bestCardIndex2);

		DeckGeneratorContext deckGeneratorContext = new DeckGeneratorContext(indexInBitmap, basicTournamentDecks);
		deckGeneratorContext.setStartingHp(STARTING_HP);
		deckGeneratorContext.setMaxCardsPerDeck(maxCardsPerDeck);
		deckGeneratorContext.setGamesPerMatch(GAMES_PER_MATCH);
		deckGeneratorContext.setEnemyBehaviour(new PlayRandomWithoutSelfDamageBehaviour());
		deckGeneratorContext.setPlayerBehaviour(new PlayRandomWithoutSelfDamageBehaviour());

		Factory<Genotype<BitGene>> bitGeneFactory = new DeckGeneFactory(maxCardsPerDeck, indexInBitmap.size(), invalidCards);
		Engine<BitGene, Double> engine = Engine.builder((individual) -> deckGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory)
				.populationSize(POPULATION_SIZE)
				.alterers(new BitSwapMutator<>(1), new BitSwapToChangeCardCountMutator<>(1, indexInBitmap))
				.build();

		EvolutionStatistics<Double, DoubleMomentStatistics> statistics = EvolutionStatistics.ofNumber();

		Phenotype<BitGene, Double> result = engine.stream()
				.limit(NUMBER_OF_GENERATIONS)
				.peek(statistics)
				.collect(EvolutionResult.toBestPhenotype());

		assertTrue(result.getGenotype().getChromosome().getGene(bestCardIndex1).booleanValue());
		assertTrue(result.getGenotype().getChromosome().getGene(bestCardIndex2).booleanValue());
	}

	/**
	 * Tests if the genetic algorithm can find cards that are objectively better
	 * than other similar cards
	 * In this case, we want to see if it will find the 10/10 creature with
	 * card draw as opposed to a 10/10 without card draw
	 */
	@Test
	public void differentStatMinionsTestWithCardDraw() {
		int maxCardsPerDeck = 8;
		int GAMES_PER_MATCH = 18;
		int STARTING_HP = 30;
		int POPULATION_SIZE = 15;
		long NUMBER_OF_GENERATIONS = 20;

		Random random = new XORShiftRandom(101010L);

		CardCatalogue.loadCardsFromPackage();

		List<Card> indexInBitmap = new ArrayList<>();
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_1"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_2"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_3"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_4"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_5"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_6"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_7"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_8"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_9"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_10"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_with_draw_1"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_with_draw_2"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_with_draw_3"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_with_draw_4"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_with_draw_5"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_with_draw_6"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_with_draw_7"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_with_draw_8"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_with_draw_9"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_with_draw_10"));

		List<GameDeck> basicTournamentDecks = new ArrayList<>();

		// Create unique decks for each of the different stat versions of the card
		for (int i = 0; i < 3; i++) {
			List<String> deckCardIds = new ArrayList<>();
			for (int j = 0; j < maxCardsPerDeck; j++) {
				deckCardIds.add(indexInBitmap.get(random.nextInt(indexInBitmap.size())).getCardId());
			}
			basicTournamentDecks.add(new GameDeck(HeroClass.BLUE, deckCardIds));
		}

		int bestCardIndex1 = indexInBitmap.size() - 1;

		List<Integer> invalidCards = new ArrayList<>();
		invalidCards.add(bestCardIndex1);

		DeckGeneratorContext deckGeneratorContext = new DeckGeneratorContext(indexInBitmap, basicTournamentDecks);
		deckGeneratorContext.setFatigueEnabled(false);
		deckGeneratorContext.setStartingHp(STARTING_HP);
		deckGeneratorContext.setMaxCardsPerDeck(maxCardsPerDeck);
		deckGeneratorContext.setGamesPerMatch(GAMES_PER_MATCH);
		deckGeneratorContext.setEnemyBehaviour(new PlayRandomWithoutSelfDamageBehaviour());
		deckGeneratorContext.setPlayerBehaviour(new PlayRandomWithoutSelfDamageBehaviour());

		Factory<Genotype<BitGene>> bitGeneFactory = new DeckGeneFactory(maxCardsPerDeck, indexInBitmap.size(), invalidCards);
		Engine<BitGene, Double> engine = Engine.builder((individual) -> deckGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory)
				.populationSize(POPULATION_SIZE)
				.alterers(new BitSwapMutator<>(1))
				.build();

		EvolutionStatistics<Double, DoubleMomentStatistics> statistics = EvolutionStatistics.ofNumber();

		Phenotype<BitGene, Double> result = engine.stream()
				.limit(NUMBER_OF_GENERATIONS)
				.peek(statistics)
				.collect(EvolutionResult.toBestPhenotype());

		assertTrue(result.getGenotype().getChromosome().getGene(bestCardIndex1).booleanValue());
	}

	/**
	 * Currently, damage spells can target our own minions, so even though minion_with_damage_3
	 * really is the best card, coming bundled with a fireball as a battlecry, it is not necessarily added to the deck
	 * Instead, the vanilla version might be added
	 */
	@Ignore
	@Test
	public void damageOwnMinionsTest() {
		int MAX_CARDS_PER_DECK = 5;
		int GAMES_PER_MATCH = 18;
		int STARTING_HP = 15;
		int POPULATION_SIZE = 20;
		int NUMBER_OF_GENERATIONS = 10;

		XORShiftRandom random = new XORShiftRandom(101010L);

		CardCatalogue.loadCardsFromPackage();
		List<GameDeck> basicTournamentDecks = new ArrayList<>();
		List<Card> indexInBitmap = CardCatalogue.getAll()
				.stream()
				.filter(card -> card.isCollectible()
						&& (card.getHeroClass().equals(HeroClass.BLUE) || card.getHeroClass().equals(HeroClass.ANY))
						&& card.getCardSet().equals(CardSet.BASIC))
				.collect(toList());

		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_3"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_with_damage_3"));

		// Create random decks for the tournament
		for (int i = 0; i < 5; i++) {
			GameDeck tournamentDeck = new GameDeck(HeroClass.BLUE);
			for (int j = 0; j < MAX_CARDS_PER_DECK; j++) {
				tournamentDeck.getCards().add(indexInBitmap.get(random.nextInt(indexInBitmap.size())));
			}
			basicTournamentDecks.add(tournamentDeck);
		}

		// Set up our tournament playing environment
		DeckGeneratorContext deckGeneratorContext = new DeckGeneratorContext(indexInBitmap, basicTournamentDecks);
		deckGeneratorContext.setStartingHp(STARTING_HP);
		deckGeneratorContext.setMaxCardsPerDeck(MAX_CARDS_PER_DECK);
		deckGeneratorContext.setGamesPerMatch(GAMES_PER_MATCH);
		deckGeneratorContext.setEnemyBehaviour(new PlayRandomWithoutSelfDamageBehaviour());
		deckGeneratorContext.setPlayerBehaviour(new PlayRandomWithoutSelfDamageBehaviour());

		int bestCardIndex2 = indexInBitmap.size() - 2;
		List<Integer> invalidCards = new ArrayList<>(0);
		invalidCards.add(indexInBitmap.size() - 1);

		Factory<Genotype<BitGene>> bitGeneFactory = new DeckGeneFactory(MAX_CARDS_PER_DECK, indexInBitmap.size(), invalidCards);
		Engine<BitGene, Double> engine = Engine.builder((individual) -> deckGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory)
				.populationSize(POPULATION_SIZE)
				.alterers(new BitSwapMutator<>(.9), new BitSwapMutator<>(.9), new BitSwapBetweenTwoSequencesMutator<>(.9))
				.build();

		Genotype<BitGene> result = engine.stream()
				.limit(NUMBER_OF_GENERATIONS)
				.collect(EvolutionResult.toBestGenotype());

		assertTrue(result.getChromosome().getGene(bestCardIndex2).getAllele());
	}

	/**
	 * This test shows that forbidding damage abilities from targeting player minions
	 * guarantees that the desired minion is used in the deck
	 */
	@Test
	public void damageOwnMinionsForbiddenTest() {
		int MAX_CARDS_PER_DECK = 1;
		int GAMES_PER_MATCH = 18;
		int STARTING_HP = 15;
		int POPULATION_SIZE = 20;
		int NUMBER_OF_GENERATIONS = 10;

		XORShiftRandom random = new XORShiftRandom(101010L);

		CardCatalogue.loadCardsFromPackage();
		List<GameDeck> basicTournamentDecks = new ArrayList<>();
		List<Card> indexInBitmap = CardCatalogue.getAll()
				.stream()
				.filter(card -> card.isCollectible()
						&& (card.getHeroClass().equals(HeroClass.BLUE) || card.getHeroClass().equals(HeroClass.ANY))
						&& card.getCardSet().equals(CardSet.BASIC))
				.collect(toList());

		indexInBitmap.add(CardCatalogue.getCardById("minion_stat_3"));
		indexInBitmap.add(CardCatalogue.getCardById("minion_with_damage_3"));

		// Create random decks for the tournament
		for (int i = 0; i < 5; i++) {
			GameDeck tournamentDeck = new GameDeck(HeroClass.BLUE);
			for (int j = 0; j < MAX_CARDS_PER_DECK; j++) {
				tournamentDeck.getCards().add(indexInBitmap.get(random.nextInt(indexInBitmap.size())));
			}
			basicTournamentDecks.add(tournamentDeck);
		}

		// Set up our tournament playing environment
		DeckGeneratorContext deckGeneratorContext = new DeckGeneratorContext(indexInBitmap, basicTournamentDecks);
		deckGeneratorContext.setStartingHp(STARTING_HP);
		deckGeneratorContext.setMaxCardsPerDeck(MAX_CARDS_PER_DECK);
		deckGeneratorContext.setGamesPerMatch(GAMES_PER_MATCH);

		PlayRandomWithoutSelfDamageBehaviour enemyBehavior = new PlayRandomWithoutSelfDamageBehaviour();
		enemyBehavior.ownMinionTargetingIsEnabled(false);
		deckGeneratorContext.setEnemyBehaviour(enemyBehavior);

		PlayRandomWithoutSelfDamageBehaviour playerBehaviour = new PlayRandomWithoutSelfDamageBehaviour();
		playerBehaviour.ownMinionTargetingIsEnabled(false);
		deckGeneratorContext.setPlayerBehaviour(playerBehaviour);

		int bestCardIndex1 = indexInBitmap.size() - 1;
		List<Integer> invalidCards = new ArrayList<>(0);
		invalidCards.add(indexInBitmap.size() - 1);

		Factory<Genotype<BitGene>> bitGeneFactory = new DeckGeneFactory(MAX_CARDS_PER_DECK, indexInBitmap.size(), invalidCards);
		Engine<BitGene, Double> engine = Engine.builder((individual) -> deckGeneratorContext.fitness(individual, HeroClass.BLUE), bitGeneFactory)
				.populationSize(POPULATION_SIZE)
				.alterers(new BitSwapMutator<>(.9), new BitSwapMutator<>(.9), new BitSwapBetweenTwoSequencesMutator<>(.9))
				.build();

		Genotype<BitGene> result = engine.stream()
				.limit(NUMBER_OF_GENERATIONS)
				.collect(EvolutionResult.toBestGenotype());
		assertTrue(result.getChromosome().getGene(bestCardIndex1).getAllele());
	}
}
