package fitnesse.wikitext.translator;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.parser.Symbol;

public class TableBuilder implements Translation {
    public String toHtml(Translator translator, Symbol symbol) {
        HtmlTag table = new HtmlTag("table");
        table.addAttribute("border", "1");
        table.addAttribute("cellspacing", "0");
        int longestRow = longestRow(symbol);
        for (Symbol child: symbol.getChildren()) {
            HtmlTag row = new HtmlTag("tr");
            table.add(row);
            int extraColumnSpan = longestRow - rowLength(child);
            int column = 1;
            for (Symbol grandChild: child.getChildren()) {
                String body = translator.translate(grandChild);
                HtmlTag cell = new HtmlTag("td", body.trim());
                if (extraColumnSpan > 0 && column == rowLength(child))
                    cell.addAttribute("colspan", Integer.toString(extraColumnSpan + 1));
                row.add(cell);
                column++;
            }
        }
        return table.html();
    }

    private int longestRow(Symbol table) {
        int longest = 0;
        for (Symbol row: table.getChildren()) {
            int length = rowLength(row);
            if (length > longest) longest = length;
        }
        return longest;
    }

    private int rowLength(Symbol row) {
        return row.getChildren().size();
    }
}
