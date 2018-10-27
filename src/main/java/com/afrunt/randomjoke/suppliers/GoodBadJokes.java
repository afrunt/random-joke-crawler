package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;
import org.htmlcleaner.BaseToken;
import org.htmlcleaner.TagNode;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Andrii Frunt
 */
public class GoodBadJokes extends AbstractSupplier {
    @Override
    public String getSource() {
        return "goodbadjokes.com";
    }

    @Override
    public Joke get() {
        TagNode page = tagNodeFromUrl("https://www.goodbadjokes.com");

        TagNode firstJoke = page.findElementByAttValue("class", "joke-body-wrap", true, true);
        TagNode surroundingLink = firstJoke.getAllElements(false)[0];

        return new Joke().setText(extractJokeFromLinkTag(surroundingLink));
    }

    private String extractJokeFromLinkTag(TagNode link) {
        String joke;
        if (link.getAllChildren().size() == 1) {
            BaseToken dl = link.getAllChildren().get(0);
            if (dl instanceof TagNode) {
                joke = ((TagNode) dl).getChildTagList().stream().map(c -> c.getText().toString()).collect(Collectors.joining("\n"));
            } else {
                joke = dl.toString();
            }
        } else {
            joke = allChildrenToString(link);
        }

        return Arrays.stream(joke.split("\n"))
                .filter(l -> !l.trim().isEmpty())
                .collect(Collectors.joining("\n"));
    }

    private String allChildrenToString(TagNode node) {
        StringBuilder sb = new StringBuilder();
        for (BaseToken token : node.getAllChildren()) {
            String line = token.toString();
            if (!line.equals("br")) {
                sb.append(line);
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
