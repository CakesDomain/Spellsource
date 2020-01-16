package com.hiddenswitch.deckgeneration;

import io.jenetics.BitGene;
import io.jenetics.Genotype;
import net.demilich.metastone.game.GameContext;
import net.demilich.metastone.game.behaviour.IntelligentBehaviour;
import net.demilich.metastone.game.behaviour.PlayRandomBehaviour;
import net.demilich.metastone.game.cards.Attribute;
import net.demilich.metastone.game.cards.Card;
import net.demilich.metastone.game.decks.GameDeck;
import net.demilich.metastone.game.entities.heroes.HeroClass;
import net.demilich.metastone.game.statistics.Statistic;

import java.util.Arrays;
import java.util.List;

/**
 * Helper class that contains methods and information for
 * testing decks and creating them from a {@code BitChromosome}
 */
public class DeckGeneratorContext {
	int startingHp = 30;
	int maxCardsPerDeck = 30;
	int gamesPerMatch = 18;
	List<Card> indexInBitmap;
	List<GameDeck> basicTournamentDecks;
	IntelligentBehaviour enemyBehaviour = new PlayRandomBehaviour();
	IntelligentBehaviour playerBehaviour = new PlayRandomBehaviour();
	private boolean fatigueEnabled = true;

	public DeckGeneratorContext(List<Card> indexInBitmap, List<GameDeck> basicTournamentDecks) {
		this.indexInBitmap = indexInBitmap;
		this.basicTournamentDecks = basicTournamentDecks;
	}

	public void setIndexInBitmap(List<Card> indexInBitmap) {
		this.indexInBitmap = indexInBitmap;
	}

	public void setStartingHp(int startingHp) {
		this.startingHp = startingHp;
	}

	public void setMaxCardsPerDeck(int maxCardsPerDeck) {
		this.maxCardsPerDeck = maxCardsPerDeck;
	}

	public void setBasicTournamentDecks(List<GameDeck> basicTournamentDecks) {
		this.basicTournamentDecks = basicTournamentDecks;
	}

	public void setGamesPerMatch(int gamesPerMatch) {
		this.gamesPerMatch = gamesPerMatch;
	}

	public void setEnemyBehaviour(IntelligentBehaviour enemyBehaviour) {
		this.enemyBehaviour = enemyBehaviour;
	}

	public void setPlayerBehaviour(IntelligentBehaviour playerBehaviour) {
		this.playerBehaviour = playerBehaviour;
	}

	/**
	 * Turns a Genotype into a {@link GameDeck}
	 *
	 * @param individual The Genotype of BitGenes that determines the deck
	 * @param heroClass  The {@link HeroClass} of the deck
	 * @return
	 */
	public GameDeck deckFromBitGenotype(Genotype<BitGene> individual, String heroClass) {
		GameDeck deck = new GameDeck(heroClass);
		int count = 0;
		for (int i = 0; i < individual.getChromosome().length(); i++) {
			if (individual.getChromosome().getGene(i).booleanValue()) {
				deck.getCards().add(indexInBitmap.get(i));
				count++;

				if (count > maxCardsPerDeck) {
					break;
				}
			}
		}
		return deck;
	}

	/**
	 * Compute the fitness
	 *
	 * @param individual The Genotype of a BitChromosome containing the deck information
	 * @param heroClass  The {@link HeroClass} of the deck we are testing
	 * @return {@code Double.MIN_VALUE} if the deck is invalid, otherwise the average winrate of the deck
	 * under test with random players
	 */
	public double fitness(Genotype<BitGene> individual, String heroClass) {

		GameDeck gameDeck = deckFromBitGenotype(individual, heroClass);

		if (individual.getChromosome().
				stream().map(gene -> (gene.getBit() ? 1 : 0)).mapToInt(Integer::intValue).sum() != maxCardsPerDeck) {
			return Double.MIN_VALUE;
		}

		return basicTournamentDecks.stream()
				.map(opposingDeck -> GameContext.simulate(
						Arrays.asList(gameDeck, opposingDeck),
						() -> this.playerBehaviour,
						() -> this.enemyBehaviour,
						gamesPerMatch,
						true,
						false,
						null,
						this::handleContext)
				)
				.mapToDouble(res -> res.getPlayer1Stats().getDouble(Statistic.WIN_RATE))
				.average().orElse(Double.MIN_VALUE);
	}

	/**
	 * Set up a match in the fitness function
	 *
	 * @param ctx
	 */
	protected void handleContext(GameContext ctx) {
		ctx.getPlayer1().getHero().setMaxHp(startingHp);
		ctx.getPlayer2().getHero().setMaxHp(startingHp);
		ctx.getPlayer1().getHero().setBaseHp(startingHp);
		ctx.getPlayer2().getHero().setBaseHp(startingHp);
		ctx.getPlayer1().getHero().setHp(startingHp);
		ctx.getPlayer2().getHero().setHp(startingHp);
		if (!this.fatigueEnabled) {
			ctx.getPlayer1().getAttributes().put(Attribute.DISABLE_FATIGUE, true);
			ctx.getPlayer2().getAttributes().put(Attribute.DISABLE_FATIGUE, true);
		}

	}

	public void setFatigueEnabled(boolean fatigueEnabled) {
		this.fatigueEnabled = fatigueEnabled;
	}

	public boolean getFatigueEnabled() {
		return fatigueEnabled;
	}
}
