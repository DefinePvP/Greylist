package cwok.main;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Greylist extends JavaPlugin implements Listener {

	public static Greylist plugin;

	public void onEnable() {
		plugin = this;

		// TODO: Make this into a config so everything isn't hard coded
		// Registers events
		getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable() {

	}

	// TODO: Add a command to review applications, a list of unreviewed applications
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("greylist")) {

			// Checks permission before anything is done
			if (!sender.hasPermission("greylist.recruiter")) {
				return false;
			}

			// Checks if there is two arguments
			try {

				if (args.length == 0) {
					sender.sendMessage(ChatColor.AQUA + "Usage> " + ChatColor.DARK_AQUA
							+ "Greylist [Accept | Deny | Info | Open | Reset | Clear] [Player]");
					return true;
				}
			} catch (Exception e) {
				sender.sendMessage(ChatColor.AQUA + "Usage> " + ChatColor.DARK_AQUA
						+ "Greylist [Accept | Deny | Info | Open | Reset | Clear] [Player]");
			}

			// Is the single argument that does not require a valid player

			// Checks if there is two arguments
			try {
				if (args.length == 1) {
					sender.sendMessage(ChatColor.AQUA + "Usage> " + ChatColor.DARK_AQUA
							+ "Greylist [Accept | Deny | Info | Open | Reset | Clear] [Player]");
					return true;
				}
			} catch (Exception e) {
				sender.sendMessage(ChatColor.AQUA + "Usage> " + ChatColor.DARK_AQUA
						+ "Greylist [Accept | Deny | Info | Open | Reset | Clear] [Player]");
			}

			if (args[0].equalsIgnoreCase("accept")) {
					Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
							"luckperms user " + args[1] + " permission set greylist.accepted");
					Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
							"luckperms user " + args[1] + " group set member");
					Player playerChosen = Bukkit.getServer().getPlayer(args[1]);


					try {
						Bukkit.getServer().broadcastMessage(ChatColor.GREEN + playerChosen.getName()
								+ "'s application has been accepted by " + sender.getName());
					} catch (Exception e) {
						Bukkit.getServer().broadcastMessage(
								ChatColor.GREEN + args[1] + "'s application has been accepted by " + sender.getName());
					}
				} else {
					sender.sendMessage(ChatColor.RED + "This player has already been accepted or denied");
				}

			if (args[0].equalsIgnoreCase("deny")) {
					Bukkit.getServer().broadcastMessage(
							ChatColor.WHITE + args[1] + "'s " + "application has been denied by an admin");
					Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
							"mute " + args[1] + " Application Denied");
					
				} else {
					sender.sendMessage(ChatColor.AQUA + "Usage> " + ChatColor.DARK_AQUA
							+ "Greylist [Accept | Deny | Info | Open | Reset | Clear] [Player]");
				}
			if (args[0].equalsIgnoreCase("reset")) {

				Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
						"luckperms user " + args[1] + " permission unset greylist.accepted");
				Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), "unmute " + args[1]);
				Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
						"luckperms user " + args[1] + " demote greylist");

				sender.sendMessage(ChatColor.GREEN + args[1] + "'s files have been reset");

				return true;
			}
			return true;
		}
		return false;
	}

	// Here begins the simple methods that simply return simple things simply.
	// Only one method now, used to be a lot more.
	public File getPluginDataFolder() {
		return getDataFolder();
	}

	/*
	 * public FileConfiguration getBukkitConfig() { return getConfig(); }
	 */

	// TODO: Clean up the commands and events into different classes

	// Everything below this line is an event
	// Captures responses from chat

	// TODO: Add messages so players know they have to /apply
	// Gives unregistered players god mode
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (((entity instanceof Player)) && (!entity.hasPermission("greylist.accepted"))) {
			event.setCancelled(true);
		}
	}

	// Prevents unregistered players from PvP or PvE
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		Entity entity = event.getDamager();
		if (((entity instanceof Player)) && (!entity.hasPermission("greylist.accepted"))) {
			event.setCancelled(true);
		}
	}

	// Prevents unregistered players from starving
	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
		Entity entity = event.getEntity();
		if (((entity instanceof Player)) && (!entity.hasPermission("greylist.accepted"))) {
			event.setCancelled(true);
		}
	}

	// Prevents unregistered players from pressing buttons or activating pressure
	// plates
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("greylist.accepted")) {
			event.setCancelled(true);
		}
	}

	// Prevents unregistered players from picking up dropped items
	@EventHandler
	public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
		Entity entity = event.getEntity();
		if (((entity instanceof Player)) && (!entity.hasPermission("greylist.accepted"))) {
			event.setCancelled(true);
		}
	}

	// Prevents unregistered players from placing blocks
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		Player blockplacer = event.getPlayer();
		if (!blockplacer.hasPermission("greylist.accepted")) {
			blockplacer.sendMessage(ChatColor.AQUA + "You must be a member to interact with the world, please "
					+ ChatColor.WHITE + "/apply" + ChatColor.AQUA + " to our server");
			event.setCancelled(true);
		}
	}

	// Prevents unregistered players from breaking blocks
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("greylist.accepted")) {
			player.sendMessage(ChatColor.AQUA + "You must be a member to interact with the world, please "
					+ ChatColor.WHITE + "/apply" + ChatColor.AQUA + " to our server");
			event.setCancelled(true);
		}
	}

	// Prevents monsters from targeting unregistered players
	@EventHandler
	public void onEntityTargetEvent(EntityTargetEvent event) {
		Entity player = event.getTarget();
		if (((player instanceof Player)) && (!player.hasPermission("greylist.accepted"))) {
			event.setCancelled(true);
		}
	}

	// Prevents riding horses, interacting with villagers, and right clicking blocks
	// and stuff
	@EventHandler
	public void onAnimalInteractEvent(PlayerInteractEntityEvent event) {
		Entity player = event.getPlayer();
		if (((player instanceof Player)) && (!player.hasPermission("greylist.accepted"))) {
			event.setCancelled(true);
		}
	}

	// Sends first time join messages and recruiters messages that the player who
	// joined is not registered

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player joiner = event.getPlayer();

		if (!joiner.hasPermission("greylist.accepted")) {

			if (!event.getPlayer().hasPlayedBefore()) {
				Bukkit.broadcastMessage(ChatColor.RED + joiner.getName() + ChatColor.DARK_AQUA + " has joined  "
						+ ChatColor.AQUA + "this server " + ChatColor.DARK_AQUA + "for the first time!");
			}
			Bukkit.getServer().broadcast(ChatColor.RED + joiner.getName() + ChatColor.DARK_AQUA + " is not greylisted!",
					"greylist.recruiter");
		}
	}
}