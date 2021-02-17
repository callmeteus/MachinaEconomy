package org.MachinaEconomy.ThePrometeus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

class Bank {
    private final Main plugin;

    public Bank(Main main) {
        this.plugin = main;
    }
    
     /**
     * Creates a bank account
     * @param e MachinaEntity that will receive an accoun
     * @return 
     */
    public boolean createAccount(Entity e) {
        try {
            // Prepare the statement
            PreparedStatement stmt = plugin.db.getConnection().prepareStatement("INSERT INTO machina_economy SET uuid = ?, name = ?, amount = ?");

            stmt.setBytes(1, e.getHexUUID());
            stmt.setString(2, e.getName());
            stmt.setFloat(3, plugin.getConfig().getLong("currency.defaultAmount"));

            stmt.execute();
            
            return true;
        } catch (SQLException err) {
            return false;
        }
    }

    /**
     * Creates a bank account
     * @param e MachinaEntity that will receive an account
     * @param amount Balance to start with
     * @return 
     */
    public boolean createAccount(Entity e, double amount) {
        if (createAccount(e)) {
            return setMoney(e, amount) > -1;
        }
        
        return false;
    }
    
    /**
     * Check if an account exists
     * @param e MachinaEntity account
     * @param amount Desired amount
     * @return 
     */
    public boolean accountExists(Entity e) {
        try {
            // Query for player account
            PreparedStatement stmt = plugin.db.getConnection().prepareStatement("SELECT 1 FROM machina_economy WHERE uuid = ? OR name = ?");

            stmt.setBytes(1, e.getHexUUID());
            stmt.setString(2, e.getName());

            ResultSet res = stmt.executeQuery();

            // Check if has any result
            if (res.next()) {
                return true;
            }
        } catch(SQLException err) {
            plugin.getLogger().log(Level.SEVERE, "Error retrieving account money: {0}", err.getMessage());
        }

        return false;
    }

    /**
     * Check if account has more than the desired amount
     * @param e MachinaEntity
     * @param amount Desired amount
     * @return 
     */
    public boolean hasMoreThan(Entity e, double amount) {
        try {
            // Query for player account
            PreparedStatement stmt = plugin.db.getConnection().prepareStatement("SELECT amount FROM machina_economy WHERE uuid = ? OR name = ?");

            stmt.setBytes(1, e.getHexUUID());
            stmt.setString(2, e.getName());

            ResultSet res = stmt.executeQuery();

            // Check if has any result
            if (res.next()) {
                return res.getDouble("amount") >= amount;
            }
        } catch(SQLException err) {
            plugin.getLogger().log(Level.SEVERE, "Error retrieving account money: {0}", err.getMessage());
        }

        return false;
    }

    /**
     * Get an account balance
     * @param e MachinaEntity account
     * @return Account balance
     */
    public double getMoney(Entity e) {
        try {
            // Query for player account
            PreparedStatement stmt = plugin.db.getConnection().prepareStatement("SELECT amount FROM machina_economy WHERE uuid = ? OR name = ?");

            stmt.setBytes(1, e.getHexUUID());
            stmt.setString(2, e.getName());

            ResultSet res = stmt.executeQuery();

            // Check if has any result
            if (res.next()) {
                return res.getDouble("amount");
            } else {
                // Create entity account
                createAccount(e);
            }
        } catch(SQLException err) {
            plugin.getLogger().log(Level.SEVERE, "Error retrieving account money: {0}", err.getMessage());

            return -1;
        }

        return plugin.getConfig().getInt("currency.defaultAmount");
    }

    /**
     * Adjust an account balance
     * @param e MachinaEntity account
     * @param amount Amount to adjust (can be negative)
     * @return New account balance
     */
    public double adjustMoney(Entity e, double amount) {
        try {
            // Query for player account
            PreparedStatement stmt = plugin.db.getConnection().prepareStatement("UPDATE machina_economy SET amount = amount + ? WHERE uuid = ? OR name = ?");

            stmt.setDouble(1, amount);
            stmt.setBytes(2, e.getHexUUID());
            stmt.setString(3, e.getName());

            int res = stmt.executeUpdate();

            // Check if has any result
            if (res > 0) {
                return this.getMoney(e);
            } else {
                // Create entity account
                createAccount(e, amount);
            }
        } catch(SQLException err) {
            plugin.getLogger().log(Level.SEVERE, "Error adjusting account money: {0}, player {1}, amount: {2}", new Object[]{err.getMessage(), e.getName(), amount});
        }

        return -1;
    }

     /**
     * Set an account balance
     * @param e MachinaEntity account
     * @param amount Amount to adjust (can be negative)
     * @return New account balance
     */
    public double setMoney(Entity e, double amount) {
        amount = Math.max(amount, 0);
        
        try {
            // Query for player account
            PreparedStatement stmt = plugin.db.getConnection().prepareStatement("UPDATE machina_economy SET amount = ? WHERE uuid = ? OR name = ?");

            stmt.setDouble(1, amount);
            stmt.setBytes(2, e.getHexUUID());
            stmt.setString(3, e.getName());

            int res = stmt.executeUpdate();

            // Check if has any result
            if (res > 0) {
                return this.getMoney(e);
            } else {
                // Create entity account
                createAccount(e, amount);
            }
        } catch(SQLException err) {
            plugin.getLogger().log(Level.SEVERE, "Error setting account money: {0}", err.getMessage());
        }

        return -1;
    }

    /**
     * Get the top 10 most richest players
     * @return Map<PlayerName, Balance> 
     */
    public Map<String, Integer> getTop10(int page) {
        Map<String, Integer> ret = new HashMap<>();
        
        page--;

        try {
            // Query for player account
            PreparedStatement stmt = plugin.db.getConnection().prepareStatement("SELECT name, amount FROM machina_economy ORDER BY amount DESC LIMIT ?,?");

            stmt.setInt(1, page * 10);
            stmt.setInt(2, 10);

            // Get query result
            ResultSet res = stmt.executeQuery();
            
            while(res.next()) {
                ret.put(res.getString("name"), res.getInt("amount"));
            }
        } catch(SQLException err) {
            plugin.getLogger().log(Level.SEVERE, "Error setting account money: {0}", err.getMessage());
        }
        
        return ret;
    }
}
