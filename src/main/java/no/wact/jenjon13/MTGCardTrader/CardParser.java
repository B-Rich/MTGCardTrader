package no.wact.jenjon13.MTGCardTrader;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class CardParser {
    public static List<Card> parse(List<Element> elements) {

        final ArrayList<Card> resultingList = new ArrayList<Card>(elements.size());
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).child(0).child(0).text().trim().isEmpty()) {
                resultingList.add(new Card(resultingList.get(resultingList.size() - 1),
                        elements.get(i).child(1).text(),
                        elements.get(i).child(2).text(),
                        Float.parseFloat(elements.get(i).child(3).text().trim())
                ));
                continue;
            }

            resultingList.add(new Card(
                    elements.get(i).child(0).child(0).text(),
                    elements.get(i).child(1).text(),
                    elements.get(i).child(2).text(),
                    "N/A",
                    elements.get(i).child(4).text(),
                    elements.get(i).child(5).text(),
                    elements.get(i).child(6).text(),
                    elements.get(i).child(7).text(),
                    Float.parseFloat(elements.get(i).child(8).text().trim())
            ));
        }

        return resultingList;
    }
}
