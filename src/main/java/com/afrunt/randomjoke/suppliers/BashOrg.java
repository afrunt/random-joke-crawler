package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;
import org.apache.commons.text.StringEscapeUtils;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.TagNode;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * @author Andrii Frunt
 */
public class BashOrg extends AbstractJokeSupplier {
    private Stack<String> jokeStack = new Stack<>();

    @Override
    public Joke get() {
        return new Joke().setText(findFirstRandomJoke());
    }

    private String findFirstRandomJoke() {
        if (jokeStack.size() > 0) {
            return jokeStack.pop();
        } else {

            TagNode page = tagNodeFromUrl("http://bash.org/?random");

            List<? extends TagNode> jokeNodes = page.getElementListByAttValue("class", "qt", true, true);

            jokeNodes.forEach(jn -> jokeStack.push(extractJoke(jn)));
            return jokeStack.pop();
        }
    }

    @Override
    public String getSource() {
        return "bash.org";
    }

    private String extractJoke(TagNode jokeNode) {
        return jokeNode.getAllChildren().stream()
                .map(c -> c instanceof ContentNode ? StringEscapeUtils.unescapeHtml4(((ContentNode) c).getContent()) : "\n")
                .collect(Collectors.joining());
    }
}
