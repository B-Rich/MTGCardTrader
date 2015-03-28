package no.wact.jenjon13.MTGCardTrader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import no.wact.jenjon13.Forelesning08.R;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<Card> leftCards = new ArrayList<Card>();
    private ArrayList<Card> rightCards = new ArrayList<Card>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);

        CardsAdapter leftAdapter = new CardsAdapter(leftCards, MainActivity.this);
        ((ListView) findViewById(R.id.listViewLeft)).setAdapter(leftAdapter);

        final CardsAdapter rightAdapter = new CardsAdapter(rightCards, MainActivity.this);
        final ListView listView = (ListView) findViewById(R.id.listViewRight);
        listView.setAdapter(rightAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Card currentCard = (Card) rightAdapter.getItem(position);

                final View dialogView = getLayoutInflater().inflate(R.layout.carddialog, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        //.setTitle("Card dialog")
                        .setView(dialogView)
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                currentCard.setAmount(Integer.parseInt(((TextView) dialogView.findViewById(R.id
                                        .txtAmount))
                                        .getText()
                                        .toString()));
                                recalculateTotal(rightAdapter, ((TextView) findViewById(R.id.txtPriceRight)));
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();

                ((TextView) dialogView.findViewById(R.id.txtCardTitle)).setText(currentCard.getTitle());
                ((EditText) dialogView.findViewById(R.id.txtAmount)).setText(String.valueOf(currentCard.getAmount()));
                alertDialog.show();

                ((Button) dialogView.findViewById(R.id.btnRemove)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        java.util.List<Card> cards = rightAdapter.getCards();
                        for (int i = 0; i < cards.size(); i++) {
                            if (cards.get(i).equals(currentCard)) {
                                cards.remove(i);
                                rightAdapter.notifyDataSetChanged();
                                recalculateTotal(rightAdapter, ((TextView) findViewById(R.id.txtPriceRight)));
                                break;
                            }
                        }

                        alertDialog.dismiss();
                    }
                });

                final View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateCardCondition(((Button) v).getText().toString());
                        alertDialog.dismiss();
                    }
                };

                final int[] buttons = {R.id.btnNM, R.id.btnEX, R.id.btnVG, R.id.btnG};
                for (int button : buttons) {
                    final Button btn = (Button) dialogView.findViewById(button);
                    btn.setPressed(currentCard.getCondition()
                            .toLowerCase()
                            .trim()
                            .equals(btn.getText().toString().toLowerCase().trim()) ? true : false);

                    btn.setOnClickListener(listener);
                }
            }
        });

        findViewById(R.id.btnAddLeft).setOnClickListener(listener(leftCards, leftAdapter, R.id.txtPriceLeft));
        findViewById(R.id.btnAddRight).setOnClickListener(listener(rightCards, rightAdapter, R.id.txtPriceRight));
    }

    private void recalculateTotal(CardsAdapter adapter, TextView priceHolder) {
        float totalPrice = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            final Card card = (Card) adapter.getItem(i);
            totalPrice += (card.getPrice() * card.getAmount());
        }

        priceHolder.setText(String.format("%.2f$", totalPrice));
        adapter.notifyDataSetChanged();
    }

    private void updateCardCondition(String cardName) {
    }

    private View.OnClickListener listener(final ArrayList<Card> cards, final CardsAdapter adapter, final int
            priceTxtId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText enteredCardText = (EditText) findViewById(R.id.editCardname);
                new GetCardsTask(MainActivity.this, cards, adapter, priceTxtId)
                        .execute(enteredCardText.getText().toString());
                enteredCardText.setText("");
            }
        };
    }
}
