package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;
import org.htmlcleaner.BaseToken;
import org.htmlcleaner.TagNode;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * @author Andrii Frunt
 */
public class GoodBadJokes extends AbstractJokeSupplier {
    private Stack<String> jokeStack = new Stack<>();

    @Override
    public String getSource() {
        return "goodbadjokes.com";
    }

    @Override
    public Joke get() {

        if (jokeStack.isEmpty()) {
            TagNode page = tagNodeFromUrl("https://www.goodbadjokes.com");

            List<? extends TagNode> jokeNodes = page.getElementListByAttValue("class", "joke-body-wrap", true, true);

            jokeNodes.stream().map(this::extractJoke).forEach(j -> jokeStack.push(j));
        }
        return new Joke()
                .setText(jokeStack.pop());
    }

    private String extractJoke(TagNode jokeNode) {
        TagNode surroundingLink = jokeNode.getAllElements(false)[0];
        return extractJokeFromLinkTag(surroundingLink);
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
