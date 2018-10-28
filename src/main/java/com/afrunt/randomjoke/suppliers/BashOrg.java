package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;
import org.apache.commons.text.StringEscapeUtils;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.TagNode;

import java.util.stream.Collectors;

/**
 * @author Andrii Frunt
 */
public class BashOrg extends AbstractJokeSupplier {
    @Override
    public Joke get() {
        return new Joke().setText(findFirstRandomJoke());
    }

    private String findFirstRandomJoke() {
        TagNode jokeNode = tagNodeFromUrl("http://bash.org/?random").findElementByAttValue("class", "qt", true, true);

        return jokeNode.getAllChildren().stream()
                .map(c -> c instanceof ContentNode ? StringEscapeUtils.unescapeHtml4(((ContentNode) c).getContent()) : "\n")
                .collect(Collectors.joining());
    }

    @Override
    public String getSource() {
        return "bash.org";
    }
}
