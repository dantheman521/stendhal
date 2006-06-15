package games.stendhal.server.maps.quests;

import games.stendhal.common.Rand;
import games.stendhal.server.*;
import games.stendhal.server.entity.Player;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.NPCList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.pathfinder.Path;

import java.util.*;

import marauroa.common.game.IRPZone;

/** 
 * QUEST: Find the seven cherubs that are all around the world.
 * PARTICIPANTS: 
 * - Cherubiel
 * - Gabriel
 * - Ophaniel
 * - Raphael
 * - Uriel
 * - Zophiel
 * - Azazel
 * STEPS: 
 * - Find them and they will reward you.
 *
 * REWARD: 
 * - 
 *
 * REPETITIONS:
 * - Just once.
 */
public class SevenCherubs implements IQuest {
	private StendhalRPWorld world;

	private NPCList npcs;

	static class CherubNPC extends SpeakerNPC {
		public CherubNPC(String name, int x, int y) {
			super(name);

			put("class", "angelnpc");
			set(x, y);
			initHP(100);

			List<Path.Node> nodes = new LinkedList<Path.Node>();
			nodes.add(new Path.Node(x, y));
			nodes.add(new Path.Node(x - 2, y));
			nodes.add(new Path.Node(x - 2, y - 2));
			nodes.add(new Path.Node(x, y - 2));
			setPath(nodes, true);
		}

		@Override
		protected void createPath() {
			// do nothing
		}

		@Override
		protected void createDialog() {
			add(ConversationStates.IDLE,
				SpeakerNPC.GREETING_MESSAGES,
				null,
				ConversationStates.IDLE,
				null,
				new SpeakerNPC.ChatAction() {
					public void fire(Player player, String text,
							SpeakerNPC engine) {
						if (!player.hasQuest("seven_cherubs")) {
							player.setQuest("seven_cherubs", "");
						}
	
						String npcDoneText = player
								.getQuest("seven_cherubs");
						String[] done = npcDoneText.split(";");
						List<String> list = Arrays.asList(done);
						int left = 7 - list.size();

						if (!list.contains(engine.getName())) {
							player.setQuest("seven_cherubs", npcDoneText
									+ ";" + engine.getName());
		
							player.setHP(player.getBaseHP());
							player.healPoison();
	
							if (left > 0) {
								engine.say("Only need to find "
										+ (7 - list.size())
										+ " more. Farewell.");
								player.addXP((7 - left + 1) * 200);
							} else {
								engine.say("Thou have proven yourself brave enough to wear this present!");
	
								String[] items = { "golden_boots",
										"golden_armor", "fire_sword",
										"golden_shield", "golden_legs",
										"golden_helmet" };
								Item item = world
										.getRuleManager()
										.getEntityManager()
										.getItem(items[Rand.rand(items.length)]);
								if (!player.equip(item)) {
									StendhalRPZone zone = (StendhalRPZone) world
											.getRPZone(player.getID());
	
									zone.assignRPObjectID(item);
									item.setx(player.getx());
									item.sety(player.gety());
									zone.add(item);
								}
								player.addXP(2000);
							}
						} else {
							if (left > 0) {
								engine.say("Find the rest of us to get the reward");
							} else {
								engine.say("You found all of us and got the reward.");
							}
						}
						world.modify(player);
					}
				});
			addGoodbye();
		}
	}

	public SevenCherubs(StendhalRPWorld w, StendhalRPRuleProcessor rules) {
		this.npcs = NPCList.get();
		this.world = w;

		StendhalRPZone zone;
		SpeakerNPC npc;

		zone = (StendhalRPZone) world.getRPZone(new IRPZone.ID(
				"0_semos_village_w"));
		npc = new CherubNPC("Cherubiel", 48, 59);
		npcs.add(npc);
		zone.assignRPObjectID(npc);
		zone.addNPC(npc);

		zone = (StendhalRPZone) world
				.getRPZone(new IRPZone.ID("0_nalwor_city"));
		npc = new CherubNPC("Gabriel", 105, 16);
		npcs.add(npc);
		zone.assignRPObjectID(npc);
		zone.addNPC(npc);

		zone = (StendhalRPZone) world.getRPZone(new IRPZone.ID(
				"0_orril_river_s"));
		npc = new CherubNPC("Ophaniel", 105, 78);
		npcs.add(npc);
		zone.assignRPObjectID(npc);
		zone.addNPC(npc);

		zone = (StendhalRPZone) world.getRPZone(new IRPZone.ID(
				"0_orril_river_s_w2"));
		npc = new CherubNPC("Raphael", 95, 29);
		npcs.add(npc);
		zone.assignRPObjectID(npc);
		zone.addNPC(npc);

		zone = (StendhalRPZone) world.getRPZone(new IRPZone.ID(
				"0_orril_mountain_w2"));
		npc = new CherubNPC("Uriel", 47, 26);
		npcs.add(npc);
		zone.assignRPObjectID(npc);
		zone.addNPC(npc);

		zone = (StendhalRPZone) world.getRPZone(new IRPZone.ID(
				"0_semos_mountain_n2_w2"));
		npc = new CherubNPC("Zophiel", 16, 2);
		npcs.add(npc);
		zone.assignRPObjectID(npc);
		zone.addNPC(npc);

		zone = (StendhalRPZone) world.getRPZone(new IRPZone.ID("0_ados_rock"));
		npc = new CherubNPC("Azazel", 67, 23);
		npcs.add(npc);
		zone.assignRPObjectID(npc);
		zone.addNPC(npc);
	}
}