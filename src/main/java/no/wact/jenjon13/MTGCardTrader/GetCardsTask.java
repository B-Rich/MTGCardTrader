package no.wact.jenjon13.MTGCardTrader;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetCardsTask extends AsyncTask<String, Void, List<Card>> {
    private final Activity activity;
    private final ArrayList<Card> dataSet;
    private final CardsAdapter adapter;

    public GetCardsTask(Activity activity, ArrayList<Card> dataSet, CardsAdapter adapter) {
        this.activity = activity;
        this.dataSet = dataSet;
        this.adapter = adapter;
    }

    @Override

    protected List<Card> doInBackground(String... params) {
        Log.v("doInBackground", "Fetching ..");
        if (params[0] == null || params[0].isEmpty()) {
            return null;
        }

        Log.v("doInBackground", "Params ok, starting search..");
        return searchForCards(params[0]);
    }

    @Override
    protected void onPostExecute(List<Card> cards) {
        if (cards != null && !cards.isEmpty()) {
            dataSet.addAll(cards);
            adapter.notifyDataSetChanged();
        }
    }

    private List<Card> searchForCards(String cardName) {
        final String searchURL = "http://cardkingdom.com/catalog/view?search=basic&filter%5Bname%5D=";
        final String cardNameFormatted = cardName.trim().replaceAll(" ", "+");
        final Document document;
        try {
            document = Jsoup.connect(searchURL + cardNameFormatted).get();
        } catch (IOException e) {
            Log.e("search", "An error occurred when getting document!");
            return null;
        }

        final Elements cards = document.select("body > div.colmask.holygrail > div > div > div.col1wrap > div " +
                "> div > table:nth-child(10) > tbody > tr");
        if (cards.size() < 1) {
            cancel(true);
            return null;
        }

        cards.remove(0); // Remove header row.

        final ArrayList<Element> foundCards = new ArrayList<Element>();
        for (int i = 0; i < cards.size(); i++) {
            final String matchingCard = cards.get(i).child(0).child(0).text().trim();
            if (matchingCard.toLowerCase().equals(cardName.trim().toLowerCase()) ||
                    i > 0 && matchingCard.isEmpty() && cards.get(i - 1)
                            .child(0)
                            .child(0)
                            .text()
                            .trim()
                            .toLowerCase()
                            .equals(cardName.trim().toLowerCase())) {
                foundCards.add(cards.get(i));
            }
        }

        return CardParser.parse(foundCards);
    }
}
