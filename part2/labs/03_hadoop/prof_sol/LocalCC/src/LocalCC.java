import java.io.*;
import java.util.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

public class LocalCC {
	
	private static final String TEMP_DIR = "/interm";

	public static void main(String[] args) throws Exception {

		int numReducers = 1;
		List<String> otherArgs = new ArrayList<String>();
		for (int i=0; i<args.length; i++)
			if ("-r".equals(args[i])) 
				 numReducers = Integer.parseInt(args[++i]);
			else otherArgs.add(args[i]);

		boolean res = runJob(numReducers, 
                             new Path(otherArgs.get(0)),
                             new Path(TEMP_DIR),
                             null,
                             Reducer1.class);
		if (!res) System.exit(1);

		res = runJob(numReducers, 
                     new Path(TEMP_DIR),
                     new Path(otherArgs.get(1)),
                     null,
                     Reducer2.class);
		if (!res) System.exit(1);

		System.exit(0);
	}

	protected static boolean runJob(int numReducers, 
            Path in, Path out, 
            Class<? extends Mapper> mapper, 
            Class<? extends Reducer> reducer) throws Exception {

		Job job = Job.getInstance();
		
		if (mapper!=null) job.setMapperClass(mapper);
		job.setReducerClass(reducer);
		job.setJarByClass(LocalCC.class);

		job.setNumReduceTasks(numReducers);

		job.setInputFormatClass(KeyValueTextInputFormat.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, in);
		FileOutputFormat.setOutputPath(job, out);

		return job.waitForCompletion(true);
	}

	public static class Reducer1 extends Reducer<Text, Text, Text, Text> {

		private List<String> g = new ArrayList<String>();
		private Text         a = new Text();
		private Text         b = new Text();

		@Override
		protected void reduce(Text y, Iterable<Text> vals, Context context)
						throws IOException, InterruptedException {

			// collect neighbors of y from vals
			g.clear();
			for (Text v: vals) g.add(v.toString());

			// cartesian product of neighbors
			for (String x: g)
				for (String z: g) {
                    if (x.equals(z)) continue;
                    a.set(x);
                    b.set(y+";"+z);
                    context.write(a,b); // write ("x","y;z") pair
				}

            // write ("y", "#neighbors of y")
            a.set(g.size()+"");
            context.write(y, a);
		}
	}

	public static class Reducer2 extends Reducer<Text,Text,Text,Text> {
		private Text cc = new Text();
        private Set<String> m = new HashSet<String>();

		@Override
		protected void reduce(Text x, Iterable<Text> vals, Context context)
				throws IOException, InterruptedException {

            int n = 0, d = 0;

			// compute numerator and denominator of clustering coeff.
			m.clear();
			for (Text v: vals) {
                String[] t = v.toString().split(";");
                if (t.length == 2) 
                    if (m.contains(t[1]+";"+t[0])) n++;
                    else m.add(v.toString());
                else d = Integer.parseInt(t[0]);
            }

            cc.set(String.format("%.2f",(d<2 ? 0.0 : 2.0*n/(d*(d-1)))));
            context.write(x,cc);
		}
	}
}
