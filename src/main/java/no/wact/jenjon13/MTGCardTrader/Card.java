package no.wact.jenjon13.MTGCardTrader;

import java.util.HashMap;
import java.util.Map;

public class Card {
    private final String title;
    private final String edition;
    private final String type;
    private final String cast;
    private final String powerAndToughness;
    private final String rarity;
    private final Map<Condition, Float> conditionPriceMap;
    private int amount = 1;
    private Condition condition;

    public Card(String title, String edition, String type, String cast, String powerAndToughness, String rarity,
            Condition condition, float price) {
        this.title = title;
        this.edition = edition;
        this.type = type;
        this.cast = cast;
        this.powerAndToughness = powerAndToughness;
        this.rarity = rarity;
        conditionPriceMap = new HashMap<Condition, Float>(1);
        conditionPriceMap.put(condition, price);
        this.condition = condition;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Card{")
                .append("title='")
                .append(title)
                .append('\'')
                .append(", edition='")
                .append(edition)
                .append('\'')
                .append(", type='")
                .append(type)
                .append('\'')
                .append(", cast='")
                .append(cast)
                .append('\'')
                .append(", powerAndToughness='")
                .append(powerAndToughness)
                .append('\'')
                .append(", rarity='")
                .append(rarity)
                .append('\'')
                .append(", conditionPriceMap=")
                .append(conditionPriceMap)
                .append(", amount=")
                .append(amount)
                .append('}')
                .toString();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public String getEdition() {
        return edition;
    }

    public String getType() {
        return type;
    }

    public String getCast() {
        return cast;
    }

    public String getPowerAndToughness() {
        return powerAndToughness;
    }

    public String getRarity() {
        return rarity;
    }

    public float getPrice() {
        final Float price = conditionPriceMap.get(getCondition());
        return price != null ? price : -1;
    }

    public boolean addNewPrice(Condition condition, float price) {
        if (conditionPriceMap.get(condition) == null) {
            conditionPriceMap.put(condition, price);
            return true;
        }

        return false;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
