package org.MachinaEconomy.ThePrometeus;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.MachinaEconomy.ThePrometeus.Entities.MachinaPlayerEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {
    public final FileConfiguration language = new YamlConfiguration();
    private final CommandListener cmd = new CommandListener(this);
    public Bank bank = new Bank(this);
    public Database db = new Database();
    public Utils utils = new Utils(this);

    /**
     * Set configuration defaults
     */
    private void setDefaults() {        
        /**
         * Default config section
         */
        
        getConfig().addDefault("currency.name", "dollar");
        getConfig().addDefault("currency.namePlural", "dollars");
        getConfig().addDefault("currency.sign", "$");
        getConfig().addDefault("currency.defaultAmount", 30);

        getConfig().addDefault("connection.uri", "jdbc:mysql://localhost/minecraft");
        getConfig().addDefault("connection.username", "root");
        getConfig().addDefault("connection.password", "");

        /**
         * Language section
         */
        
        getLanguage().addDefault("prefix", utils.chatColorToText(ChatColor.GREEN + "" + ChatColor.BOLD + "[MachinaEconomy]"));

        getLanguage().addDefault("internalError", utils.chatColorToText(ChatColor.RED + "An internal error happened, please try again later."));
        getLanguage().addDefault("notEnoughArguments", utils.chatColorToText(ChatColor.RED + "You have specified an invalid amount of arguments."));
        getLanguage().addDefault("unknownCommand", utils.chatColorToText(ChatColor.RED + "Unkown command."));
        getLanguage().addDefault("unknownPlayer", utils.chatColorToText(ChatColor.RED + "Unkown player {0}."));
        getLanguage().addDefault("invalidAmount", utils.chatColorToText(ChatColor.RED + "The amount you given is invalid."));
        getLanguage().addDefault("notEnoughMoney", utils.chatColorToText(ChatColor.RED + "You don't have enough money to complete this operation."));
        
        getLanguage().addDefault("selfAmount", utils.chatColorToText(ChatColor.GREEN + "You have {0}."));
        getLanguage().addDefault("playerAmount", utils.chatColorToText(ChatColor.GREEN + "{0} have {1}."));
        
        getLanguage().addDefault("transactionSent", utils.chatColorToText(ChatColor.GREEN + "You have sent {0} to {1}."));
        getLanguage().addDefault("transactionReceived", utils.chatColorToText(ChatColor.GREEN + " {0} sent {1} to you."));
        
        getLanguage().addDefault("adjustSent", utils.chatColorToText(ChatColor.GREEN + "You have set {1} balance to {0}."));
        getLanguage().addDefault("adjustReceived", utils.chatColorToText(ChatColor.GREEN + "{0} have set your balance to {1}."));
        
        getLanguage().addDefault("top10Header", utils.chatColorToText(ChatColor.GREEN + "Top 10 players | Page {0}"));

        getConfig().options().copyDefaults(true);
        getLanguage().options().copyDefaults(true);
    }
    
    /**
     * Try loading the plugin language configuration
     */
    public void loadLanguage() {
        try {
            language.load(getDataFolder().getAbsolutePath() + File.separator + "language.yml");
        } catch (IOException | InvalidConfigurationException ex) {

        }
    }
    
    @Override
    public void saveConfig() {
        super.saveConfig();

        try {
            language.save(getDataFolder().getAbsolutePath() + File.separator + "language.yml");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onEnable() {    
        super.onEnable();
        
        // Set plugin defaults
        setDefaults();
        
        // Load config
        loadLanguage();

        getLogger().info("Config file loaded");

        // Set command listener
        getCommand("money").setExecutor(cmd);
        
        if (!db.connect(getConfig().getString("connection.uri"), getConfig().getString("connection.username"), getConfig().getString("connection.password"))) {
            getLogger().log(Level.SEVERE, "Can't connect to MySQL server.");
            this.setEnabled(false);
            return;
        } else {
            getLogger().info("Connected to MySQL.");
        }
        
        final Main m = this;
        
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                getLogger().info("Hooking into Vault...");

                // Try retrieving the Vault plugin
                Plugin Vault = Bukkit.getPluginManager().getPlugin("Vault");
                
                // Check if Vault is not loaded
                if (Vault == null) {
                    getLogger().severe("Vault not found, turning the plugin off...");

                    // Disable the plugin
                    m.getServer().getPluginManager().disablePlugin(m);
                    return;
                }

                ServicesManager sm = Bukkit.getServicesManager();

                // Create a new Vault connection
                sm.register(net.milkbowl.vault.economy.Economy.class, new VaultEconomy(m), m, ServicePriority.Highest);

                getLogger().info("Vault hook succeeded");
            }
        };

        runnable.runTaskLater(this, 1L);
        
        // Save the configuration
        saveConfig();

        getLogger().info("Enabled and ready to hook.");
    }

    public FileConfiguration getLanguage() {
        return language;
    }
    
    public String formatMessage(String index) {
        return ChatColor.translateAlternateColorCodes('&', language.getString("prefix")) + ChatColor.RESET + " " + ChatColor.translateAlternateColorCodes('&', language.getString(index));
    }

    public String formatMessage(String index, Object... args) {
        return ChatColor.translateAlternateColorCodes('&', language.getString("prefix")) + ChatColor.RESET + " " + ChatColor.translateAlternateColorCodes('&', new MessageFormat(language.getString(index)).format(args));
    }
    
    public MachinaPlayerEntity entityFromName(String name) {
        return new MachinaPlayerEntity(Bukkit.getOfflinePlayer(name).getPlayer());
    }
    
    public MachinaPlayerEntity entityFromUUID(UUID uuid) {
        return new MachinaPlayerEntity(Bukkit.getOfflinePlayer(uuid).getPlayer());
    }
}