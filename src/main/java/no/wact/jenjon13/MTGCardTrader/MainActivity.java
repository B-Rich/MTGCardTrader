package no.wact.jenjon13.MTGCardTrader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import no.wact.jenjon13.Forelesning08.R;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<Card> cards = new ArrayList<Card>();
    private CardsAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);

        adapter = new CardsAdapter(cards, MainActivity.this);

        final ListView listView = (ListView) findViewById(R.id.listViewLeft);
        listView.setAdapter(adapter);

        final Button searchButton = (Button) findViewById(R.id.btnAddLeft);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetCardsTask(MainActivity.this, cards, adapter)
                        .execute(((EditText) findViewById(R.id.editCardname)).getText().toString());
            }
        });
    }
}
