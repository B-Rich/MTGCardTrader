package no.wact.jenjon13.Forelesning08;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);

        final Button searchButton = (Button) findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetCardsTask().execute(((EditText) findViewById(R.id.editCardname)).getText().toString());
            }
        });
    }
}
