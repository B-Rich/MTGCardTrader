package no.wact.jenjon13.MTGCardTrader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import no.wact.jenjon13.Forelesning08.R;
import org.json.JSONArray;
import org.jsoup.Jsoup;

import java.util.ArrayList;

public class MainActivity extends Activity {
    public static final String apiUrl = "http://api.mtgdb.info/";
    public static final String cardsQuery = "search/";

    private ArrayList<Card> leftCards = new ArrayList<>();
    private ArrayList<Card> rightCards = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);

        final CardsAdapter leftAdapter = new CardsAdapter(leftCards, MainActivity.this);
        final ListView listViewLeft = (ListView) findViewById(R.id.listViewLeft);
        listViewLeft.setAdapter(leftAdapter);
        listViewLeft.setOnItemClickListener(createOnItemClickListener(leftAdapter, false));

        final CardsAdapter rightAdapter = new CardsAdapter(rightCards, MainActivity.this);
        final ListView listViewRight = (ListView) findViewById(R.id.listViewRight);
        listViewRight.setAdapter(rightAdapter);
        listViewRight.setOnItemClickListener(createOnItemClickListener(rightAdapter, true));

        findViewById(R.id.btnAddLeft).setOnClickListener(listener(leftCards, leftAdapter, R.id.txtPriceLeft));
        findViewById(R.id.btnAddRight).setOnClickListener(listener(rightCards, rightAdapter, R.id.txtPriceRight));

        ((TextView) findViewById(R.id.editCardname)).addTextChangedListener(autoCompleteFromAPIListener());
    }

    private TextWatcher autoCompleteFromAPIListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int MIN_QUERY_INPUT = 2;
                if (s.length() >= MIN_QUERY_INPUT) {
                    updateAutoComplete(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private void updateAutoComplete(final String enteredText) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String result = Jsoup.connect(apiUrl + cardsQuery + enteredText)
                            .ignoreContentType(true)
                            .execute()
                            .body()
                            .toString();

                    final JSONArray jsonArray = new JSONArray(result);
                    final StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        builder.append(" " + jsonArray.getJSONObject(i).getString("name"));
                    }
                    Log.v("onTextChanged", builder.toString());
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }).start();
    }

    private AdapterView.OnItemClickListener createOnItemClickListener(final CardsAdapter adapter, final boolean
            rightSide) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Card currentCard = (Card) adapter.getItem(position);

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
                                recalculateTotal(adapter, rightSide);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();

                ((TextView) dialogView.findViewById(R.id.txtCardTitle)).setText(currentCard.getTitle());
                ((EditText) dialogView.findViewById(R.id.txtAmount)).setText(String.valueOf(currentCard.getAmount()));
                alertDialog.show();

                dialogView.findViewById(R.id.btnRemove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.getCards().remove(currentCard);
                        recalculateTotal(adapter, rightSide);
                        alertDialog.dismiss();
                    }
                });

                final View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateCardCondition(currentCard, Condition.valueOf(((Button) v).getText()
                                .toString()), adapter, rightSide);
                        alertDialog.dismiss();
                    }
                };

                final int[] buttons = {R.id.btnNM, R.id.btnEX, R.id.btnVG, R.id.btnG};
                for (int button : buttons) {
                    final Button btn = (Button) dialogView.findViewById(button);
                    btn.setPressed(currentCard.getCondition().toString()
                            .equalsIgnoreCase(btn.getText().toString().trim()));

                    btn.setOnClickListener(listener);
                }
            }
        };
    }

    private void updateCardCondition(Card currentCard, Condition condition, CardsAdapter adapter, boolean rightSide) {
        currentCard.setCondition(condition);

        if (currentCard.getPrice() == -1) {
            new GetPricesForCardTask(MainActivity.this, adapter, rightSide).execute(currentCard);
        } else {
            recalculateTotal(adapter, rightSide);
        }
    }


    public void recalculateTotal(CardsAdapter adapter, boolean rightSide) {
        float totalPrice = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            final Card card = (Card) adapter.getItem(i);
            totalPrice += (card.getPrice() * card.getAmount());
        }

        ((TextView) findViewById(rightSide ? R.id.txtPriceRight : R.id.txtPriceLeft))
                .setText(String.format("%.2f$", totalPrice));
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener listener(final ArrayList<Card> cards,
            final CardsAdapter adapter, final int priceTxtId) {
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
