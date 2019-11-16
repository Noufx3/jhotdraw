package org.jhotdraw8.css.function;

import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.collection.ReadOnlyList;
import org.jhotdraw8.css.CssFunctionProcessor;
import org.jhotdraw8.css.CssToken;
import org.jhotdraw8.css.CssTokenType;
import org.jhotdraw8.css.CssTokenizer;
import org.jhotdraw8.css.ListCssTokenizer;
import org.jhotdraw8.css.SelectorModel;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Processes the var() function.
 * <pre>
 *     var = "var(" ,  s* , custom-property-name, s* , [ "," ,  s* , declaration-value ] ,  s* , ")" ;
 *     custom-property-name = ident-token;
 *     declaration-value = fallback-value;
 * </pre>
 * The custom-property-name must start with two dashes "--".
 */
public class VarCssFunction<T> extends AbstractCssFunction<T> {
    /**
     * Function name.
     */
    public final static String NAME = "var";

    public VarCssFunction() {
        this(NAME);
    }

    public VarCssFunction(String name) {
        super(name);
    }

    @Override
    public void process(@NonNull T element, @NonNull CssTokenizer tt, @NonNull SelectorModel<T> model, @NonNull CssFunctionProcessor<T> functionProcessor, @NonNull Consumer<CssToken> out) throws IOException, ParseException {
        tt.requireNextToken(CssTokenType.TT_FUNCTION, "〈var〉: function var() expected.");
        if (!getName().equals(tt.currentString())) {
            throw new ParseException("〈var〉: function var() expected.", tt.getStartPosition());
        }
        int line = tt.getLineNumber();
        int start = tt.getStartPosition();

        tt.requireNextToken(CssTokenType.TT_IDENT, "〈var〉: function custom-property-name expected.");

        String customPropertyName = tt.currentString();
        List<CssToken> attrFallback = new ArrayList<>();
        if (tt.next() == CssTokenType.TT_COMMA) {
            while (tt.nextNoSkip() != CssTokenType.TT_EOF && tt.current() != CssTokenType.TT_RIGHT_BRACKET) {
                attrFallback.add(tt.getToken());
            }
        }
        if (tt.current() != CssTokenType.TT_RIGHT_BRACKET) {
            throw new ParseException("〈attr〉: right bracket expected. " + tt.current(), tt.getStartPosition());
        }
        int end = tt.getEndPosition();

        if (!customPropertyName.startsWith("--")) {
            throw new ParseException("〈var〉: custom-property-name starting with two dashes \"--\" expected. Found: \"" + customPropertyName + "\"", tt.getStartPosition());
        }
        ReadOnlyList<CssToken> customValue = functionProcessor.getCustomProperties().get(customPropertyName);
        if (customValue == null) {
            functionProcessor.process(element, new ListCssTokenizer(attrFallback), out);
        } else {
            functionProcessor.process(element, new ListCssTokenizer(customValue), out);
        }
    }

    @Override
    public String getHelpText() {
        return NAME + "(⟨custom-property-name⟩, ⟨fallback⟩)"
                + "\n    Retrieves a custom-property by name."
                + "\n    If the custom-property is not found, the fallback is used."
                + "\n    A custom-property is a property defined on a parent element (or on ':root')."
                + "\n    The name of a custom-property must start with two dashes: '--'.";

    }

}