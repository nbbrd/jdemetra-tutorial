package be.nbb.demetra.dstats;

import ec.tstoolkit.data.DescriptiveStatistics;
import ec.tstoolkit.utilities.DoubleList;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.netbeans.api.sendopts.CommandException;
import org.netbeans.api.sendopts.CommandLine;
import org.netbeans.spi.sendopts.Arg;
import org.netbeans.spi.sendopts.ArgsProcessor;
import org.netbeans.spi.sendopts.Description;
import org.netbeans.spi.sendopts.Env;

/**
 * Hello world!
 *
 */
public class Dstats implements ArgsProcessor {

    public static void main(String[] args) throws CommandException {
        //http://dstats.sourceforge.net/
        CommandLine.create(Dstats.class).process(args);
    }
    //
    @Arg(longName = "datapoints", shortName = 't', defaultValue = "DATA_COUNT,SUM,MIN,MAX,AVERAGE,VAR,STDEV")
    @Description(shortDescription = "T option controls which datapoints are (possibly computed) and displayed.")
    public String datapoints;
    //
    @Arg(longName = "help", shortName = '?')
    @Description(shortDescription = "print help")
    public boolean help;
    //
    @Arg(longName = "files")
    public String[] files;

    @Override
    public void process(Env env) throws CommandException {
        if (help == true) {
            env.usage();
        } else {

            DoubleList all = new DoubleList();
            Scanner scanner = new Scanner("1\n 2 3");
//            Scanner scanner = new Scanner(env.getInputStream());
            while (scanner.hasNextDouble()) {
                all.add(scanner.nextDouble());
            }
            System.out.println(Arrays.toString(all.toArray()));

            print(new PrintWriter(env.getOutputStream()), splitDataPoints(), all.toArray());
        }
    }

    List<DStatItem> splitDataPoints() {
        String[] tmp = datapoints.split(",");
        List<DStatItem> items = new ArrayList<DStatItem>();
        for (String o : tmp) {
            items.add(DStatItem.valueOf(o.trim()));
        }
        return items;
    }

    public static void print(PrintWriter writer, List<DStatItem> items, double[] values) {
        DescriptiveStatistics stats = new DescriptiveStatistics(values);
        for (DStatItem o : items) {
            writer.print(o.getDisplayName());
            writer.print("=");
            writer.print(o.getValue(stats));
            writer.print(" ");
        }
        writer.println();
        writer.flush();
    }
}
