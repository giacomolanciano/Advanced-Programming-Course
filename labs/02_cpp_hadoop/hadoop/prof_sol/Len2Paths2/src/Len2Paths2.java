import java.io.*;
import java.util.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

public class Len2Paths2 {
	
	private static final String TEMP_DIR = "/tmp";

	public static void main(String[] args) throws Exception {

		int numReducers = 1;
		List<String> otherArgs = new ArrayList<String>();
		for (int i=0; i<args.length; i++)
			if ("-r".equals(args[i])) 
				 numReducers = Integer.parseInt(args[++i]);
			else otherArgs.add(args[i]);

		boolean res = runLen2Paths(numReducers, 
		                           new Path(otherArgs.get(0)),
		                           new Path(TEMP_DIR));
		if (!res) System.exit(1);

		res = runDupRem(numReducers, 
		                new Path(TEMP_DIR),
		                new Path(otherArgs.get(1)));
		if (!res) System.exit(1);

		System.exit(0);
	}
	
	protected static boolean runLen2Paths(int numReducers, Path in, Path out) throws Exception {
		Job job = Job.getInstance();
		
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);
		job.setJarByClass(Len2Paths2.class);

		job.setNumReduceTasks(numReducers);

		job.setInputFormatClass(KeyValueTextInputFormat.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(EdgeWritable.class);

		FileInputFormat.addInputPath(job, in);
		FileOutputFormat.setOutputPath(job, out);

		return job.waitForCompletion(true);
	}

	protected static boolean runDupRem(int numReducers, Path in, Path out) throws Exception {
		Job job = Job.getInstance();
		
		job.setMapperClass(MyDupRemMapper.class);
		job.setReducerClass(MyDupRemReducer.class);
		job.setJarByClass(Len2Paths2.class);

		job.setNumReduceTasks(numReducers);

		job.setInputFormatClass(KeyValueTextInputFormat.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, in);
		FileOutputFormat.setOutputPath(job, out);

		return job.waitForCompletion(true);
	}

	public static class EdgeWritable implements Writable {

		private int x, y;

		public EdgeWritable(int x, int y) {
			this.set(x,y);
		}
		
		public EdgeWritable() {
			this.set(0,0);
		}

		public int getX() { return x; }
		public int getY() { return y; }

		public void set(int x, int y) { 
			this.x = x; 
			this.y = y; 
		}
	
		@Override
		public void readFields(DataInput in) throws IOException {
			x = in.readInt();
			y = in.readInt();
		}

		@Override
		public void write(DataOutput out) throws IOException {
			out.writeInt(x);
			out.writeInt(y);
		}

		@Override
		public String toString() {
			return "("+x+","+y+")";
		}
	}

	public static class MyMapper extends Mapper<Text, Text, IntWritable, EdgeWritable> {

		private IntWritable z = new IntWritable();
		private EdgeWritable edge = new EdgeWritable();

		@Override
		protected void map(Text x, Text y, Context context)
				throws IOException, InterruptedException {
			int xi = Integer.parseInt(x.toString());
			int yi = Integer.parseInt(y.toString());
			edge.set(xi, yi); // boxing
			z.set(xi);        // boxing
			context.write(z, edge);
			z.set(yi);        // boxing
			context.write(z, edge);
		}
	}

	public static class MyReducer extends Reducer<IntWritable, EdgeWritable, Text, Text> {

		private List<Integer> in  = new ArrayList<Integer>();
		private List<Integer> out = new ArrayList<Integer>();
		private Text xt = new Text();
		private Text yt = new Text();

		@Override
		protected void reduce(IntWritable z, Iterable<EdgeWritable> edges, Context context)
						throws IOException, InterruptedException {
			in.clear();
			out.clear();

			// separate edges entering and leaving z
			for (EdgeWritable edge: edges) {
				if (z.get() == edge.getX()) 
					out.add(edge.getY()); // edge is (z,y)
				else in.add(edge.getX()); // edge is (x,z)
			}

			// cartesian product of in-nodes and out-nodes
			for (Integer x: in)
				for (Integer y: out) {
					xt.set(x.toString()); // boxing
					yt.set(y.toString()); // boxing
					context.write(xt, yt);
				}
		}
	}
	
	public static class MyDupRemMapper extends Mapper<Text,Text,Text,Text> {
		private Text dummy = new Text();
		private Text edge = new Text();

		@Override
		protected void map(Text x, Text y, Context context)
				throws IOException, InterruptedException {
			edge.set(x.toString()+";"+y.toString());
			context.write(edge, dummy);
		}
	}

	public static class MyDupRemReducer extends Reducer<Text,Text,Text,Text> {
		private Text x = new Text();
		private Text y = new Text();

		@Override
		protected void reduce(Text key, Iterable<Text> dummy, Context context)
				throws IOException, InterruptedException {
			String[] edge = key.toString().split(";");
			x.set(edge[0]);
			y.set(edge[1]);
			context.write(x, y);
		}
	}
}
