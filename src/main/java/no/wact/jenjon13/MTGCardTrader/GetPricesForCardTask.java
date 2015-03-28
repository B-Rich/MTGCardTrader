package no.wact.jenjon13.MTGCardTrader;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GetPricesForCardTask extends AsyncTask<Card, Void, Void> {
    private final Activity activity;
    private final CardsAdapter adapter;
    private final boolean rightSide;
    private String message = null;

    public GetPricesForCardTask(Activity activity, CardsAdapter adapter, boolean rightSide) {
        this.activity = activity;
        this.adapter = adapter;
        this.rightSide = rightSide;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (message != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            message = null;
            return;
        }

        adapter.notifyDataSetChanged();
        ((MainActivity) activity).recalculateTotal(adapter, rightSide);
    }

    @Override
    protected Void doInBackground(Card... params) {
        final Card card = params[0];
        final Document document;

        try {
            document = Jsoup.connect(card.getPageURL()).get();
        } catch (IOException e) {
            message = "An error occurred when getting document!";
            Log.e("doInBackground", message);
            return null;
        }

        final Elements select = document.select("body > div.colmask.holygrail > div > div > div.col1wrap > div > div " +
                "> table.grid > tbody > tr > td:nth-child(3) > form > table.grid.compact > tbody > tr");

        if (select.size() < 1) {
            message = "Price table not found for requested card!";
            Log.e("doInBackground", message);
        }

        card.addNewPrice(Condition.NM, Float.parseFloat(select.get(1).child(1).text().trim()));
        card.addNewPrice(Condition.EX, Float.parseFloat(select.get(2).child(1).text().trim()));
        card.addNewPrice(Condition.VG, Float.parseFloat(select.get(3).child(1).text().trim()));
        card.addNewPrice(Condition.G, Float.parseFloat(select.get(4).child(1).text().trim()));
        return null;
    }
}
