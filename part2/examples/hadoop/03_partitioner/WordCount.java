import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ToolRunner;


public class WordCount {

	static int printUsage() {
		System.out.println("wordcount [-r <reduces>] <input> <output>");
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
		
		Job job = Job.getInstance(conf);
        job.setJarByClass(WordCount.class);
        job.setJobName("WordCount");
        
	    FileInputFormat.addInputPath(job, input);
	    FileOutputFormat.setOutputPath(job, output);

	    job.setMapperClass(MyMapper.class);
	    job.setReducerClass(MyReducer.class);
	    
	    /*
	     * make a combiner execute the same function as the reducer
	     * */
	    //job.setCombinerClass(MyReducer.class);
	    job.setCombinerClass(MyCombiner.class);
	    
	    /*
	     * define a custom partitioner
	     * */
	    job.setPartitionerClass(MyPartitioner.class);

        // An InputFormat for plain text files. 
        // Files are broken into lines. Either linefeed or carriage-return are used 
        // to signal end of line. Keys are the position in the file, and values 
        // are the line of text.
	    job.setInputFormatClass(TextInputFormat.class);

        //tells the system the types of output key and value
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);

	    job.waitForCompletion(true);
	}
	
    //NESTED CLASS (not INNER) because of the 'static', i want the WordCounter class to be
    //a name space with the mapper and reducer in it
	public static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
		
        //i declare 'one' and 'word' outside the method to save TIME, 
        //avoid the garbage collector to destroy it
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
		
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			Scanner scanner = new Scanner(value.toString());
			scanner.useDelimiter("\\W"); // any non-alphanumeric character
			while (scanner.hasNext()) {
				word.set(scanner.next().toLowerCase());
				context.write(word, one);
			}
       	}
	}
	
	public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable>{

		@Override
		protected void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
            
            //values is a list given by an iterable        
            
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			context.write(key, new IntWritable(sum));   //wrap sum to be sent in network
			
			//DEBUG
			System.out.println("reducing key = "+ key);
		}
	}
	
	public static class MyCombiner extends Reducer<Text, IntWritable, Text, IntWritable>{
		
		/*
		 * just to see the different prints, not good for modularity
		 * we should use the same classe of the reducer
		 * */

		@Override
		protected void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
            
            //values is a list given by an iterable        
            
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			context.write(key, new IntWritable(sum));   //wrap sum to be sent in network
			
			//DEBUG
			System.out.println("combining key = "+ key);
		}
	}
	
	
	public static class MyPartitioner extends Partitioner<Text, IntWritable> {
		@Override
		public int getPartition(Text key, IntWritable value, int numRed) {
			
			/*
			 * use abs() not to have a negative value for reducer
			 * */
			//int reducer = (int) Math.abs(key.hashCode()) % numRed;
			
			/*
			 * force to send all pairs to same reducer
			 * */
			int reducer = 0;
			
			System.out.println("key="+ key + " value=" + value + " reducer="+reducer);
			return reducer;
		}
		
	}
}
