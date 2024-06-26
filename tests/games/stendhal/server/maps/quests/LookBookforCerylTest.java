/***************************************************************************
 *                   (C) Copyright 2003-2023 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.quests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static utilities.SpeakerNPCTestHelper.getReply;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.npc.quest.BuiltQuest;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendhalRPRuleProcessor;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.orril.magician_house.WitchNPC;
import games.stendhal.server.maps.semos.library.LibrarianNPC;
import marauroa.common.Log4J;
import utilities.PlayerTestHelper;

public class LookBookforCerylTest {
	private static final String CERYL_BOOK = "ceryl_book";

	@BeforeClass
	public static void setupClass() {
		MockStendhalRPRuleProcessor.get();
		MockStendlRPWorld.get();
		Log4J.init();
	}

	private SpeakerNPC jynath;

	private SpeakerNPC ceryl;

	@Before
	public void setUp() throws Exception {
		PlayerTestHelper.generateNPCRPClasses();
		new WitchNPC().configureZone(new StendhalRPZone("testzone"), null);
		new LibrarianNPC().configureZone(new StendhalRPZone("testzone"), null);
		jynath = SingletonRepository.getNPCList().get("jynath");
		ceryl = SingletonRepository.getNPCList().get("ceryl");
	}

	@After
	public void tearDown() throws Exception {
		SingletonRepository.getNPCList().remove("ceryl");
		SingletonRepository.getNPCList().remove("Jynath");
	}

	@Test
	public final void askJynathWithoutQuest() {
		final AbstractQuest quest = new BuiltQuest(new LookBookforCeryl().story());

		quest.addToWorld();
		final Player pl = PlayerTestHelper.createPlayer("joe");
		assertFalse(quest.isStarted(pl));
		assertFalse(quest.isCompleted(pl));

		final Engine jynathEngine = jynath.getEngine();
		jynathEngine.step(pl, "Hi");
		assertTrue(jynath.isTalking());
		assertEquals("Greetings! How may I help you?", getReply(jynath));

		jynathEngine.step(pl, "book");
		assertTrue(jynath.isTalking());
		assertEquals(
				"Sssh! I'm concentrating on this potion recipe... it's a tricky one.",
				getReply(jynath));

		jynathEngine.step(pl, "bye");
		assertFalse(jynath.isTalking());
	}

	@Test
	public final void comeBackFromJynathWithoutBook() {
		final AbstractQuest quest = new BuiltQuest(new LookBookforCeryl().story());

		quest.addToWorld();
		final Player pl = PlayerTestHelper.createPlayer("joe");
		pl.setQuest(CERYL_BOOK, "jynath");

		final Engine cerylEngine = ceryl.getEngine();
		cerylEngine.step(pl, "Hi");
		assertTrue(ceryl.isTalking());
		assertEquals(
				"Haven't you got that #book back from #Jynath? Please go look for it, quickly!",
				getReply(ceryl));

		assertEquals("start", pl.getQuest(LookBookforCerylTest.CERYL_BOOK));
	}

	@Test
	public void doQuest() throws Exception {
		final AbstractQuest quest = new BuiltQuest(new LookBookforCeryl().story());

		quest.addToWorld();
		final Player pl = PlayerTestHelper.createPlayer("player");
		assertFalse(quest.isStarted(pl));
		assertFalse(quest.isCompleted(pl));
		final Engine cerylEngine = ceryl.getEngine();
		cerylEngine.step(pl, "Hi");
		assertTrue(ceryl.isTalking());
		assertEquals("Greetings! How may I help you?", getReply(ceryl));
		cerylEngine.step(pl, ConversationPhrases.QUEST_MESSAGES.get(0));
		assertTrue(ceryl.isTalking());
		assertEquals("I am looking for a very special book. Could you ask #Jynath to return it? She has had it for months now, and people are looking for it.",
				getReply(ceryl));
		cerylEngine.step(pl, ConversationPhrases.YES_MESSAGES.get(0));
		assertTrue(ceryl.isTalking());
		assertEquals(
				"Great! Please get me it as quickly as possible... there's a huge waiting list!",
				getReply(ceryl));
		assertEquals("start", pl.getQuest(LookBookforCerylTest.CERYL_BOOK));
		cerylEngine.step(pl, ConversationPhrases.GOODBYE_MESSAGES.get(0));
		assertFalse(ceryl.isTalking());
		final Engine jynathEngine = jynath.getEngine();
		jynathEngine.step(pl, "Hi");
		assertTrue(jynath.isTalking());
		assertEquals(
				"Oh, Ceryl's looking for that book back? My goodness! I completely forgot about it... here you go!",
				getReply(jynath));
		assertTrue(pl.isEquipped("black book"));
		jynathEngine.step(pl, "bye");
		assertFalse(jynath.isTalking());

		jynathEngine.step(pl, "Hi");
		assertTrue(jynath.isTalking());
		assertEquals(
				"You'd better take that book back to #Ceryl quickly... he'll be waiting for you.",
				getReply(jynath));

		jynathEngine.step(pl, "bye");
		assertFalse(jynath.isTalking());

		cerylEngine.step(pl, "Hi");
		assertTrue(ceryl.isTalking());
		assertEquals("Hi, did you get the book from Jynath?", getReply(ceryl));
		cerylEngine.step(pl, "yes");
		assertEquals("Oh, you got the book back! Phew, thanks!", getReply(ceryl));
		cerylEngine.step(pl, "bye");

		cerylEngine.step(pl, "Hi");
		assertTrue(ceryl.isTalking());
		assertEquals("Greetings! How may I help you?", getReply(ceryl));
		cerylEngine.step(pl, "quest");
		assertTrue(ceryl.isTalking());
		assertEquals("I have nothing for you now.", getReply(ceryl));
	}

	/**
	 * Tests for addToWorld.
	 */
	@Test
	public final void testAddToWorld() {
		final AbstractQuest quest = new BuiltQuest(new LookBookforCeryl().story());
		quest.addToWorld();
	}

	/**
	 * Tests for getHistory.
	 */
	@Test
	public final void testGetHistory() {
		final Player pl = PlayerTestHelper.createPlayer("player");
		final AbstractQuest quest = new BuiltQuest(new LookBookforCeryl().story());
		quest.addToWorld();

		assertTrue(quest.getHistory(pl).isEmpty());

		pl.setQuest(CERYL_BOOK, "rejected");
		assertThat(quest.getHistory(pl), equalTo(Arrays.asList(
						"I have met Ceryl at the library, he's the librarian there.",
						"I do not want to find the book.")));

		pl.setQuest(CERYL_BOOK, "start");
		assertThat(quest.getHistory(pl), equalTo(Arrays.asList(
					"I have met Ceryl at the library, he's the librarian there.",
					"I promised to fetch the black book from Jynath.")));

		pl.setQuest(CERYL_BOOK, "jynath");

		final Item item = SingletonRepository.getEntityManager().getItem("black book");
		assertNotNull(item);
		item.setBoundTo(pl.getName());
		pl.equipOrPutOnGround(item);
		assertThat(quest.getHistory(pl), equalTo(Arrays.asList(
					"I have met Ceryl at the library, he's the librarian there.",
					"I promised to fetch the black book from Jynath.",
					"I have talked to Jynath and got the book.")));

		pl.setQuest(CERYL_BOOK, "done");
		assertThat(quest.getHistory(pl), equalTo(Arrays.asList(
					"I have met Ceryl at the library, he's the librarian there.",
					"I promised to fetch the black book from Jynath.",
					"I have talked to Jynath and got the book.",
					"I have returned the book to Ceryl and got a little reward.")));
	}

	/**
	 * Tests for isCompleted.
	 */
	@Test
	public final void testIsCompleted() {
		final AbstractQuest quest = new BuiltQuest(new LookBookforCeryl().story());

		quest.addToWorld();
		final Player pl = PlayerTestHelper.createPlayer("player");
		assertFalse(quest.isCompleted(pl));
		pl.setQuest(LookBookforCerylTest.CERYL_BOOK, "done");
		assertTrue(pl.hasQuest(LookBookforCerylTest.CERYL_BOOK));
		assertTrue(pl.isQuestCompleted(LookBookforCerylTest.CERYL_BOOK));
		assertTrue(quest.isCompleted(pl));
	}

	/**
	 * Tests for isRepeatable.
	 */
	@Test
	public final void testIsRepeatable() {
		final AbstractQuest quest = new BuiltQuest(new LookBookforCeryl().story());
		assertFalse(quest.isRepeatable(null));
	}

	/**
	 * Tests for isStarted.
	 */
	@Test
	public final void testIsStarted() {
		final AbstractQuest quest = new BuiltQuest(new LookBookforCeryl().story());

		final Player bob = PlayerTestHelper.createPlayer("bob");
		assertFalse(bob.hasQuest(LookBookforCerylTest.CERYL_BOOK));
		assertFalse(quest.isStarted(bob));
		bob.setQuest(LookBookforCerylTest.CERYL_BOOK, "done");
		assertTrue(bob.hasQuest(LookBookforCerylTest.CERYL_BOOK));
		assertTrue(quest.isStarted(bob));
	}



}
