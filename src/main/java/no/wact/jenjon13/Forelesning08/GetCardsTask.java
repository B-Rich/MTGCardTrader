package no.wact.jenjon13.Forelesning08;

import android.os.AsyncTask;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetCardsTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        Log.v("doInBackground", "Starting ..");
        if (params[0] == null || ((String) params[0]).isEmpty()) {
            return null;
        }

        Log.v("doInBackground", "Params are OK.");
        try {
            searchForCards(params[0]);
        } catch (IOException e) {
            Log.e("search", "An error occurred when getting document!");
        }


        return null;
    }

    private List<Card> searchForCards(String cardName) throws IOException {
        final String searchURL = "http://cardkingdom.com/catalog/view?search=basic&filter%5Bname%5D=";
        final String cardNameFormatted = cardName.trim().replaceAll(" ", "+");

        final Document document = Jsoup.connect(searchURL + cardNameFormatted).get();

//        final String cardLink = document.select("body > div.colmask.holygrail > div > div > div.col1wrap > div " +
//                "> div > table:nth-child(10) > tbody > tr:nth-child(2) > td:nth-child(1) > a").get(0).attr("href");
//        Log.d("doInBackground", cardLink.toString());

        final Elements cards = document.select("body > div.colmask.holygrail > div > div > div.col1wrap > div " +
                "> div > table:nth-child(10) > tbody > tr"); //  > td > a
        cards.remove(0);

        for (int i = 0; i < cards.size(); i++) {
            final String matchingCard = cards.get(i).child(0).child(0).text().trim();
            if (matchingCard.toLowerCase().equals(cardName.trim().toLowerCase())) {
                Log.d("foundCards", cards.get(i).toString());
            } else {
                // If current card title is blank, check if it's a continuation of the previous card.
                // Some cards are once with title, but have several entries for different conditions.
                if (i > 0 && matchingCard.isEmpty() && cards.get(i - 1)
                                .child(0)
                                .child(0)
                                .text()
                                .trim()
                                .toLowerCase()
                                .equals(cardName.trim().toLowerCase())) {
                    Log.d("foundCards", cards.get(i).toString());
                }
            }
        }

        final ArrayList<Card> foundCards = new ArrayList<Card>();
        // TODO parse cards
        return foundCards;
    }
}
