package org.MachinaEconomy.ThePrometeus;


public class Utils {
    private final Main plugin;

    Utils(Main m) {
        plugin          = m;
    }

    public double getDecimals(double amount) {
        return amount - Math.floor(amount);
    }
    
    /**
     * Format an amount to a displayable text
     * @param amount Money amount
     * @return String
     */
    public String format(double amount) {
        double decimals = this.getDecimals(amount);
        
        String f;

        if (decimals == 0) {
            f = plugin.getConfig().getString("currency.name");
        } else {
            f = plugin.getConfig().getString("currency.namePlural");
        }

        return amount + " " + f;
    }

    public String chatColorToText(String string) {
        return string.replaceAll("§", "&");
    }
}
