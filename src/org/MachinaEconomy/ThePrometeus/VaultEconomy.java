package org.MachinaEconomy.ThePrometeus;

import java.util.List;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.MachinaEconomy.ThePrometeus.Entities.MachinaOfflinePlayerEntity;
import org.bukkit.OfflinePlayer;

public class VaultEconomy implements Economy {
    private final Main plugin;
    
    public VaultEconomy(Main m) {
        plugin = m;
    }   

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public String getName() {
        return plugin.getName();
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
       return -1;
    }

    @Override
    public String format(double d) {
        return plugin.utils.format(d);
    }

    @Override
    public String currencyNamePlural() {
        return plugin.getConfig().getString("currency.namePlural");
    }

    @Override
    public String currencyNameSingular() {
        return plugin.getConfig().getString("currency.name");
    }

    @Override
    public boolean hasAccount(String playerName) {
        return plugin.bank.accountExists(plugin.entityFromName(playerName));
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return this.hasAccount(playerName);
    }

    @Override
    public double getBalance(String playerName) {
        return plugin.bank.getMoney(plugin.entityFromName(playerName));
    }

    @Override
    public double getBalance(String playerName, String worldName) {
        return this.getBalance(playerName);
    }

    @Override
    public boolean has(String playerName, double d) {
        return plugin.bank.hasMoreThan(plugin.entityFromName(playerName), d);
    }

    @Override
    public boolean has(String playerName, String worldName, double d) {
        return this.has(playerName, d);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double d) {
        Entity e = plugin.entityFromName(playerName);

        if (d < 0) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative funds.");
        }
        
        // Process new player amount
        double newAmount = plugin.bank.adjustMoney(e, d * -1);

        // Check if operation succeeded
        if (newAmount > -1) {
            return new EconomyResponse(d, newAmount, ResponseType.SUCCESS, null);
        }

        return new EconomyResponse(0, plugin.bank.getMoney(e), ResponseType.FAILURE, "Insufficient funds.");
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double d) {
        return this.withdrawPlayer(playerName, d);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double d) {
        Entity e = plugin.entityFromName(playerName);

        if (d < 0) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot deposit negative funds.");
        }

        // Process new player amount
        double newAmount = plugin.bank.adjustMoney(e, d);

        // Check if operation succeeded
        if (newAmount > -1) {
            return new EconomyResponse(d, newAmount, ResponseType.SUCCESS, null);
        }

        return new EconomyResponse(0, plugin.bank.getMoney(e), ResponseType.FAILURE, "Operation failed.");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double d) {
        return this.depositPlayer(playerName, d);
    }

    @Override
    public EconomyResponse createBank(String playerName, String worldName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "This feature is not supported in this plugin.");
    }

    @Override
    public EconomyResponse deleteBank(String playerName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "This feature is not supported in this plugin.");
    }

    @Override
    public EconomyResponse bankBalance(String playerName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "This feature is not supported in this plugin.");
    }

    @Override
    public EconomyResponse bankHas(String playerName, double d) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "This feature is not supported in this plugin.");
    }

    @Override
    public EconomyResponse bankWithdraw(String playerName, double d) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "This feature is not supported in this plugin.");
    }

    @Override
    public EconomyResponse bankDeposit(String playerName, double d) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "This feature is not supported in this plugin.");
    }

    @Override
    public EconomyResponse isBankOwner(String playerName, String worldName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "This feature is not supported in this plugin.");
    }

    @Override
    public EconomyResponse isBankMember(String playerName, String worldName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "This feature is not supported in this plugin.");
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return plugin.bank.createAccount(plugin.entityFromName(playerName));
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return this.createPlayerAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer op) {
       return plugin.bank.accountExists(new MachinaOfflinePlayerEntity(op));
    }

    @Override
    public boolean hasAccount(OfflinePlayer op, String string) {
       return this.hasAccount(op);
    }

    @Override
    public double getBalance(OfflinePlayer op) {
       return plugin.bank.getMoney(new MachinaOfflinePlayerEntity(op));
    }

    @Override
    public double getBalance(OfflinePlayer op, String worldName) {
       return this.getBalance(op);
    }

    @Override
    public boolean has(OfflinePlayer op, double d) {
       return plugin.bank.hasMoreThan(new MachinaOfflinePlayerEntity(op), d);
    }

    @Override
    public boolean has(OfflinePlayer op, String worldName, double d) {
       return this.has(op, d);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer op, double d) {
        Entity e = new MachinaOfflinePlayerEntity(op);

        if (d < 0) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative funds.");
        }
        
        // Process new player amount
        double newAmount = plugin.bank.adjustMoney(e, d * -1);

        // Check if operation succeeded
        if (newAmount > -1) {
            return new EconomyResponse(d, newAmount, ResponseType.SUCCESS, null);
        }

        return new EconomyResponse(0, plugin.bank.getMoney(e), ResponseType.FAILURE, "Insufficient funds.");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer op, String worldName, double d) {
       return this.withdrawPlayer(op, d);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer op, double d) {
        Entity e = new MachinaOfflinePlayerEntity(op);

        if (d < 0) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot deposit negative funds.");
        }
        
        // Process new player amount
        double newAmount = plugin.bank.adjustMoney(e, d);

        // Check if operation succeeded
        if (newAmount > -1) {
            return new EconomyResponse(d, newAmount, ResponseType.SUCCESS, null);
        }

        return new EconomyResponse(0, plugin.bank.getMoney(e), ResponseType.FAILURE, "Insufficient funds.");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer op, String worldName, double d) {
       return this.depositPlayer(op, d);
    }

    @Override
    public EconomyResponse createBank(String string, OfflinePlayer op) {
       return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "This feature is not supported in this plugin.");
    }

    @Override
    public EconomyResponse isBankOwner(String string, OfflinePlayer op) {
       return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "This feature is not supported in this plugin.");
    }

    @Override
    public EconomyResponse isBankMember(String string, OfflinePlayer op) {
       return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "This feature is not supported in this plugin.");
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer op) {
       return plugin.bank.createAccount(new MachinaOfflinePlayerEntity(op));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer op, String worldName) {
       return this.createPlayerAccount(op);
    }
}
