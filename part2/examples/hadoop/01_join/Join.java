import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

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
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ToolRunner;


public class Join {

	static int printUsage() {
		System.out.println("Join [-r <reduces>] <input> <output>");
		ToolRunner.printGenericCommandUsage(System.out);
		return -1;
	}

	public static void main(String[] args) throws Exception {
		
		List<String> otherArgs = new ArrayList<String>();

		Configuration conf = new Configuration();
		
		for(int i=0; i < args.length; ++i) {
			try {
				if ("-r".equals(args[i])) {
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
		
		Path input = new Path(otherArgs.get(0));
		Path output =new Path(otherArgs.get(1));
		
		Job job = Job.getInstance(conf, "join program");
		job.setJarByClass(Join.class);
		
		FileInputFormat.addInputPath(job, input);
		FileOutputFormat.setOutputPath(job, output);

		job.setMapperClass(MyMapper.class);
		//job.setCombinerClass(MyReducer.class);
		job.setReducerClass(MyReducer.class);

		// An InputFormat for plain text files. 
		// Files are broken into lines. Either linefeed or carriage-return are used 
		// to signal end of line. Keys are the position in the file, and values 
		// are the line of text.
		job.setInputFormatClass(TextInputFormat.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.waitForCompletion(true);
	}
	
	public static class MyMapper extends Mapper<LongWritable, Text, Text, Text>{
		
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
					
			//DEBUG -> printed in log, not stdout
			System.out.println("[mapper] value = " + value);

			String[] tok = value.toString().split(" ");
			String rel = tok[0];
			String v1 = tok[1];
			String v2 = tok[2];
			
			//DEBUG -> printed in log, not stdout
			System.out.println("[mapper] rel name = " + rel + " v1 = " + v1 + " v2 = " + v2);

			if (rel.equals("A")) 
				 context.write(new Text(v1),	   // name
							  new Text("A "+v2));  // (rel, age)
			else 
				 context.write(new Text(v2),	   // name
							  new Text("B "+v1));  // (rel, class)
	   	}
	}
	
	public static class MyReducer extends Reducer<Text, Text, Text, Text>{

		@Override
		protected void reduce(Text name, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			List<String> A = new ArrayList<String>();
			List<String> B = new ArrayList<String>();
			
			Iterator<Text> it = values.iterator();
			
			while (it.hasNext()) {
				String v = it.next().toString();
				String[] tok = v.split(" ");
				if (tok[0].equals("A")) A.add(tok[1]);
				else B.add(tok[1]);
			}
			
			//DEBUG -> printed in log, not stdout
			System.out.println("[reducer] key = " + name + " [A] = " + A.size() + " [B] = " + B.size());

			for (String age:A)
				for (String cls:B)
					context.write(new Text("C"), 
								  new Text(name+" "+age+" "+cls));
		}
	}
}




