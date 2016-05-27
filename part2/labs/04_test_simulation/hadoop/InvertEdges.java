import java.io.IOException;
import java.util.ArrayList;
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



public class InvertEdges {
    
    private static final String JOB_NAME = "InvertEdges";
    private static final String MAP_TAG_1 = "[mapper1] ";
    private static final String MAP_TAG_2 = "[mapper2] ";
    private static final String MAP_TAG_3 = "[mapper3] ";
    private static final String RED_TAG_1 = "[reducer1] ";
    private static final String RED_TAG_2 = "[reducer2] ";
    private static final String RED_TAG_3 = "[reducer3] ";
    private static final String TEMP_DIR_1 = "/outInvertTemp1";
    private static final String TEMP_DIR_2 = "/outInvertTemp2";

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
        
        //tmp folder for second pass output
        Path tmpOut_2 = new Path(TEMP_DIR_2);
		
		
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
	    FileOutputFormat.setOutputPath(job, tmpOut_2);

	    job.setMapperClass(MyMapper2.class);
	    //job.setCombinerClass(MyReducer.class);
	    job.setReducerClass(MyReducer2.class);

        
	    job.setInputFormatClass(TextInputFormat.class);
	    
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
			z.set(xi);        // boxing
			w.set(yi);        // boxing
			context.write(z, w);
		}
	}

	public static class MyReducer extends Reducer<IntWritable, IntWritable, Text, Text> {

		private String out;
		private Text xt = new Text();
		private Text yt = new Text();

		@Override
		protected void reduce(IntWritable node, Iterable<IntWritable> neighbours, Context context)
						throws IOException, InterruptedException {
			
			/*
			 * compute the neighbours list of node
			 * */
			
			out="";
 
			for (IntWritable x: neighbours)
				out += x +"\t";
            
            xt.set(node.toString());
            yt.set(out.trim());
            context.write(xt, yt);
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
