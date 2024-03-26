import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static int overwriteCount = 0;

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new FetchDataTask(), 0, 10000); // Fetch data every 10 seconds
    }

    static class FetchDataTask extends TimerTask {
        @Override
        public void run() {
            try {
                // Fetch the HTML content of the webpage
                Document doc = Jsoup.connect("https://www.heavens-above.com/Satellites.aspx?lat=0&lng=0&loc=Unspecified&alt=0&tz=UCT").get();

                // Find all tables with class "standardTable"
                Elements tables = doc.select("table.standardTable");

                // Create a FileWriter instance for writing to a text file
                FileWriter writer = new FileWriter("table_data.txt");

                // Iterate over each table
                for (int i = 0; i < tables.size(); i++) {
                    Element table = tables.get(i);
                    Elements rows = table.select("tr");

                    // Write table identifier
                    writer.write("Table " + (i + 1) + ":\n");

                    // Iterate over each row of the current table
                    for (Element row : rows) {
                        Elements cells = row.select("td");
                        // Iterate over each cell of the current row
                        for (Element cell : cells) {
                            // Write cell content to the text file
                            writer.write(cell.text() + "\t");
                        }
                        // End of row, move to the next line
                        writer.write("\n");
                    }
                    // End of table, add a newline separator
                    writer.write("\n");
                }

                // Close the FileWriter
                writer.close();

                overwriteCount++;
                System.out.println("Text file updated successfully. Overwrite count: " + overwriteCount);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
