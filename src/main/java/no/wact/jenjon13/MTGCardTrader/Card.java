package no.wact.jenjon13.MTGCardTrader;

public class Card {
    private final String title;
    private final String edition;
    private final String type;
    private final String cast;
    private final String powerAndToughness;
    private final String rarity;
    private final String condition;
    private final String stock;
    private final float price;
    private int amount = 1;

    public Card(Card base, String condition, String stock, float price) {
        this.title = base.getTitle();
        this.edition = base.getEdition();
        this.type = base.getType();
        this.cast = base.getCast();
        this.rarity = base.getRarity();

        this.condition = condition;
        this.stock = stock;
        this.price = price;
        this.powerAndToughness = base.getPowerAndToughness();
    }

    public Card(String title, String edition, String type, String cast, String powerAndToughness, String rarity,
            String condition, String stock, float price) {
        this.title = title;
        this.edition = edition;
        this.type = type;
        this.cast = cast;
        this.powerAndToughness = powerAndToughness;
        this.rarity = rarity;
        this.condition = condition;
        this.stock = stock;
        this.price = price;
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
                .append(", condition='")
                .append(condition)
                .append('\'')
                .append(", stock='")
                .append(stock)
                .append('\'')
                .append(", price=")
                .append(price)
                .append('}')
                .toString();
    }

    public String getRarity() {
        return rarity;
    }

    public String getCondition() {
        return condition;
    }

    public String getStock() {
        return stock;
    }

    public float getPrice() {
        return price;
    }
}
