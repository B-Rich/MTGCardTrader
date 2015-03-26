package no.wact.jenjon13.MTGCardTrader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import no.wact.jenjon13.Forelesning08.R;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<Card> leftCards = new ArrayList<Card>();
    private ArrayList<Card> rightCards = new ArrayList<Card>();
    private CardsAdapter leftAdapter;
    private CardsAdapter rightAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);

        leftAdapter = new CardsAdapter(leftCards, MainActivity.this);
        ((ListView) findViewById(R.id.listViewLeft)).setAdapter(leftAdapter);

        rightAdapter = new CardsAdapter(rightCards, MainActivity.this);
        ((ListView) findViewById(R.id.listViewRight)).setAdapter(rightAdapter);

        findViewById(R.id.btnAddLeft).setOnClickListener(listener(leftCards, leftAdapter));
        findViewById(R.id.btnAddRight).setOnClickListener(listener(rightCards, rightAdapter));
    }

    private View.OnClickListener listener(final ArrayList<Card> cards, final CardsAdapter adapter) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText enteredCardText = (EditText) findViewById(R.id.editCardname);
                new GetCardsTask(MainActivity.this, cards, adapter)
                        .execute(enteredCardText.getText().toString());
                enteredCardText.setText("");
            }
        };
    }
}
