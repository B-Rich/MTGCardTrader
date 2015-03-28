package no.wact.jenjon13.MTGCardTrader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import no.wact.jenjon13.Forelesning08.R;

import java.util.List;

public class CardsAdapter extends BaseAdapter {
    private final Activity activity;
    private List<Card> cards;

    public CardsAdapter(List<Card> cards, Activity activity) {
        this.cards = cards;
        this.activity = activity;
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.txtTitle)).setText(cards.get(position).getTitle());
        ((TextView) convertView.findViewById(R.id.txtEdition)).setText(cards.get(position).getEdition());
        ((TextView) convertView.findViewById(R.id.txtPrice))
                .setText(cards.get(position).getAmount() + "x$" + cards.get(position).getPrice());
        ((TextView) convertView.findViewById(R.id.txtCondition)).setText(cards.get(position).getCondition().toString());

        return convertView;
    }
}
