import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ToolRunner;



public class ClusteringCoefficient {
	
	private static final String JOB_NAME = "ClusteringCoefficient";
	private static final String MAP_TAG_1 = "[mapper1] ";
	private static final String MAP_TAG_2 = "[mapper2] ";
	private static final String RED_TAG_1 = "[reducer1] ";
	private static final String RED_TAG_2 = "[reducer2] ";
	private static final String TEMP_DIR_1 = "/outClustTemp1";

	static int printUsage() {
		System.out.println(JOB_NAME + " [-r <reducers>] <input> <output>");
		ToolRunner.printGenericCommandUsage(System.out);
		return -1;
	}

	public static void main(String[] args) throws Exception {
		
		List<String> otherArgs = new ArrayList<String>();

		Configuration conf = new Configuration();
		
		for(int i=0; i < args.length; ++i) {
			try {
				if ("-r".equals(args[i])) { 
					//to customize the number of reducers
					conf.setInt("mapreduce.job.reduces", Integer.parseInt(args[++i]));
				} else {
					otherArgs.add(args[i]);
				}
			} catch (NumberFormatException except) {
				System.out.println("ERROR: Integer expected instead of " + args[i]);
				System.exit(printUsage());
			} catch (ArrayIndexOutOfBoundsException except) {
				System.out.println("ERROR: Required parameter missing from " +
						args[i-1]);
				System.exit(printUsage());
			}
		}

		// Make sure there are exactly 2 parameters left.
		if (otherArgs.size() != 2) {
			System.out.println("ERROR: Wrong number of parameters: " +
					otherArgs.size() + " instead of 2.");
			System.exit(printUsage());
		}
		
		//take input and output folders from command line
		Path input = new Path(otherArgs.get(0));
		Path output =new Path(otherArgs.get(1));
		
		//tmp folder for first pass output
		Path tmpOut_1 = new Path(TEMP_DIR_1);		
		
		/*
		 * BEGIN OF FIRST PASS
		 * */
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(ClusteringCoefficient.class);
		job.setJobName(JOB_NAME);
		
		FileInputFormat.addInputPath(job, input);
		FileOutputFormat.setOutputPath(job, tmpOut_1);

		job.setMapperClass(MyMapper.class);
		//job.setCombinerClass(MyReducer.class);
		job.setReducerClass(MyReducer.class);

	
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		
		//to specify the types of intermediate result key and value
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(IntWritable.class);

		//to specify the types of output key and value
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.waitForCompletion(true);
		
		
		
		/*
		 * BEGIN OF SECOND PASS
		 * */
		 
		job = Job.getInstance(conf);
		job.setJarByClass(ClusteringCoefficient.class);
		job.setJobName(JOB_NAME);
		
		FileInputFormat.addInputPath(job, tmpOut_1);
		FileOutputFormat.setOutputPath(job, output);

		job.setMapperClass(MyMapper2.class);
		//job.setCombinerClass(MyReducer.class);
		job.setReducerClass(MyReducer2.class);

		
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		
		//to specify the types of intermediate result key and value
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);
		

		//to specify the types of output key and value
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.waitForCompletion(true);
		
	}

	
	public static class MyMapper extends Mapper<Text, Text, IntWritable, IntWritable> {

		private IntWritable z = new IntWritable();
		private IntWritable w = new IntWritable();
		int xi, yi;

		@Override
		protected void map(Text x, Text y, Context context)
				throws IOException, InterruptedException {
					
			/*
			 * identity function
			 *  
			 * It would be not necessary to define a mapper here, since
			 * identity function is the default. 
			 * Although, with this implementation a smaller amount of bytes 
			 * is passed from the mapper to the reducer. Since it represents 
			 * the bottleneck of MapReduce, performances are improved.
			 * 
			 * NOTE: the undirected graph in input has to be represented
			 * as a list of edges s.t. if u,v is in the list ALSO v,u has
			 * to occur
			 * */
			
			xi = Integer.parseInt(x.toString());
			yi = Integer.parseInt(y.toString());
			z.set(xi);		// boxing
			w.set(yi);		// boxing
			context.write(z, w);
		}
	}

	public static class MyReducer extends Reducer<IntWritable, IntWritable, Text, Text> {

		private Text key = new Text();
		private Text value = new Text();
		private ArrayList<String> neighbors = new ArrayList<String>();
		
		@Override
		protected void reduce(IntWritable node, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			
			/* 
			 * Getting all the adjacent nodes of the key node 
			 * */
			neighbors.clear();
			
			for (IntWritable t : values)
				neighbors.add(t.toString());
			
			/* 
			 * For each node n adjacent to key node, write the pair <key=n, 
			 * value=node,<list of all other nodes adjacent to key node>>
			 * Basically we will have in output a list of all the neighbors 
			 * of a neighbor of a node (excluding itself) 
			 * */
			String otherNeighborsOfNode;
			for (int i=0; i<neighbors.size(); i++) {
				otherNeighborsOfNode = "";
				for (int j=0; j<neighbors.size(); j++) {
					if (i!=j)
						otherNeighborsOfNode += neighbors.get(j)+" ";
				}
				
				key.set(neighbors.get(i));
				value.set(node.toString()+","+otherNeighborsOfNode);
				context.write(key, value);
			}
		}
	}
	
	public static class MyMapper2 extends Mapper<Text,Text,IntWritable,Text> {
		private IntWritable outKey = new IntWritable();
		int xi;

		@Override
		protected void map(Text key, Text value, Context context)
				throws IOException, InterruptedException {

			/* 
			 * Just write in output the input:
			 * The reducer will have as input a node as a key and a list 
			 * of its neighbors with all their other neighbors associated 
			 * as a value 
			 * */
			xi = Integer.parseInt(key.toString());
			outKey.set(xi);		// boxing
			context.write(outKey, value);
	   	}
	}

	public static class MyReducer2 extends Reducer<IntWritable,Text,Text,Text> {
		
		private Text outKey = new Text();
		private Text coefficient = new Text();
		
		private ArrayList<String> neighbors = new ArrayList<String>();
		private HashMap<String, ArrayList<String>> neighborsOfNeighbors = new HashMap<String, ArrayList<String>>();

		
		@Override
		protected void reduce(IntWritable node, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			
			/* 
			 * List of neighbors of the key node (the first element of 
			 * each value) 
			 * */	
			neighbors.clear();
			
			/* 
			 * List of neighbors of the first element of each value, 
			 * which is a neighbor of key node 
			 * */
			neighborsOfNeighbors.clear();
			
			String[] toks;
			String[] neighborsOfCurrentNeighbor;
			for (Text t : values) {
				toks = t.toString().split(",");
				String neighbor = toks[0];
				
				neighbors.add(neighbor);
				ArrayList<String> mapValues = new ArrayList<String>();
				
				if (toks.length!=1) {
					neighborsOfCurrentNeighbor = toks[1].split(" ");
					for (int i=0; i<neighborsOfCurrentNeighbor.length; i++) {
						mapValues.add(neighborsOfCurrentNeighbor[i]);
					}
				}
				neighborsOfNeighbors.put(neighbor, mapValues);
			}
			
			int edges = 0;
			for (int i=0; i<neighbors.size(); i++) {
				ArrayList<String> l = neighborsOfNeighbors.get(neighbors.get(i));
				for (String s : l) {
					
					/* 
					 * If a neighbor of a neighbor is also a neighbor of 
					 * the current node, this edge must be counted 
					 * */
					if (neighbors.contains(s))
						edges++;
				}
			}
			
			/* 
			 * Every edge is counted twice in the previous step 
			 * */
			int num = edges/2;
			double result;
			if (neighbors.size()<2) {
				result = 0.0;
			}
			else {
				double den = (neighbors.size()*(neighbors.size()-1))/2;
				result = num/den;
			}
			
			coefficient.set(String.format("%.2f", result));
			outKey.set(node.toString());
			context.write(outKey, coefficient);
		}
	}
}
